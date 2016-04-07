package com.htlc.cyjk.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.DrugsAdapter;
import com.htlc.cyjk.app.adapter.SecondAdapter;
import com.htlc.cyjk.app.fragment.ContactFragment;
import com.htlc.cyjk.app.fragment.ThirdChildFragment;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.app.widget.SideBar;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.DrugBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/2/15.
 */
public class DrugsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private View mImageBack;

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private ArrayList drugs = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private EditText mEditSearch;
    private SideBar mSideBar;
    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs);
        setupView();
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new DrugsAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mEditSearch = (EditText) findViewById(R.id.editSearch);
        mEditSearch.setFocusable(true);
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        mTextDialog = (TextView) findViewById(R.id.textDialog);
        mSideBar = (SideBar) findViewById(R.id.sideBar);
        mSideBar.setTextView(mTextDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = ((DrugsAdapter) mAdapter).getFirstPositionOfType(s);
                LogUtil.e(DrugsActivity.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.setSelection(position + 1);
                }
            }
        });
        initData();
    }

    private void search(String search) {
        if(TextUtils.isEmpty(search)){
            if(drugs.size()>0){
                mList.clear();
                mList.addAll(drugs);
                mAdapter.notifyDataSetChanged();
            }
        }else {
            mList.clear();
            for(int i=0;i<drugs.size();i++){
                DrugBean bean = (DrugBean) drugs.get(i);
                if(bean.medicine.contains(search)){
                    mList.add(bean);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

//        LogUtil.e(this,"search:"+search);
//        if(TextUtils.isEmpty(search)){
//            if(drugs.size()>0){
//                mList.clear();
//                mList.addAll(drugs);
//                mAdapter.notifyDataSetChanged();
//            }
//        }else {
//            appAction.drugsList(search, new ActionCallbackListener<ArrayList<DrugBean>>() {
//                @Override
//                public void onSuccess(ArrayList<DrugBean> data) {
//                    mList.clear();
//                    mList.addAll(data);
//                    mAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onFailure(String errorEvent, String message) {
//                    if(handleNetworkOnFailure(errorEvent, message)) return;
//                    ToastUtil.showToast(DrugsActivity.this, message);
//                }
//            });
//        }
    }

    private void initData() {
        LogUtil.e(this,"InitData");
        appAction.drugsList("", new ActionCallbackListener<ArrayList<DrugBean>>() {
            @Override
            public void onSuccess(ArrayList<DrugBean> data) {
                LogUtil.e(DrugsActivity.this, data.size() + "数据长度");
                drugs.addAll(data);
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(DrugsActivity.this, message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(canClick){
            canClick = !canClick;
            DrugBean bean = (DrugBean) mList.get(position);
            EventBus.getDefault().post(bean);
            finish();
        }


    }
}
