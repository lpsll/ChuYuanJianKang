package com.htlc.cyjk.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.activity.MeasureActivity;
import com.htlc.cyjk.app.adapter.FirstChildAdapter;
import com.htlc.cyjk.app.bean.FirstChildAdapterBean;
import com.htlc.cyjk.app.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class SecondChildFragment extends BaseFragment implements AdapterView.OnItemClickListener {
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

        //---------------------------------------
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new FirstChildAdapter(mList, getActivity());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        String baseUrl = "file:///android_asset/h5/html/illness/";
        String[] urls = {"yunDong.html","shuiMian.html","yinJiu.html","chouYan.html","yinShi.html"};
        String[] names = CommonUtil.getResourceStringArray(R.array.fragment_second_child_adapter_name);
        int[] imageIds = {R.mipmap.sport,R.mipmap.sleep,R.mipmap.wine,
                R.mipmap.smoke,R.mipmap.eat};
        for(int i=0; i<names.length;i++){
            FirstChildAdapterBean bean = new FirstChildAdapterBean();
            bean.name = names[i];
            bean.imageId = imageIds[i];
            bean.url = baseUrl+urls[i];
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FirstChildAdapterBean bean = (FirstChildAdapterBean) mList.get(position);
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra(MeasureActivity.Url, bean.url);
        intent.putExtra(MeasureActivity.Title, bean.name);
        startActivity(intent);
    }
}
