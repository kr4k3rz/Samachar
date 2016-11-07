package com.codelite.kr4k3rz.samachar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.codelite.kr4k3rz.samachar.ui.fragments.MyFeedsFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.NewsTabFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.SettingsFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.TrendingNewsFrag;
import com.codelite.kr4k3rz.samachar.worker.MyAlarmReceiver;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name);
        setupBottomBar();
        languageChage();
        setupAlarmNotify();

    }

    private void setupBottomBar() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.icon_whatshot, R.color.accent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_view_carousel, R.color.accent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.icon_rss_feed, R.color.accent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.icon_setting, R.color.accent);


        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        // Set background color
        //   bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        //    bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        //  bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        // bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);
// Or force the titles to be hidden (against Material Design guidelines, too!)
        bottomNavigation.setForceTitlesHide(true);


        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically

// Customize notification (title, background, typeface)
        //  bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item
//        bottomNavigation.setNotification("1", 3);
// OR
       /* AHNotification notification = new AHNotification.Builder()
                .setText("1")
                .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
                .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
                .build();*/
        // bottomNavigation.setNotification(notification, 1);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                boolean flag = false;
                switch (position) {
                    case 0:
                        try {
                            fragment = TrendingNewsFrag.class.newInstance();
                            flag = true;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            fragment = NewsTabFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            fragment = MyFeedsFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }


                        break;
                    case 3:
                        try {
                            fragment = SettingsFrag.class.newInstance();
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
                return true;
            }
        });
        bottomNavigation.setCurrentItem(0);
    }

    private void languageChage() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("FLAG")) {
            // ToastMsg.shortMsg(getBaseContext(), "" + flag);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v = View.inflate(getBaseContext(), R.layout.dialog_language, null);
            Button btn_english = (Button) v.findViewById(R.id.btn_english);
            btn_english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  ToastMsg.shortMsg(getBaseContext(), "Message");
                    String language = "EN";
                    Paper.book().write("language", language);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            Button btn_nepali = (Button) v.findViewById(R.id.btn_nepali);
            btn_nepali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String language = "NP";
                    Paper.book().write("language", language);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            builder.setView(v);
            builder.show();


        }
    }


    private void setupAlarmNotify() {
        boolean checked = true;
        if (Paper.book().exist("notifications")) {
            checked = Paper.book().read("notifications");
            Log.i("TAG", "" + checked);
        }

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