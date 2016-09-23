package com.codelite.kr4k3rz.samachar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.codelite.kr4k3rz.samachar.util.FeedLists;
import com.orhanobut.hawk.Hawk;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hawk.init(getBaseContext()).build();
        initFeedList();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        boolean checked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("pushNotification", true);
        if (checked)
            scheduleAlarm();
        else cancelAlarm();

        finish();
    }


    private void initFeedList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            Hawk.put("updatedData", FeedLists.feedsListSetup());   //<---- Setups your feed into database
            // mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }


    }


    // Setup a recurring alarm every half hour
    private void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis + 1000 * 60,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getBaseContext(), MyAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);

    }

}
