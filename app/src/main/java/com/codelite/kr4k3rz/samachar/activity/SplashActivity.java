package com.codelite.kr4k3rz.samachar.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.codelite.kr4k3rz.samachar.BuildConfig;
import com.codelite.kr4k3rz.samachar.MainActivity;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
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

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    Log.wtf("Alert", paramThrowable.getMessage(), paramThrowable);
                    System.exit(2); //Prevents the service/app from freezing
                }
            });
        }
        Paper.init(getBaseContext());
        mHandler = new Handler(Looper.getMainLooper());
        initFeedList();
        /*starts MainActivity class*/
        if (CheckInternet.isNetworkAvailable(getBaseContext()))
            loadFeeds();
        else {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /*initialize the newspapers list by their category*/
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
    private void loadFeeds() {
        final List<Entry> list = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Dispatcher dispatcher = okHttpClient.dispatcher();

        final String[] rss = onFirstLoadFeedUrl();
        for (int i = 0; i < rss.length; i++) {
            Request request = new Request.Builder().url(rss[i]).build();
            final int finalI = i;
            final int finalI1 = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TAG", "" + e);
                    dispatcher.cancelAll();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!rss[finalI].equalsIgnoreCase("")) {

                        String jsonStr = response.body().string();    //loads JSON String feeds by Okhttp
                        Log.i("TAG", " : " + jsonStr);
                        JSONObject mainNode = null;
                        try {
                            mainNode = new JSONObject(jsonStr);
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


                        if (finalI1 == rss.length - 1) {
                            Parse.filterCategories(list, getBaseContext()).get(1);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Succeed saved", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                        }
                    }

                }
            });

        }


    }
    private String[] onFirstLoadFeedUrl() {
        String[] test;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime1", false)) {
            Log.i("TAG", "on first time" + FeedLists.getFeedListCached(0).length);
            test = FeedLists.getFeedListCached(0);// mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime1", true);
            editor.apply();
            return test;

        } else {
            Log.i("TAG", "on second time" + FeedLists.getFeedListCached(0).length);
            test = FeedLists.getFeedListLatest(0);
            return test;

        }
    }


}
