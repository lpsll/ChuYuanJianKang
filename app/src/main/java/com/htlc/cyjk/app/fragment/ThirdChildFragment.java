package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.ThirdChildAdapter;
import com.htlc.cyjk.app.adapter.ThirdChildFootAdapter;
import com.htlc.cyjk.app.util.DateFormat;
import com.htlc.cyjk.app.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sks on 2016/1/29.
 */
public class ThirdChildFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshScrollView mScrollView;
    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private BaseAdapter mFootAdapter;
    private ListView mFootListView;
    private ArrayList mFootList = new ArrayList();
    private FirstFragment mFirstFragment;
    private ArrayList mDeleteItems = new ArrayList<>();
    private TextView mTextTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_child,null);
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
                LogUtil.i(ThirdChildFragment.this, "Home fragment 刷新。。。。。。");
                if (refreshView.isShownHeader()) {
                    LogUtil.i("refreshView", "pull-to-refresh-------------------------------------------");
                    initData();

                } else if (refreshView.isShownFooter()) {//上拉加载
                    LogUtil.i("refreshView", "pull-to-load-more------------------------------------------");
                    getMoreData();
                }
            }
        });

        mFirstFragment = (FirstFragment) getParentFragment();
        mFirstFragment.mTextLeft.setOnClickListener(this);
        mFirstFragment.mTextRight.setOnClickListener(this);
        Date date = new Date();
        String time = DateFormat.getTime(date);
        mTextTime = (TextView) view.findViewById(R.id.textTime);
        mTextTime.setText(time);
        mTextTime.setOnClickListener(this);
        //---------------------------------------
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new ThirdChildAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mFootListView = (ListView) view.findViewById(R.id.listViewFoot);
        mFootAdapter = new ThirdChildFootAdapter(mFootList, getActivity());
        mFootListView.setAdapter(mFootAdapter);
        initData();
    }

    private void initData() {
        for(int i=0; i<3;i++){
            mList.add("i"+i);
            mFootList.add("i"+i);
        }
        mAdapter.notifyDataSetChanged();
        mFootAdapter.notifyDataSetChanged();
    }

    public void getMoreData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textLeft:
                textLeft();
                break;
            case R.id.textRight:
                textRight();
                break;
            case R.id.textTime:
                textTime();
                break;
        }
    }

    private void textTime() {
        //时间选择器
        TimePickerView timePickerView = new TimePickerView(getActivity(), TimePickerView.Type.ALL);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(true);
        timePickerView.setCancelable(true);
        //时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTextTime.setText(DateFormat.getTime(date));
            }
        });
        timePickerView.show();

    }

    private void textRight() {
        CharSequence rightStr = mFirstFragment.mTextRight.getText();
        if("确定".equals(rightStr)){
            LogUtil.e(this, "删除选中条目");
            LogUtil.e(ThirdChildFragment.this,"确定qian mlist.size="+mList.size());
            ((ThirdChildAdapter)mAdapter).setDeleteState(false);
            mList.removeAll(mDeleteItems);
            mAdapter.notifyDataSetChanged();
            mFirstFragment.mTextLeft.setText("删除");
            mFirstFragment.mTextRight.setText("保存");
            LogUtil.e(ThirdChildFragment.this, "确定hou mlist.size=" + mList.size());
        }else {
            LogUtil.e(this,"保存用药记录");
        }
    }

    private void textLeft() {
        CharSequence leftStr = mFirstFragment.mTextLeft.getText();
        if("删除".equals(leftStr)){
            LogUtil.e(this, "进入删除状态");
            ((ThirdChildAdapter) mAdapter).setDeleteState(true);
            mAdapter.notifyDataSetChanged();
            mFirstFragment.mTextLeft.setText("取消");
            mFirstFragment.mTextRight.setText("确定");
        }else {
            LogUtil.e(this,"取消删除");
            mDeleteItems.clear();
            ((ThirdChildAdapter)mAdapter).setDeleteState(false);
            mAdapter.notifyDataSetChanged();
            mFirstFragment.mTextLeft.setText("删除");
            mFirstFragment.mTextRight.setText("保存");

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mListView){
            if(((ThirdChildAdapter)mAdapter).isDeleteState()){
                LogUtil.e(ThirdChildFragment.this, "delete=" + position);
                ImageView imageSelect = (ImageView) view.findViewById(R.id.imageSelect);
                if(imageSelect.isSelected()){
                    imageSelect.setSelected(false);
                    mDeleteItems.remove(mAdapter.getItem(position));
                }else {
                    imageSelect.setSelected(true);
                    mDeleteItems.add(mAdapter.getItem(position));
                }
            }
        }else {

        }
    }
}
