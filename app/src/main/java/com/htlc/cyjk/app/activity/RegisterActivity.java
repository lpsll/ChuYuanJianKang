
package com.htlc.cyjk.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    public static final String IsRegister = "IsRegister";
    private boolean isRegister = true;

    private TextView mTextButtonRegister, mTextButtonGetVerification,mTextButtonProtocol,mTextTitle,mTextCheckBoxHint;
    private EditText mEditUsername,mEditVerification,mEditPassword;

    private Handler handler = new Handler();//用于刷新倒计时的时间
    private int time = Constant.VERIFICATION_TIME;//获取验证码成功后，倒计时60s
    private CheckBox mCheckBox, mCheckBoxEye;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRegister = getIntent().getBooleanExtra(IsRegister,true);
        setContentView(R.layout.activity_register);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle = (TextView) findViewById(R.id.textTitle);
        mTextButtonRegister = (TextView) findViewById(R.id.textButtonRegister);
        mTextButtonGetVerification = (TextView) findViewById(R.id.textButtonGetVerification);
        mTextButtonProtocol = (TextView) findViewById(R.id.textButtonProtocol);
        mTextCheckBoxHint = (TextView) findViewById(R.id.textCheckBoxHint);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditVerification = (EditText) findViewById(R.id.editVerification);
        mEditPassword = (EditText) findViewById(R.id.editPassword);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBoxEye = (CheckBox) findViewById(R.id.checkBoxEye);
        if(!isRegister){
            mTextTitle.setText("忘记密码");
            mTextButtonRegister.setText("重置密码");
            mEditPassword.setHint("请输入新密码");
            mCheckBox.setVisibility(View.INVISIBLE);
            mTextButtonProtocol.setVisibility(View.INVISIBLE);
            mTextCheckBoxHint.setVisibility(View.INVISIBLE);
        }

        mTextButtonRegister.setOnClickListener(this);
        mTextButtonGetVerification.setOnClickListener(this);
        mTextButtonProtocol.setOnClickListener(this);
        mCheckBoxEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.e(RegisterActivity.this,isChecked+"===isChecked");
                if(isChecked){
                    mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CharSequence text = mEditPassword.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButtonGetVerification:
                sendSmsCode();
                break;
            case R.id.textButtonRegister:
                if(isRegister){
                    register();
                }else {
                    resetPassword();
                }
                break;
            case R.id.textButtonProtocol:
                protocol();
                break;
        }
    }

    private void resetPassword() {
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        String smsCode = mEditVerification.getText().toString().trim();
        appAction.forget(username, smsCode, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(RegisterActivity.this, "重置成功！");
                goLogin();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(RegisterActivity.this, message);
            }
        });
    }

    private void protocol() {
        LogUtil.e(this, "protocol");
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra(WebActivity.Title,"用户协议");
        intent.putExtra(WebActivity.Url, Api.Protocol);
        startActivity(intent);
    }

    private void register() {
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        String smsCode = mEditVerification.getText().toString().trim();
        if(!mCheckBox.isChecked()){
            ToastUtil.showToast(RegisterActivity.this, "请同意相关协议！");
            return;
        }
        appAction.register(username, smsCode, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(RegisterActivity.this, "注册成功！");
                goLogin();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(RegisterActivity.this, message);
                handler.removeCallbacksAndMessages(null);
                mTextButtonGetVerification.setText("重新获取验证码");
                mTextButtonGetVerification.setEnabled(true);
                time = Constant.VERIFICATION_TIME;
            }
        });
    }

    private void goLogin() {
//        LoginActivity.start(this,null);
        finish();
    }

    private void sendSmsCode() {
        String username = mEditUsername.getText().toString().trim();
        appAction.sendSmsCode(username, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                showTimer();
                ToastUtil.showToast(RegisterActivity.this, "获取验证码成功！");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                mTextButtonGetVerification.setEnabled(true);
                ToastUtil.showToast(RegisterActivity.this, message);
            }
        });
    }
    /**
     * 显示倒计时
     */
    private void showTimer() {
        mTextButtonGetVerification.setEnabled(false);//不允许再次获取验证码
        handler.postDelayed(runnable, 1000);

    }

    /**
     * 用于更新倒计时
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                time--;
                mTextButtonGetVerification.setText("(" + time + "s)后重新获取");
                handler.postDelayed(this, 1000);
            } else {
                mTextButtonGetVerification.setText("重新获取验证码");
                mTextButtonGetVerification.setEnabled(true);
                time = Constant.VERIFICATION_TIME;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
