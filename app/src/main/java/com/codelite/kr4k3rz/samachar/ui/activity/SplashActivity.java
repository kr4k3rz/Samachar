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
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.FilterCategory;
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
    static final String TAG = SplashActivity.class.getSimpleName();
    Handler mHandler;
    RingProgressBar ringProgressBar;
    int tempProgressCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mHandler = new Handler(Looper.getMainLooper());
        ringProgressBar = (RingProgressBar) findViewById(R.id.progress_bar);
        initFeedList();
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

            if (CheckInternet.isNetworkAvailable(getBaseContext()))
                loadFeeds();
            else {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }


    }

    private void loadingFeedsOnFirstStart() {
        final List<Entry> list = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final String[] rss = FeedLists.getFeedListCached(0);
        for (int i = 0; i < rss.length; i++) {
            Request request = new Request.Builder().url(rss[i]).build();
            final int responseCount = i;
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
                        JSONArray entries = feeds.getJSONArray("entries");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                        list.addAll(posts);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            int progress = (int) (((responseCount + 1) / (float) rss.length) * 100);
                            if (progress >= tempProgressCounter) {
                                Log.i(TAG, "Progress : " + progress + "%");
                                ringProgressBar.setProgress(progress);
                                tempProgressCounter = progress;
                            }
                            if (responseCount == rss.length - 1) {
                                FilterCategory filterCategory = new FilterCategory(list, getBaseContext());
                                filterCategory.filter().get(1);
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

    private void loadFeeds() {
        final List<Entry> list = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final String[] rss = FeedLists.getFeedListLatest(0);
        for (int i = 0; i < rss.length; i++) {
            Request request = new Request.Builder().url(rss[i]).build();
            final int failureCount = i;
            final float responseCount = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    okHttpClient.dispatcher().cancelAll();
                    Log.i("TAG", "" + e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (failureCount == rss.length - 1) {
                                FilterCategory filterCategory = new FilterCategory(list, getBaseContext());
                                filterCategory.filter().get(1);
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();    //loads JSON String feeds by Okhttp
                    Log.i("TAG", " : " + jsonStr);
                    try {
                        JSONObject mainNode = new JSONObject(jsonStr);
                        JSONObject responseData = mainNode.getJSONObject("responseData");
                        JSONObject feeds = responseData.getJSONObject("feed");
                        JSONArray entries = feeds.getJSONArray("entries");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                        list.addAll(posts);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int progress = (int) (((responseCount + 1) / (float) rss.length) * 100);
                            if (progress >= tempProgressCounter) {
                                Log.i(TAG, "Progress : " + progress + "%");
                                ringProgressBar.setProgress(progress);
                                tempProgressCounter = progress;
                            }

                            ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
                                @Override
                                public void progressToComplete() {
                                    Log.i("TAG", "on completed: ");
                                    FilterCategory filterCategory = new FilterCategory(list, getBaseContext());
                                    filterCategory.filter().get(1);
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    });
                }
            });

        }


    }


    private void initFeedList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            Paper.book().write("updatedData", FeedLists.feedsListSetup());   //<---- Setups your feed into database
            // mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }


    }


}