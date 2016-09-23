package com.codelite.kr4k3rz.samachar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codelite.kr4k3rz.samachar.fragments.BusinessFrag;
import com.codelite.kr4k3rz.samachar.fragments.EntertainmentFrag;
import com.codelite.kr4k3rz.samachar.fragments.HeadlinesFrag;
import com.codelite.kr4k3rz.samachar.fragments.HealthFrag;
import com.codelite.kr4k3rz.samachar.fragments.TechnologyFrag;
import com.codelite.kr4k3rz.samachar.fragments.WorldFrag;
import com.google.firebase.crash.FirebaseCrash;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Fragment fragment = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.flContent, new HeadlinesFrag()).commit();
        }

        FirebaseCrash.log("Activity created");

    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                try {

                    selectDrawerItem(menuItem);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }


    private void selectDrawerItem(final MenuItem menuItem) throws IllegalAccessException, InstantiationException {
        switch (menuItem.getItemId()) {
            case R.id.headlines_frag:
                fragment = HeadlinesFrag.class.newInstance();
                break;
            case R.id.world_frag:
                fragment = WorldFrag.class.newInstance();
                break;
            case R.id.business_frag:
                fragment = BusinessFrag.class.newInstance();
                break;
            case R.id.technology_frag:
                fragment = TechnologyFrag.class.newInstance();
                break;
            case R.id.entertainment_frag:
                fragment = EntertainmentFrag.class.newInstance();
                break;
            case R.id.health_frag:
                fragment = HealthFrag.class.newInstance();
                break;
            case R.id.setting:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(i);

                    }
                }, 200);
                break;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
                    fragment = null;
                    menuItem.setChecked(true);
                    setTitle(menuItem.getTitle());
                }

            }
        }, 200);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


}
