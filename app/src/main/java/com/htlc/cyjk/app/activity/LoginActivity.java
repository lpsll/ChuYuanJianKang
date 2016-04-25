
package com.htlc.cyjk.app.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.util.AppManager;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.RongIMUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.UserBean;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTextButtonLogin, mTextButtonRegister, mTextButtonForget;
    private EditText mEditUsername,mEditPassword;

    public static void start(Context context, Intent extras) {
        LogUtil.e("LoginActivity","begin: " + System.currentTimeMillis());
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.e("LoginActivity","middle: " + System.currentTimeMillis());
        LogUtil.e("LoginActivity", "end: " + System.currentTimeMillis());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        mTextButtonLogin = (TextView) findViewById(R.id.textButtonLogin);
        mTextButtonRegister = (TextView) findViewById(R.id.textButtonRegister);
        mTextButtonForget = (TextView) findViewById(R.id.textButtonForget);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditPassword = (EditText) findViewById(R.id.editPassword);

        mTextButtonLogin.setOnClickListener(this);
        mTextButtonRegister.setOnClickListener(this);
        mTextButtonForget.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButtonLogin:
                login();
                break;
            case R.id.textButtonRegister:
                goRegister(true);
                break;
            case R.id.textButtonForget:
                goRegister(false);
                break;
        }
    }

    private void login() {
        final String username = mEditUsername.getText().toString().trim();
        final String password = mEditPassword.getText().toString().trim();
        showProgressHUD();
        appAction.login(username, password, new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                data.username = username;
                data.password = password;
                application.setUserBean(data);
                connect(data.token);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(LoginActivity.this, message);
                dismissProgressHUD();
            }
        });
    }

    private void goRegister(boolean isRegister) {
        Intent intent = null;
        if(isRegister){
            intent = new Intent(this, RegisterActivity.class);
        }else {
            intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.IsRegister,false);
        }
        startActivity(intent);
    }

    private void goMainOrGuide() {
        String flag = application.getUserBean().flag;
        switch (flag){
            case "0":
                MainActivity.start(this,null);
                finish();
                break;
            case "2":
            case "3":
                goRecommendation();
                break;
            default:
                MainActivity.start(this,null);
                finish();
        }

    }

    private void goRecommendation() {
        Intent intent = new Intent(LoginActivity.this, RecommendationActivity.class);
        intent.putExtra(RecommendationActivity.IsLoginBind,true);
        startActivity(intent);
        finish();
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        LogUtil.e("RongIMUtil", "connect");
        if (App.app.getApplicationInfo().packageName.equals(App.getCurProcessName(App.app.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    dismissProgressHUD();
                    LogUtil.e("RongIM TOKEN connect", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    dismissProgressHUD();
                    App.app.setIsLogin(true);
                    LogUtil.e("RongIM TOKEN connect", "--onSuccess" + userid);
                    goMainOrGuide();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    dismissProgressHUD();
                    LogUtil.e("RongIM TOKEN connect", "error:" + errorCode);
                }
            });
        }
    }
}
