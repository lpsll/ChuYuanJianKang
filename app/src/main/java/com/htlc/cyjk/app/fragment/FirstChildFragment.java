package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.FirstChildAdapter;
import com.htlc.cyjk.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FirstChildFragment extends BaseFragment{
    private PullToRefreshScrollView mScrollView;
    private GridView mGridView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_child,null);
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
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i(FirstChildFragment.this, "Home fragment 刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    initData();

                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                    getMoreData();
                }
            }
        });

        //---------------------------------------
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new FirstChildAdapter(mList, getActivity());
        mGridView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        for(int i=0; i<9;i++){
            mList.add(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void getMoreData() {

    }
}
