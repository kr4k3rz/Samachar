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
import android.view.Menu;
import android.view.MenuItem;

import com.codelite.kr4k3rz.samachar.ui.activity.SearchActivity;
import com.codelite.kr4k3rz.samachar.ui.activity.SettingsActivity;
import com.codelite.kr4k3rz.samachar.ui.activity.SplashActivity;
import com.codelite.kr4k3rz.samachar.ui.fragments.ImgVidFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.MyListFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.allnewstab.NewsTabFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.hotnewstab.HomeFrag;
import com.codelite.kr4k3rz.samachar.util.CacheLang;
import com.codelite.kr4k3rz.samachar.worker.MyAlarmReceiver;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
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
        //  final BottomBarTab nearby = bottomBar.getTabWithId(R.id.categoryAll);
//        int newFeeds = Hawk.get("NewFeedsLoaded");
        //      Log.i(MainActivity.class.getSimpleName(), "value : " + newFeeds);
        //    nearby.setBadgeCount(newFeeds);
       /* final BottomBarTab breakingNum = bottomBar.getTabWithId(R.id.home_item);
        final int breakingNewNumber = Paper.book().read("BREAKINGNUM");
        breakingNum.setBadgeCount(breakingNewNumber);
        final BottomBarTab categoryAllNum = bottomBar.getTabWithId(R.id.categoryAll);
        int allCategoryNewNum = Paper.book().read("CATEGORYNUM");
        categoryAllNum.setBadgeCount(allCategoryNewNum);
        final BottomBarTab imgVidNum = bottomBar.getTabWithId(R.id.imgvid);
        int imgVidNewNumber = Paper.book().read("IMGVIDNUM");
        imgVidNum.setBadgeCount(imgVidNewNumber);
*/

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(@IdRes int tabId) {
                boolean flag = false;
                switch (tabId) {
                    case R.id.home_item:
                        try {
                            fragment = HomeFrag.class.newInstance();
                            flag = true;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.categoryAll:
                        try {
                            fragment = NewsTabFrag.class.newInstance();
                            //   categoryAllNum.removeBadge();
                            //  breakingNum.removeBadge();

                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(coordinatorLayout, "Favorite Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.imgvid:
                        try {
                            fragment = ImgVidFrag.class.newInstance();
                            //   imgVidNum.removeBadge();
                            // breakingNum.removeBadge();

                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(coordnatorLayout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.mylist:
                        try {
                            fragment = MyListFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            case R.id.action_language:
                if (CacheLang.lang().equalsIgnoreCase("NP")){
                     String language = "EN";
                    Paper.book().write("language", language);
                    startActivity(new Intent(MainActivity.this, SplashActivity.class));
                 }else {
                     String language = "NP";
                     Paper.book().write("language", language);
                     startActivity(new Intent(MainActivity.this, SplashActivity.class));
                 }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}