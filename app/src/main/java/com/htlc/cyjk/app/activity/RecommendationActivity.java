package com.htlc.cyjk.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ContactBean;

import de.greenrobot.event.EventBus;

/**
 * Created by sks on 2016/2/15.
 */
public class RecommendationActivity extends BaseActivity implements View.OnClickListener {
    public static final String IsLoginBind = "IsLoginBind";
    private boolean isLoginBind = false;

    private TextView mTextButton;
    private EditText mEditBindNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoginBind = getIntent().getBooleanExtra(IsLoginBind,false);
        setContentView(R.layout.activity_recommendation);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditBindNumber = (EditText) findViewById(R.id.editBindNumber);
        mTextButton = (TextView) findViewById(R.id.textButton);
        if(isLoginBind){
            mTextButton.setText("下一步");
            mEditBindNumber.setHint("请输入推荐码(选填)");
        }
        mTextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                bindDoctor();
                break;
        }
    }

    private void bindDoctor() {
        String bindNumber = mEditBindNumber.getText().toString().trim();
        String userId = application.getUserBean().userid;
        if(isLoginBind){
            if(TextUtils.isEmpty(bindNumber)){
                goPerfectInfoActivity();
                return;
            }
            //bind
            appAction.bindDoctor(userId, bindNumber, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    goPerfectInfoActivity();
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if(handleNetworkOnFailure(errorEvent, message)) return;
                    ToastUtil.showToast(RecommendationActivity.this,message);
                }
            });
        }else {
            if(TextUtils.isEmpty(bindNumber)){
                mEditBindNumber.setText("");
                return;
            }
            //bind
            appAction.bindDoctor(userId, bindNumber, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    EventBus.getDefault().post(new ContactBean());
                    ToastUtil.showToast(RecommendationActivity.this, "添加医生成功！");
                    mEditBindNumber.setText("");
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if(handleNetworkOnFailure(errorEvent, message)) return;
                    ToastUtil.showToast(RecommendationActivity.this,message);
                }
            });
        }

    }

    private void goPerfectInfoActivity() {
        Intent intent = new Intent(this, PerfectInfoActivity.class);
        startActivity(intent);
    }
}
