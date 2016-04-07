package com.htlc.cyjk.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.activity.WebActivity;
import com.htlc.cyjk.app.adapter.ThirdAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.InformationBean;
import com.htlc.cyjk.model.UserBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class ThirdFragment extends HomeFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshScrollView mScrollView;

    private ListView mListView;
    private BaseAdapter mAdapter;
    private ArrayList mList = new ArrayList();
    private int mPage = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third,null);
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
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i(ThirdFragment.this, "Home fragment 刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    initData();

                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                    getMoreData();
                }
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new ThirdAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        String username = baseActivity.application.getUserBean().username;
        mPage = 0;
        baseActivity.appAction.informationList(username, mPage, new ActionCallbackListener<ArrayList<InformationBean>>() {
            @Override
            public void onSuccess(ArrayList<InformationBean> data) {
                mList.clear();
                mList.addAll(data);
                mPage++;
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mScrollView.onRefreshComplete();
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(getActivity(),message);

            }
        });

    }

    public void getMoreData() {
        String username = baseActivity.application.getUserBean().username;
        baseActivity.appAction.informationList(username, mPage, new ActionCallbackListener<ArrayList<InformationBean>>() {
            @Override
            public void onSuccess(ArrayList<InformationBean> data) {
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mScrollView.onRefreshComplete();
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(getActivity(), message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserBean userBean = App.app.getUserBean();
        InformationBean bean = (InformationBean) mList.get(position);
        LogUtil.e(this, "position:" + position);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(WebActivity.Title,bean.title);
        intent.putExtra(WebActivity.Url, Api.InformationDetail+"?id="+bean.id+"&userid="+userBean.userid+"&token="+userBean.token);
        startActivity(intent);
    }
}
