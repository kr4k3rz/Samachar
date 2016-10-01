package com.codelite.kr4k3rz.samachar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.codelite.kr4k3rz.samachar.activity.EditCategory;
import com.codelite.kr4k3rz.samachar.activity.SettingsActivity;
import com.codelite.kr4k3rz.samachar.fragments.HomeMainFrag;
import com.codelite.kr4k3rz.samachar.fragments.MyNewsMainFrag;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);
        setupBottomBar();
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
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.home_item:
                        try {
                            fragment = HomeMainFrag.class.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.mynews_item:
                        try {
                            fragment = MyNewsMainFrag.class.newInstance();
                            //   nearby.removeBadge();

                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(coordinatorLayout, "Favorite Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.edit_category:
                        startActivity(new Intent(MainActivity.this, EditCategory.class));
                        //Snackbar.make(coordnatorLayout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.more_item:
                        /*TODO make setting Fragment best memory optimized*/
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).commit();
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


}
