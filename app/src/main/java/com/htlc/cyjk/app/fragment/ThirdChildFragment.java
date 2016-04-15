package com.htlc.cyjk.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.activity.DrugsActivity;
import com.htlc.cyjk.app.activity.MeasureActivity;
import com.htlc.cyjk.app.adapter.ThirdChildAdapter;
import com.htlc.cyjk.app.adapter.ThirdChildFootAdapter;
import com.htlc.cyjk.app.util.DateFormat;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.DrugBean;
import com.htlc.cyjk.model.MedicalHistoryItemBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

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
    private TextView mTextTime, mTextButton, mTextButtonHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void onEventMainThread(DrugBean event) {
        String msg = "onEventMainThread收到了消息：";
        for (int i = 0; i < mList.size(); i++) {
            DrugBean bean = (DrugBean) mList.get(i);
            if (event.id.equals(bean.id)) {
                return;
            }
        }
        mList.add(event);
        mAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_child, null);
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

        mFirstFragment = (FirstFragment) getParentFragment();
        mFirstFragment.mTextLeft.setOnClickListener(this);
        mFirstFragment.mTextRight.setOnClickListener(this);
        Date date = new Date();
        String time = DateFormat.getTime(date);
        mTextTime = (TextView) view.findViewById(R.id.textTime);
        mTextTime.setText(time);
        mTextTime.setOnClickListener(this);
        mTextButton = (TextView) view.findViewById(R.id.textButton);
        mTextButton.setOnClickListener(this);
        mTextButtonHistory = (TextView) view.findViewById(R.id.textButtonHistory);
        mTextButtonHistory.setOnClickListener(this);
        //---------------------------------------
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new ThirdChildAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);

        mFootListView = (ListView) view.findViewById(R.id.listViewFoot);
        mFootAdapter = new ThirdChildFootAdapter(mFootList, getActivity());
        mFootListView.setAdapter(mFootAdapter);
        mFootListView.setOnItemClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Date date = new Date();
        String time = DateFormat.getTime(date);
        initData();
    }

    private void initData() {
        String userId = baseActivity.application.getUserBean().userid;
        baseActivity.appAction.medicineHistory(userId, new ActionCallbackListener<ArrayList<MedicalHistoryItemBean>>() {
            @Override
            public void onSuccess(ArrayList<MedicalHistoryItemBean> data) {
                mFootList.clear();
                mFootList.addAll(data);
                mFootAdapter.notifyDataSetChanged();
                mTextButtonHistory.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(ThirdChildFragment.this, message);
                mFootList.clear();
                mFootAdapter.notifyDataSetChanged();
                mTextButtonHistory.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textLeft:
                break;
            case R.id.textRight:
                textRight();
                break;
            case R.id.textTime:
                textTime();
                break;
            case R.id.textButton:
                addDrug();
                break;
            case R.id.textButtonHistory:
                showHistory();
                break;

        }
    }

    /**
     * 查看用药历史记录
     */
    private void showHistory() {
        String url = "file:///android_asset/h5/html/doctor/yaopinInfo.html";
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra(MeasureActivity.Url, url);
        intent.putExtra(MeasureActivity.Title, "用药记录");
        startActivity(intent);
    }

    /**
     * 添加药品
     */
    private void addDrug() {
        Intent intent = new Intent(getActivity(), DrugsActivity.class);
        startActivity(intent);
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

        postDrugs();
    }

    /**
     * 提交用药记录
     */
    private void postDrugs() {
        mFirstFragment.mTextRight.requestFocus();
        String userId = baseActivity.application.getUserBean().userid;
        String date = mTextTime.getText().toString();
        if (mList.size() > 0) {
            String drugsJson = new Gson().toJson(mList);
            LogUtil.e(this, drugsJson);
            drugsJson = Base64.encodeToString(drugsJson.getBytes(), Base64.DEFAULT);
            LogUtil.e(this, drugsJson);
            LogUtil.e(this, "base64decode:" + Base64.decode(drugsJson, Base64.DEFAULT));

            baseActivity.appAction.postDrugs(userId, date, drugsJson, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(getActivity(), "保存成功！");
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    initData();
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if (handleNetworkOnFailure(errorEvent, message)) return;
                    ToastUtil.showToast(getActivity(), message);
                }
            });
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {

        } else {
            MedicalHistoryItemBean bean = (MedicalHistoryItemBean) mFootList.get(position);
            mList.clear();
            mList.addAll(bean.drug);
            mAdapter.notifyDataSetChanged();
        }
    }
}
