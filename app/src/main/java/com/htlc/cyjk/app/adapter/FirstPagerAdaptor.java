package com.htlc.cyjk.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.fragment.BaseFragment;
import com.htlc.cyjk.app.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FirstPagerAdaptor extends FragmentStatePagerAdapter {

    private ArrayList<Class<? extends BaseFragment>> mList;
    public FirstPagerAdaptor(FragmentManager fm, ArrayList<Class<? extends BaseFragment>> list) {
        super(fm);
        mList = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Class<? extends BaseFragment> fragment = mList.get(position);
        try {
            return fragment.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CommonUtil.getResourceStringArray(R.array.fragment_first_children)[position];
    }
}
