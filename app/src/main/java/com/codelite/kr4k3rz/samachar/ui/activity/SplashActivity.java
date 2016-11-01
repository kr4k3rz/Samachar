package com.codelite.kr4k3rz.samachar.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.MainActivity;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler mHandler;
    private RingProgressBar ringProgressBar;
    private int tempProgressCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mHandler = new Handler(Looper.getMainLooper());
        ringProgressBar = (RingProgressBar) findViewById(R.id.progress_bar);
        downloadFeedsOnStart();

    }

    private void downloadFeedsOnStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime_loadFeed", false)) {
            loadingFeedsOnFirstStart();
            // mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime_loadFeed", true);
            editor.apply();
        } else {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void loadingFeedsOnFirstStart() {
        final List<Entry> list_Nepali = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final ArrayList<String> rss = new NewspaperNP().getLinksList();

         /*ALWAYS START NEPALI LANG*/
        for (int i = 0; i < rss.size(); i++) {

            Request request = new Request.Builder().url(rss.get(i)).build();
            final int responseCount = i;
            final int finalI = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TAG", "" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();    //loads JSON String feeds by Okhttp
                    Log.i("TAG", " : " + jsonStr);
                    try {
                        JSONObject mainNode = new JSONObject(jsonStr);
                        JSONObject responseData = mainNode.getJSONObject("responseData");
                        JSONObject feeds = responseData.getJSONObject("feed");
                        String feedTitle = feeds.getString("title");
                        String feedLink = feeds.getString("link");
                        Log.i(TAG, "Feedlink : " + feedLink + "\n Feed title : " + feedTitle);
                        JSONArray entries = feeds.getJSONArray("entries");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> tempList;
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                        tempList = Parse.deleteDuplicate(posts);
                        tempList = Parse.deleteEnglishFeeds(tempList);
                        tempList = Parse.sortByTime(tempList);
                        Paper.book().write("NP" + "newspaper" + finalI, tempList);
                        list_Nepali.addAll(setTitleLink(feedTitle, feedLink, posts));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressShow(responseCount, rss);
                            if (responseCount == rss.size() - 1) {
                                FilterCategoryNP filterCategory = new FilterCategoryNP(list_Nepali, getBaseContext());
                                filterCategory.filter();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                }


            });

        }


    }

    private List<Entry> setTitleLink(String feedTitle, String feedLink, List<Entry> posts) {
        List<Entry> entries = new ArrayList<>();
        entries.clear();
        for (int i = 0; i < posts.size(); i++) {
            Entry entry = posts.get(i);
            entry.setTitleFeed(feedTitle);
            entry.setLinkFeed(feedLink);
            entries.add(entry);
        }
        return entries;
    }

    private void progressShow(float responseCount, ArrayList<String> rss) {
        int progress = (int) (((responseCount + 1) / (float) rss.size()) * 100);
        if (progress >= tempProgressCounter) {
            Log.i(TAG, "Progress : " + progress + "%");
            ringProgressBar.setProgress(progress);
            tempProgressCounter = progress;
        }
    }

}
