package com.htlc.cyjk.app.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;

/**
 * Created by sks on 2015/12/29.
 */
public class HomeFragment extends BaseFragment{
    public String mTitle;
    public int mIconResId;

    public static <T extends HomeFragment> T newInstance(Class<T> clazz, String title, int iconResId) {
        try {
            T fragment = clazz.newInstance();
            fragment.mTitle = title;
            fragment.mIconResId = iconResId;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public View getTabView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_fragment, null);
        ImageView ivTabIcon = (ImageView) view.findViewById(R.id.image_tab_icon);
        TextView tvTabTitle = (TextView) view.findViewById(R.id.text_tab_title);
        tvTabTitle.setText(mTitle);
        ivTabIcon.setBackgroundResource(mIconResId);
        return view;
    }
}
