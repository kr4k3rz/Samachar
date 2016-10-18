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
import com.codelite.kr4k3rz.samachar.util.FilterCategoryEN;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
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
    String[] rss_english = {"https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://thehimalayantimes.com/feed/&num=-1",
            "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://english.onlinekhabar.com/feed&num=-1"};

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
        final List<Entry> list_Nepali = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final String[] rss = FeedLists.getFeedListCached(0);

         /*ALWAYS START NEPALI LANG*/
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
                        String feedTitle = feeds.getString("title");
                        String feedLink = feeds.getString("link");
                        Log.i(TAG, "Feedlink : " + feedLink + "\n Feed title : " + feedTitle);
                        JSONArray entries = feeds.getJSONArray("entries");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);


                        list_Nepali.addAll(setTitleLink(feedTitle, feedLink, posts));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressShow(responseCount, rss);
                            if (responseCount == rss.length - 1) {
                                FilterCategoryNP filterCategory = new FilterCategoryNP(list_Nepali, getBaseContext());
                                try {
                                    filterCategory.filter();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
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
        String lang = Paper.book().read("language");
        Log.i(TAG, "lang : " + lang);
        if (lang.equalsIgnoreCase("NP")) {
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
                                    FilterCategoryNP filterCategory = new FilterCategoryNP(list, getBaseContext());
                                    try {
                                        filterCategory.filter();
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
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
                            String feedTitle = feeds.getString("title");
                            String feedLink = feeds.getString("link");
                            Log.i(TAG, "Feedlink : " + feedLink + "\n Feed title : " + feedTitle);
                            JSONArray entries = feeds.getJSONArray("entries");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Entry>>() {
                            }.getType();
                            List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                            list.addAll(setTitleLink(feedTitle, feedLink, posts));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressShow(responseCount, rss);
                                ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
                                    @Override
                                    public void progressToComplete() {
                                        Log.i("TAG", "on completed: ");
                                        FilterCategoryNP filterCategory = new FilterCategoryNP(list, getBaseContext());
                                        try {
                                            filterCategory.filter();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
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
        } else if (lang.equalsIgnoreCase("EN")) {
            final List<Entry> list_English = new ArrayList<>();

            /*ENGLISH*/
            for (String aRss_english : rss_english) {
                int test = 0;
                test++;
                Request request = new Request.Builder().url(aRss_english).build();
                final int failureCount = test;
                final float responseCount = test;


                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("TAG", "" + e);
                        if (failureCount == rss.length - 1) {
                            FilterCategoryEN filterCategory = new FilterCategoryEN(list, getBaseContext());
                            filterCategory.filter();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String jsonStr = response.body().string();
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
                            List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                            list_English.addAll(setTitleLink(feedTitle, feedLink, posts));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressShow(responseCount, rss_english);
                                ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
                                    @Override
                                    public void progressToComplete() {
                                        FilterCategoryEN filterCategoryEN = new FilterCategoryEN(list_English, getBaseContext());
                                        filterCategoryEN.filter();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                });


                            }
                        });
                    }


                });


            }
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

    private void progressShow(float responseCount, String[] rss) {
        int progress = (int) (((responseCount + 1) / (float) rss.length) * 100);
        if (progress >= tempProgressCounter) {
            Log.i(TAG, "Progress : " + progress + "%");
            ringProgressBar.setProgress(progress);
            tempProgressCounter = progress;
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