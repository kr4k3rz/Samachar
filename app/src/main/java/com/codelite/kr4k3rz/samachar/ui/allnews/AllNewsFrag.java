package com.codelite.kr4k3rz.samachar.ui.allnews;

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
import com.codelite.kr4k3rz.samachar.model.WhichCategoryEN;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.ArrayList;
import java.util.List;


public class AllNewsFrag extends Fragment {

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        if (CacheLang.lang().equalsIgnoreCase("NP")) {
            adapter.addFragment(new NationalFrag(), WhichCategoryNP.NATIONAL.getFirstName());
            adapter.addFragment(new BusinessFrag(), WhichCategoryNP.BUSINESS.getFirstName());
            adapter.addFragment(new WorldFrag(), WhichCategoryNP.WORLD.getFirstName());
            adapter.addFragment(new EntertainmentFrag(), WhichCategoryNP.ENTERTAINMENT.getFirstName());
            adapter.addFragment(new TechnologyFrag(), WhichCategoryNP.TECHNOLOGY.getFirstName());
            adapter.addFragment(new HealthFrag(), WhichCategoryNP.HEALTH.getFirstName());
            adapter.addFragment(new SportFrag(), WhichCategoryNP.SPORT.getFirstName());
        } else {
            adapter.addFragment(new NationalFrag(), WhichCategoryEN.NATIONAL.getFirstName());
            adapter.addFragment(new BusinessFrag(), WhichCategoryEN.BUSINESS.getFirstName());
            adapter.addFragment(new WorldFrag(), WhichCategoryEN.WORLD.getFirstName());
            adapter.addFragment(new EntertainmentFrag(), WhichCategoryEN.ENTERTAINMENT.getFirstName());
            adapter.addFragment(new TechnologyFrag(), WhichCategoryEN.TECHNOLOGY.getFirstName());
            adapter.addFragment(new HealthFrag(), WhichCategoryEN.HEALTH.getFirstName());
            adapter.addFragment(new SportFrag(), WhichCategoryEN.SPORT.getFirstName());
        }

        viewPager.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmnet_all_news, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_mynews);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_mynews);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private static class Adapter extends FragmentPagerAdapter {
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
