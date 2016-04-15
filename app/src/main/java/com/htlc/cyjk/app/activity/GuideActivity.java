package com.htlc.cyjk.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;


import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.GuidePagerAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.SharedPreferenceUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/4/8.
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private LinearLayout mLinearPointContainer;
    private ArrayList mBannerList = new ArrayList();
    private GuidePagerAdapter mBannerAdapter;
    private int currentItem;

    private View textButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mLinearPointContainer = (LinearLayout) findViewById(R.id.linear_point_container);
        textButton = findViewById(R.id.text_button);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });

        initData();
        mBannerAdapter = new GuidePagerAdapter(this, mBannerList);
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }


    private void initData() {
        mBannerList.add(R.mipmap.ic_launcher);
        mBannerList.add(R.mipmap.ic_launcher);
        mBannerList.add(R.mipmap.ic_launcher);
        mBannerList.add(R.mipmap.ic_launcher);
        mLinearPointContainer.removeAllViews();
        for (int i = 0; i < mBannerList.size(); i++) {
//            ImageView pointer = (ImageView) View.inflate(this, R.layout.layout_point, null);
//            mLinearPointContainer.addView(pointer);
        }
    }

    private void goMain() {
        SharedPreferenceUtil.putInt(this, SplashActivity.IsFirstStart, 0);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /*
   * view pager listener--------------------
   * */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.i(this, "position=?" + position);
        if (currentItem != position) {
//            mLinearPointContainer.getChildAt(currentItem).setSelected(false);
//            mLinearPointContainer.getChildAt(position).setSelected(true);
            currentItem = position;
        }
        if (position == mBannerList.size() - 1 && textButton.getVisibility() == View.INVISIBLE) {
            textButton.setVisibility(View.VISIBLE);
        } else if (position != mBannerList.size() - 1 && textButton.getVisibility() == View.VISIBLE) {
            textButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
