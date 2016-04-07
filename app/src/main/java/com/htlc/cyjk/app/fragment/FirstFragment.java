package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.FirstPagerAdaptor;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FirstFragment extends HomeFragment {
    public TextView mTextRight, mTextLeft;

    private PagerSlidingTabStrip mIndicator;
    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mAdapter;
    private ArrayList<Class<? extends BaseFragment>> mList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        mTextLeft = (TextView) view.findViewById(R.id.textLeft);
        mTextRight = (TextView) view.findViewById(R.id.textRight);
        //------------------------------------------
        mIndicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mList.add(FirstChildFragment.class);
        mList.add(SecondChildFragment.class);
        mList.add(ThirdChildFragment.class);
        initTab();
        mAdapter = new FirstPagerAdaptor(getChildFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.setTranslationX(0);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i(FirstFragment.this, "onPageSelected    " + position);
                LinearLayout tabsLinearLayout = (LinearLayout) (mIndicator.getChildAt(0));
                for (int i = 0; i < tabsLinearLayout.getChildCount(); i++) {
                    TextView textView = (TextView) tabsLinearLayout.getChildAt(i);
                    if (i == position) {
                        LogUtil.i("OrdersFragment", "i == position    " + position);
                        textView.setTextColor(getResources().getColor(R.color.text_blue));
                        if(i==2){
                            mTextRight.setVisibility(View.VISIBLE);
                            mTextLeft.setVisibility(View.VISIBLE);
                        }else {
                            mTextRight.setVisibility(View.INVISIBLE);
                            mTextLeft.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        textView.setEnabled(true);
                        textView.setTextColor(getResources().getColor(R.color.text_black));
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        LinearLayout tabsLinearLayout = (LinearLayout) mIndicator.getChildAt(0);
        for (int i = 0; i < tabsLinearLayout.getChildCount(); i++) {
            TextView textView = (TextView) tabsLinearLayout.getChildAt(i);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.text_blue));
            } else {
                textView.setTextColor(getResources().getColor(R.color.text_black));
            }
        }
    }

    public void initTab() {

        //tab 宽度均分
        mIndicator.setShouldExpand(true);

        /**
         * pstsIndicatorColor   滑动条的颜色
         pstsUnderlineColor  滑动条所在的那个全宽线的颜色
         pstsDividerColor  每个标签的分割线的颜色
         pstsIndicatorHeight       滑动条的高度
         pstsUnderlineHeight 滑动条所在的那个全宽线的高度
         pstsDividerPadding 分割线底部和顶部的填充宽度
         pstsTabPaddingLeftRight   每个标签左右填充宽度
         pstsScrollOffset       Scroll offset of the selected tab
         pstsTabBackground   每个标签的背景，应该是一个StateListDrawable
         pstsShouldExpand   如果设置为true，每个标签是相同的控件，均匀平分整个屏幕，默认是false
         pstsTextAllCaps    如果为true，所有标签都是大写字母，默认为true
         */
        mIndicator.setTextSize(CommonUtil.dp2px(getActivity(),14));
//        mIndicator.setTextColor(this.getResources().getColor(R.color.black_blue_color_selector));
//        mIndicator.setTextColorResource(R.color.text_color_red_gray_selector);
        //设置下划线
        mIndicator.setUnderlineColor(this.getResources().getColor(R.color.divider_gray));
        mIndicator.setUnderlineHeight(0);
        //设置滑动指示线
        mIndicator.setIndicatorColor(this.getResources().getColor(R.color.text_blue));
        mIndicator.setIndicatorHeight(0);
        //设置tab间分割线
        mIndicator.setDividerColor(this.getResources().getColor(R.color.divider_gray));
        //设置背景颜色
        mIndicator.setBackgroundColor(this.getResources().getColor(android.R.color.white));
    }
}
