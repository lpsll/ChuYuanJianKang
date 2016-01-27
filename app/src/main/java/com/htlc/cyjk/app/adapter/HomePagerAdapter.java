package com.htlc.cyjk.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.htlc.cyjk.app.fragment.HomeFragment;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<HomeFragment> mPageFragments;

    public HomePagerAdapter(FragmentManager fm, ArrayList<HomeFragment> pageFragments) {
        super(fm);
        this.mPageFragments = pageFragments;
    }

    @Override
    public int getCount() {
        return mPageFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mPageFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageFragments.get(position).mTitle;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
