package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.app.widget.OrderItem;
import com.htlc.cyjk.core.ActionCallbackListener;

/**
 * Created by sks on 2016/2/15.
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextButton, mTextButtonSave;
    private LinearLayout mLinearContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLinearContainer = (LinearLayout) findViewById(R.id.linearContainer);
        mTextButton = (TextView) findViewById(R.id.textButton);
        mTextButtonSave = (TextView) findViewById(R.id.textRight);
        mTextButton.setOnClickListener(this);
        mTextButtonSave.setOnClickListener(this);

        addItem();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                addItem();
                break;
            case R.id.textRight:
                save();
                break;
        }
    }

    private void addItem() {
        final OrderItem orderItem = (OrderItem) View.inflate(this,R.layout.layout_order_item_view,null);
        orderItem.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearContainer.removeView(orderItem);
            }
        });
        mLinearContainer.addView(orderItem);
    }

    private void save() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for(int i=0; i<mLinearContainer.getChildCount(); i++){
            OrderItem orderItem = (OrderItem) mLinearContainer.getChildAt(i);
            stringBuilder.append("{\"drugName\": \"");
            String name = orderItem.editName.getText().toString().trim();
            if(TextUtils.isEmpty(name)){
                ToastUtil.showToast(App.app,"信息不能为空");
                return;
            }
            stringBuilder.append(name);
            stringBuilder.append("\",\"num\": \"");
            String number = orderItem.editNumber.getText().toString().trim();
            if(TextUtils.isEmpty(number)){
                ToastUtil.showToast(App.app,"信息不能为空");
                return;
            }
            stringBuilder.append(number);
            stringBuilder.append("\", \"price\": \"");
            String price = orderItem.editPrice.getText().toString().trim();
            if(TextUtils.isEmpty(price)){
                ToastUtil.showToast(App.app,"信息不能为空");
                return;
            }
            stringBuilder.append(price);
            stringBuilder.append("\",\"unit\":\"");
            String unit = orderItem.editUnit.getText().toString().trim();
            if(TextUtils.isEmpty(unit)){
                ToastUtil.showToast(App.app,"信息不能为空");
                return;
            }
            stringBuilder.append(unit);
            stringBuilder.append("\"},");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("]");
        String drugJson = stringBuilder.toString();
        appAction.createOrder(drugJson, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"提交成功！");
                finish();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

}
