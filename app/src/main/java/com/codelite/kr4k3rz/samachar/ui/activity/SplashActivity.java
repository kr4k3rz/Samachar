package com.codelite.kr4k3rz.samachar.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.MainActivity;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mHandler = new Handler(Looper.getMainLooper());
        changeNightMode();
        downloadFeedsOnStart();
    }

    private void changeNightMode() {
        boolean checked = false;
        if (Paper.book().exist("NightMode")) {
            checked = Paper.book().read("NightMode");
        }
        if (checked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }

    private void downloadFeedsOnStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime_loadFeed", false)) {
            loadingFeedsOnFirstStart();
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
        final List<EntriesItem> list_Nepali = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final ArrayList<String> rss = new NewspaperNP().getLinksList();
        final Dispatcher dispatcher = okHttpClient.dispatcher();

         /*ALWAYS START NEPALI LANG*/
        for (int i = 0; i < rss.size(); i++) {

            Request request = new Request.Builder().url(rss.get(i)).build();
            final int responseCount = i;
            final int finalI = i;
            final int finalI1 = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (finalI1 == rss.size() - 1) {
                        Log.i(TAG,"i am inside ");
                        dispatcher.cancelAll();
                        FilterCategoryNP filterCategory = new FilterCategoryNP(list_Nepali, getBaseContext());
                        filterCategory.filter();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    Log.i("TAG", "" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();    //loads JSON String feeds by Okhttp
                    Log.i("TAG", " : " + jsonStr);
                    List<EntriesItem> tempList;
                    ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                    List<EntriesItem> posts = responseFeed.getResponseData().getFeed().getEntries();
                    tempList = Parse.deleteDuplicate(posts);
                    tempList = Parse.deleteEnglishFeeds(tempList);
                    tempList = Parse.sortByTime(tempList);
                    Paper.book().write("NP" + "newspaper" + finalI, tempList);
                    list_Nepali.addAll(setTitleLink(responseFeed.getResponseData().getFeed().getTitle(), responseFeed.getResponseData().getFeed().getLink(), posts));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
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

    private List<EntriesItem> setTitleLink(String feedTitle, String feedLink, List<EntriesItem> posts) {
        List<EntriesItem> entries = new ArrayList<>();
        entries.clear();
        for (int i = 0; i < posts.size(); i++) {
            EntriesItem entry = posts.get(i);
            entry.setTitleFeed(feedTitle);
            entry.setLinkFeed(feedLink);
            entries.add(entry);
        }
        return entries;
    }

}
