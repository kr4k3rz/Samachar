package com.codelite.kr4k3rz.samachar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.ui.activity.EditCategory;
import com.codelite.kr4k3rz.samachar.ui.activity.SettingsActivity;
import com.codelite.kr4k3rz.samachar.ui.fragments.ImgVidFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.allnewstab.NewsTabFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.hotnewstab.HotTabFrag;
import com.codelite.kr4k3rz.samachar.worker.MyAlarmReceiver;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setupBottomBar();

        /*setup the alarm when to notify the user when the breaking news is popup*/
        setupAlarmNotify();
    }


    /*
    * setup bottom bar
    * */

    private void setupBottomBar() {

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        /*TODO add number feeds count updated when Broadcaster runs at background*/
        //  final BottomBarTab nearby = bottomBar.getTabWithId(R.id.mynews_item);
//        int newFeeds = Hawk.get("NewFeedsLoaded");
        //      Log.i(MainActivity.class.getSimpleName(), "value : " + newFeeds);
        //    nearby.setBadgeCount(newFeeds);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(@IdRes int tabId) {
                boolean flag = false;

                switch (tabId) {
                    case R.id.home_item:
                        try {
                            fragment = HotTabFrag.class.newInstance();
                            flag = true;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.mynews_item:
                        try {
                            fragment = NewsTabFrag.class.newInstance();
                            //   nearby.removeBadge();

                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(coordinatorLayout, "Favorite Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.edit_category:
                        //  startActivity(new Intent(MainActivity.this, EditCategory.class));
                        try {
                            fragment = ImgVidFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(coordnatorLayout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.more_item:
                        /*TODO make setting Fragment best memory optimized*/
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                }

                if (fragment != null) {
                    if (flag) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).commit();
                    } else
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).addToBackStack(null).commit();
                    fragment = null;
                }

            }
        });


// Remove the badge when you're done with it.
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.more_item)
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                if (tabId == R.id.edit_category)
                    startActivity(new Intent(MainActivity.this, EditCategory.class));
            }
        });
    }

    private void setupAlarmNotify() {
        boolean checked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("pushNotification", true);
        Log.i("TAG", "checked value : " + checked);

        if (checked) {
            Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
                    intent, 0);
            long firstMillis = System.currentTimeMillis();
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis + AlarmManager.INTERVAL_HALF_HOUR,
                    AlarmManager.INTERVAL_HOUR, pIntent);
            Log.i("TAG", "  Alarm scheduled");

        } else {
            Intent intent = new Intent(getBaseContext(), MyAlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
            Log.i("TAG", "  Alarm scheduled Canceled");
        }
    }


}