package com.codelite.kr4k3rz.samachar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.codelite.kr4k3rz.samachar.ui.activity.SearchActivity;
import com.codelite.kr4k3rz.samachar.ui.fragments.ImgVidFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.NewsTabFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.SettingsFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.TrendingNewsFrag;
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
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name);

        setupBottomBar();
        /*setup the alarm when to notify the user when the breaking news is popup*/
        languageChage();
        setupAlarmNotify();

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

    private void setupBottomBar() {
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(@IdRes int tabId) {
                boolean flag = false;
                switch (tabId) {
                    case R.id.home_item:
                        try {
                            fragment = TrendingNewsFrag.class.newInstance();
                            flag = true;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.categoryAll:
                        try {
                            fragment = NewsTabFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.imgvid:
                        try {
                            fragment = ImgVidFrag.class.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.settings:
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

            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}