package com.htlc.cyjk.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.activity.MessageCenterActivity;
import com.htlc.cyjk.app.activity.PayActivity;
import com.htlc.cyjk.app.activity.PersonActivity;
import com.htlc.cyjk.app.activity.RecommendationActivity;
import com.htlc.cyjk.app.activity.SettingActivity;
import com.htlc.cyjk.app.adapter.FourthAdapter;
import com.htlc.cyjk.app.bean.FourthAdapterBean;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FourthFragment extends HomeFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private int[] itemImageIds;
    private String[] itemNames;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth,null);
        setupView(view);

        return view;
    }

    private void setupView(View view) {
        mScrollView = (PullToRefreshScrollView) view.findViewById(R.id.scrollView);
        mScrollView.getRefreshableView().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.getRefreshableView().smoothScrollTo(0, 0);
            }
        });
        mScrollView.setMode(PullToRefreshBase.Mode.DISABLED);

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new FourthAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        itemImageIds = new int[]{R.mipmap.fragment_fourth_1, R.mipmap.fragment_fourth_2, R.mipmap.fragment_fourth_3, R.mipmap.fragment_fourth_4, R.mipmap.fragment_fourth_5};
        itemNames = CommonUtil.getResourceStringArray(R.array.fragment_fourth_children);
        for(int i=0; i<itemImageIds.length;i++){
            FourthAdapterBean bean = new FourthAdapterBean();
            bean.imageId = itemImageIds[i];
            bean.name = itemNames[i];
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FourthAdapterBean bean = (FourthAdapterBean) mList.get(position);
        LogUtil.e(this, bean.name);
        switch (position){
            case 0:
                Intent intent0 = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(getActivity(), PayActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(getActivity(), RecommendationActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
