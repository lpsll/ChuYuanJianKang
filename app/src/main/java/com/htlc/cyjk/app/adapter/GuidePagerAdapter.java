package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htlc.cyjk.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by sks on 2015/12/29.
 */
public class GuidePagerAdapter extends PagerAdapter {
    private Activity mActivity;
    private ArrayList mList;

    public GuidePagerAdapter(Activity activity, ArrayList list) {
        this.mActivity = activity;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = (ImageView) View.inflate(mActivity, R.layout.adapter_guide_pager, null);
        Object imageId = mList.get(position);
        if(imageId instanceof Integer){
            view.setImageResource((int)imageId);
        }else {
            ImageLoader.getInstance().displayImage((String)imageId,view);
        }
        container.addView(view);
        return view;
    }
}
