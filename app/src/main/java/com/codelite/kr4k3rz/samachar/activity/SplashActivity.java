package com.codelite.kr4k3rz.samachar.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.BuildConfig;
import com.codelite.kr4k3rz.samachar.MainActivity;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.worker.MyAlarmReceiver;
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
        mHandler = new Handler(Looper.getMainLooper());
        Paper.init(getBaseContext());

        /*initialize the newspapers list by their category*/
        initFeedList();

         /*setup the alarm when to notify the user when the breaking news is popup*/
        setupAlarmNotify();

        /*starts MainActivity class*/
        Log.i("TAG", "" + CheckInternet.isNetworkAvailable(getBaseContext()));
        if (CheckInternet.isNetworkAvailable(getBaseContext()))
            loadFeeds();
        else {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void loadFeeds() {
        final List<Entry> list = new ArrayList<>();
        final OkHttpClient okHttpClient = new OkHttpClient();
        final String[] rss = FeedLists.getFeedListCached(0);
        for (int i = 0; i < rss.length; i++) {
            Request request = new Request.Builder().url(rss[i]).build();
            final int finalI = i;
            final int finalI1 = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    okHttpClient.dispatcher().cancelAll();
                    if (finalI1 == rss.length - 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Parse.filterCategories(list, getBaseContext()).get(1);
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        Log.i("TAG", "" + e);
                    }

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


    private void setupAlarmNotify() {
        boolean checked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("pushNotification", true);
        Log.i("TAG", "checked value : " + checked);

        if (checked) {
            scheduleAlarm();
            Log.i("TAG", "  Alarm scheduled");

        } else {
            Log.i("TAG", "  Alarm scheduled Canceled");
            cancelAlarm();
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


    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
                intent, 0);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HOUR, pIntent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getBaseContext(), MyAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);

    }

}