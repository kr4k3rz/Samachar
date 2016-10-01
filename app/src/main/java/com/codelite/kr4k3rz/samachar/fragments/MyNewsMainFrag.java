package com.codelite.kr4k3rz.samachar.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;

import java.util.ArrayList;
import java.util.List;


public class MyNewsMainFrag extends Fragment {

    Adapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new NationalFrag(), "राष्ट्रिय");
        adapter.addFragment(new NewsPaperFrag(), "पत्रपत्रिका");
        adapter.addFragment(new LocalFrag(), "स्थानिय");
        adapter.addFragment(new OpinionFrag(), "बिचार");
        adapter.addFragment(new BusinessFrag(), "अर्थ");
        adapter.addFragment(new WorldFrag(), "अन्तराष्ट्रिय");
        adapter.addFragment(new EntertainmentFrag(), "मनोरञ्जन");
        adapter.addFragment(new TechnologyFrag(), "प्रविधि");
        adapter.addFragment(new HealthFrag(), "स्वास्थ्य");
        adapter.addFragment(new SportFrag(), "खेल");
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_news, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_mynews);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_mynews);
        tabLayout.setupWithViewPager(viewPager);
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
