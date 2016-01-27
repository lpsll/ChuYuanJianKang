package com.htlc.cyjk.app.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.astuetz.PagerSlidingTabStrip;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by sks on 2016/1/19.
 */
public class AutoPagerSlidingTabStrip extends PagerSlidingTabStrip{
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoPagerSlidingTabStrip(Context context) {
        super(context);
    }

    public AutoPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new AutoFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isInEditMode())
        {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
