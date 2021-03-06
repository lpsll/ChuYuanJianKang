package com.htlc.cyjk.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.adapter.LengthAdapter;
import com.htlc.cyjk.app.adapter.PayAdapter;
import com.htlc.cyjk.app.bean.PaymentBean;
import com.htlc.cyjk.app.event.SelectContactEvent;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ChargeBean;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.PriceBean;
import com.htlc.cyjk.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import pay.AliPayUtil;
import pay.PayResult;

/**
 * Created by sks on 2016/2/15.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshScrollView mScrollView;
    private static final int ALI_SDK_PAY_FLAG = 1000;//支付宝
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
    private int payPosition;
    private int lengthPosition;
    private ProgressDialog payProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_pay);
        setupView();
        registerWXBroadcastReceiver();
    }
    private void registerWXBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getApplication().getPackageName() + ".action.WX_PAY");
        registerReceiver(wxPayBroadcastReceiver, filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (payProgressDialog != null) {
            payProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        unregisterReceiver(wxPayBroadcastReceiver);
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
        lengthPosition = mLengthListView.getCheckedItemPosition();
        payPosition = mPayListView.getCheckedItemPosition();
        LogUtil.e(this, "lengthPosition:" + lengthPosition + ";payPosition:" + payPosition);
        if (mDoctor == null) {
            ToastUtil.showToast(App.app, "请选择收费医生");
            return;
        }
        PriceBean priceBean = mLengthList.get(lengthPosition);
        PaymentBean paymentBean = (PaymentBean) mPayList.get(payPosition);
        payProgressDialog = ProgressDialog.show(this, "", "请稍等...", true);
        appAction.payToDoctor(mDoctor.userid, priceBean.id, paymentBean.payId, new ActionCallbackListener<ChargeBean>() {
            @Override
            public void onSuccess(ChargeBean data) {
                pay(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (handleNetworkOnFailure(errorEvent, message)) return;
                showTipsDialog(message, false);
            }
        });
    }

    private void pay(ChargeBean chargeBean) {
//        byte[] decode = Base64.decode(chargeBean.charge, Base64.DEFAULT);
//        LogUtil.e(this, "解码前：" + chargeBean.charge);
//        String charge = new String(decode, Charset.forName("UTF-8"));
//        LogUtil.e(this, "解码后：" + charge);
        String charge = chargeBean.charge;
        if(payPosition == 0){
            wxPay(charge);
        }else if(payPosition == 1){
            if(payProgressDialog!=null){
                payProgressDialog.dismiss();
            }
            unionPay(charge);
        }else if(payPosition == 2){
            if(payProgressDialog!=null){
                payProgressDialog.dismiss();
            }
            aliPay(charge);
        }

    }

    /**
     * 支付宝支付
     */
    private void aliPay(String charge) {
        double aliPrice = Double.parseDouble(mLengthList.get(lengthPosition).price);
//        aliPrice = 0.01;
        final String payInfo = AliPayUtil.getPayInfo("华医医生会员", "华医医生会员费用", aliPrice + "", charge);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = ALI_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * 银联
     */
    private void unionPay(String charge) {

    }
    /**
     * 微信
     */
    private void wxPay(String charge) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        boolean b = msgApi.registerApp(Constant.WX_APP_ID);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        try {
            JSONObject json = new JSONObject(charge);
            PayReq req = new PayReq();
            req.appId = Constant.WX_APP_ID;  // 测试用appId
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
//            String sign = MD5Util.MD5("appid=" + req.appId
//                    + "&noncestr=" + req.nonceStr + "&package="
//                    + req.packageValue + "&partnerid=" + req.partnerId
//                    + "&prepayid=" + req.prepayId + "&timestamp="
//                    + req.timeStamp + "&key=" + Constant.WX_APP_KEY).toUpperCase();
//            req.sign = sign;
            req.sign = json.getString("sign");
            //req.extData = "app data";
            wxapi.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 处理支付宝支付结果
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALI_SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showTipsDialog("支付成功！", true);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showTipsDialog("支付结果确认中！", true);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showTipsDialog("支付失败！", false);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 显示支付结果
     *
     * @param msg
     */
    private void showTipsDialog(String msg, final boolean paySuccess) {
        if (payProgressDialog != null) {
            payProgressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果");
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (paySuccess) {
                    startActivity(new Intent(PayActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.bg_blue));

    }
    /**
     *
     * 处理微信支付结果
     */
    private BroadcastReceiver wxPayBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(WXPayEntryActivity.TAG, 1);
            switch (code) {
                case 0://支付成功后的界面
                    showTipsDialog("支付成功！",true);
                    break;
                case -1:
                    //支付错误
                    showTipsDialog("支付失败！",false);
                    break;
                case -2://用户取消支付后的界面
                    showTipsDialog("支付取消！",false);
                    break;
                default:
                    //支付错误
                    showTipsDialog("支付失败！",false);
                    break;
            }
        }
    };
}
