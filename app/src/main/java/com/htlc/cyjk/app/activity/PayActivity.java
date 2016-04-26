package com.htlc.cyjk.app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.adapter.FourthAdapter;
import com.htlc.cyjk.app.adapter.LengthAdapter;
import com.htlc.cyjk.app.adapter.PayAdapter;
import com.htlc.cyjk.app.bean.PaymentBean;
import com.htlc.cyjk.app.event.SelectContactEvent;
import com.htlc.cyjk.app.fragment.SelectContactFragment;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ChargeBean;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.PriceBean;
import com.pingplusplus.android.PaymentActivity;

import java.nio.charset.Charset;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/2/15.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshScrollView mScrollView;
    private static final int REQUEST_CODE_PAYMENT = 100;
    private ListView mLengthListView, mPayListView;
    private ArrayList<PriceBean> mLengthList = new ArrayList();
    private ArrayList mPayList = new ArrayList();
    private BaseAdapter mLengthAdapter, mPayAdapter;
    private TextView mTextButton;
    private int[] itemImageIds;
    private String[] itemNames;
    private String[] itemPayIds;
    private TextView mTextDoctor;
    private ContactBean mDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_pay);
        setupView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void onEventMainThread(SelectContactEvent event) {
        mDoctor = event.bean;
        mTextDoctor.setText(mDoctor.name);
        getPriceList();
    }

    private void getPriceList() {
        appAction.getPriceList(mDoctor.userid, new ActionCallbackListener<ArrayList<PriceBean>>() {
            @Override
            public void onSuccess(ArrayList<PriceBean> data) {
                for (PriceBean bean : data) {
                    if ("1".equals(bean.duration)) {
                        mLengthList.get(0).price = bean.price;
                        mLengthList.get(0).id = bean.id;
                    } else if ("3".equals(bean.duration)) {
                        mLengthList.get(1).price = bean.price;
                        mLengthList.get(1).id = bean.id;
                    } else if ("6".equals(bean.duration)) {
                        mLengthList.get(2).price = bean.price;
                        mLengthList.get(2).id = bean.id;
                    } else if ("12".equals(bean.duration)) {
                        mLengthList.get(3).price = bean.price;
                        mLengthList.get(3).id = bean.id;
                    }
                }
                mLengthAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
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
        mScrollView.setMode(PullToRefreshBase.Mode.DISABLED);

        mTextDoctor = (TextView) findViewById(R.id.textDoctor);
        mLengthListView = (ListView) findViewById(R.id.lengthListView);
        mPayListView = (ListView) findViewById(R.id.payListView);
        mTextButton = (TextView) findViewById(R.id.textButton);

        mTextDoctor.setOnClickListener(this);
        mTextButton.setOnClickListener(this);
        mLengthAdapter = new LengthAdapter(mLengthList, this);
        mLengthListView.setAdapter(mLengthAdapter);
        mLengthListView.setItemChecked(0, true);
        mPayAdapter = new PayAdapter(mPayList, this);
        mPayListView.setAdapter(mPayAdapter);
        mPayListView.setItemChecked(0, true);

        initData();

    }

    private void initData() {
        itemImageIds = new int[]{R.mipmap.logo_wx_pay, R.mipmap.logo_union_pay, R.mipmap.logo_ali_pay};
        itemNames = CommonUtil.getResourceStringArray(R.array.activity_pay_names);
        itemPayIds = new String[]{"3", "2", "1"};
        for (int i = 0; i < 3; i++) {
            PaymentBean bean = new PaymentBean();
            bean.logoId = itemImageIds[i];
            bean.payName = itemNames[i];
            bean.payId = itemPayIds[i];
            mPayList.add(bean);
        }
        mPayAdapter.notifyDataSetChanged();

        PriceBean priceBean0 = new PriceBean();
        priceBean0.duration = "1";
        priceBean0.durationDes = "一个月";
        priceBean0.price = "0";
        mLengthList.add(priceBean0);
        PriceBean priceBean1 = new PriceBean();
        priceBean1.duration = "3";
        priceBean1.durationDes = "三个月";
        priceBean1.price = "0";
        mLengthList.add(priceBean1);
        PriceBean priceBean2 = new PriceBean();
        priceBean2.duration = "6";
        priceBean2.durationDes = "半年";
        priceBean2.price = "0";
        mLengthList.add(priceBean2);
        PriceBean priceBean3 = new PriceBean();
        priceBean3.duration = "12";
        priceBean3.durationDes = "一年";
        priceBean3.price = "0";
        mLengthList.add(priceBean3);

        mLengthAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textDoctor:
                selectDoctor();
                break;
            case R.id.textButton:
                commitPayMethodAndLength();
                break;
        }
    }

    private void selectDoctor() {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivity(intent);
    }

    private void commitPayMethodAndLength() {
        int lengthPosition = mLengthListView.getCheckedItemPosition();
        int payPosition = mPayListView.getCheckedItemPosition();
        LogUtil.e(this, "lengthPosition:" + lengthPosition + ";payPosition:" + payPosition);
        if (mDoctor == null) {
            ToastUtil.showToast(App.app, "请选择收费医生");
            return;
        }
        PriceBean priceBean = mLengthList.get(lengthPosition);
        PaymentBean paymentBean = (PaymentBean) mPayList.get(payPosition);
        appAction.payToDoctor(mDoctor.userid, priceBean.id, paymentBean.payId, new ActionCallbackListener<ChargeBean>() {
            @Override
            public void onSuccess(ChargeBean data) {
                pay(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(this,"commitPayMethodAndLength:  "+message);
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void pay(ChargeBean chargeBean) {
        byte[] decode = Base64.decode(chargeBean.charge, Base64.DEFAULT);
        LogUtil.e(this, "解码前：" + chargeBean.charge);
        String charge = new String(decode, Charset.forName("UTF-8"));
        LogUtil.e(this, "解码后：" + charge);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - 支付成功
             * "fail"    - 支付失败
             * "cancel"  - 取消支付
             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
             */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                LogUtil.e(this, "error:" + errorMsg + ";msg:" + extraMsg);
                payResult(result);
            }
        }
    }

    private void payResult(String result) {
        String tips;
        if ("success".equals(result)) {
            tips = "缴费成功";
        } else if ("fail".equals(result)) {
            tips = "支付失败";
        } else if ("cancel".equals(result)) {
            tips = "支付取消";
        } else {
            tips = "交易出错";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage(tips);//设置显示的内容
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }
}
