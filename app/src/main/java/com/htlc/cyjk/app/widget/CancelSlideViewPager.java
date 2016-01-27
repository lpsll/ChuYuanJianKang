package com.htlc.cyjk.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 取消滑动的ViewPager
 */
public class CancelSlideViewPager extends ViewPager {
    public CancelSlideViewPager(Context context) {
        super(context);
    }

    public CancelSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false ;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

}
