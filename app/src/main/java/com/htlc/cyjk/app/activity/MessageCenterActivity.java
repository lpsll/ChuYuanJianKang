package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.MessageCenterAdapter;
import com.htlc.cyjk.app.adapter.ThirdAdapter;
import com.htlc.cyjk.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class MessageCenterActivity extends BaseActivity{
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private BaseAdapter mAdapter;
    private ArrayList mList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        setupView();
    }
    private void setupView() {
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        mScrollView.getRefreshableView().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.getRefreshableView().smoothScrollTo(0, 0);
            }
        });
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i(MessageCenterActivity.this, "刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    initData();

                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                    getMoreData();
                }
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MessageCenterAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        for(int i=0; i<9;i++){
            mList.add(true);
        }
        mAdapter.notifyDataSetChanged();
        mScrollView.onRefreshComplete();
    }

    public void getMoreData() {
        mScrollView.onRefreshComplete();
    }
}
