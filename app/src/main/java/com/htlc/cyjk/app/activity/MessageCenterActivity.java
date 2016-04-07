package com.htlc.cyjk.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.app.adapter.MessageCenterAdapter;
import com.htlc.cyjk.app.adapter.ThirdAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.MessageBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class MessageCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private BaseAdapter mAdapter;
    private ArrayList mList = new ArrayList();
    private int mPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        String userId = application.getUserBean().userid;
        mPage = 0;
        appAction.messageList(userId, mPage, new ActionCallbackListener<ArrayList<MessageBean>>() {
            @Override
            public void onSuccess(ArrayList<MessageBean> data) {
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
                mPage++;
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mScrollView.onRefreshComplete();
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });

    }

    public void getMoreData() {
        String userId = application.getUserBean().userid;
        appAction.messageList(userId, mPage, new ActionCallbackListener<ArrayList<MessageBean>>() {
            @Override
            public void onSuccess(ArrayList<MessageBean> data) {
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
                mPage++;
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mScrollView.onRefreshComplete();
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String userId = application.getUserBean().userid;
        String token = application.getUserBean().token;
        MessageBean bean = (MessageBean) mList.get(position);
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra(WebActivity.Url, Api.MessageDetail+"?userid="+userId+"&msgid="+bean.id+"&flag="+bean.flag+"&token="+token);
        intent.putExtra(WebActivity.Title, "消息详情");
        startActivity(intent);
    }
}
