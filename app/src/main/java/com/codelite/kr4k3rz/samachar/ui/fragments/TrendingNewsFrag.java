package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryEN;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.ui.fragments.trending.BreakingFrag;
import com.codelite.kr4k3rz.samachar.ui.fragments.trending.HomeFrag;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingNewsFrag extends Fragment {


    public TrendingNewsFrag() {
        // Required empty public constructor
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        if (CacheLang.lang().equalsIgnoreCase("NP")) {
            adapter.addFragment(new HomeFrag(), "मुख्य समाचार");
            adapter.addFragment(new BreakingFrag(), WhichCategoryNP.BREAKING.getFirstName());

        } else {
            adapter.addFragment(new HomeFrag(), "Headlines");
            adapter.addFragment(new BreakingFrag(), WhichCategoryEN.BREAKING.getFirstName());

        }
        viewPager.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_up, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_startup);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_startup);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
                    Intent i = new Intent("TAG_REFRESH");
                    lbm.sendBroadcast(i);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }


}
