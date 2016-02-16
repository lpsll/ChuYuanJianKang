package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.FourthAdapter;
import com.htlc.cyjk.app.adapter.LengthAdapter;
import com.htlc.cyjk.app.adapter.PayAdapter;
import com.htlc.cyjk.app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/2/15.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshScrollView mScrollView;

    private ListView mLengthListView, mPayListView;
    private ArrayList mLengthList = new ArrayList();
    private ArrayList mPayList = new ArrayList();
    private BaseAdapter mLengthAdapter,mPayAdapter;
    private TextView mTextButton;
    private int[] itemImageIds;
    private String[] itemNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
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
        mScrollView.setMode(PullToRefreshBase.Mode.DISABLED);

        mLengthListView = (ListView) findViewById(R.id.lengthListView);
        mPayListView = (ListView) findViewById(R.id.payListView);
        mTextButton = (TextView) findViewById(R.id.textButton);

        mTextButton.setOnClickListener(this);
        mLengthAdapter = new LengthAdapter(mLengthList, this);
        mLengthListView.setAdapter(mLengthAdapter);
        mLengthListView.setItemChecked(0, true);
        mPayAdapter = new PayAdapter(mPayList, this);
        mPayListView.setAdapter(mPayAdapter);
        mPayListView.setItemChecked(0,true);


        initData();

    }

    private void initData() {
        for(int i=0; i<4; i++){
            mLengthList.add(true);
            mPayList.add(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                commitPayMethodAndLength();
                break;
        }
    }

    private void commitPayMethodAndLength() {
        int lengthPosition = mLengthListView.getCheckedItemPosition();
        int payPosition = mPayListView.getCheckedItemPosition();
        LogUtil.e(this,"lengthPosition:"+lengthPosition+";payPosition:"+payPosition);
    }
}
