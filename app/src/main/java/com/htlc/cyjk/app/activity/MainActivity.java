package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.HomePagerAdapter;
import com.htlc.cyjk.app.fragment.FirstFragment;
import com.htlc.cyjk.app.fragment.HomeFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.fragment_first), R.drawable.tab_first_selector));
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.fragment_second),  R.drawable.tab_second_selector));
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.fragment_third),  R.drawable.tab_third_selector));
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.fragment_fourth),  R.drawable.tab_fourth_selector));

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setTag(pageFragments.get(i));
                tab.setCustomView(pageFragments.get(i).getTabView(this));
            }
        }
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
