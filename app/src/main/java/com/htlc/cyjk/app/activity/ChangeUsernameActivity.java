package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.activity.BaseActivity;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.UserBean;


/**
 * Created by sks on 2016/2/15.
 */
public class ChangeUsernameActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextUsername, mTextButton;
    private EditText mEditText;
    private LinearLayout mLinearUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLinearUsername = (LinearLayout) findViewById(R.id.linearUsername);
        mTextUsername = (TextView) findViewById(R.id.textUsername);
        mTextButton = (TextView) findViewById(R.id.textButton);
        mEditText = (EditText) findViewById(R.id.editInput);
        mTextButton.setOnClickListener(this);
        refreshView();
    }

    private void refreshView() {
        mTextUsername.setText("+86-"+application.getUserBean().username);
        mLinearUsername.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        mTextButton.setText("更换");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                execute();
                break;
        }
    }

    private void execute() {
        String buttonName = mTextButton.getText().toString();
        if ("更换".equals(buttonName)) {
            mLinearUsername.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);
            mTextButton.setText("下一步");
        } else if ("下一步".equals(buttonName)) {
            checkPassword();
        } else if ("绑定".equals(buttonName)) {
            bindNewTel();
        }
    }

    private void checkPassword() {
        final String password = mEditText.getText().toString().trim();
        String username = application.getUserBean().username;
        appAction.login(username, password, new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                data.password = password;
                application.setUserBean(data);
                mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mEditText.setText("");
                mEditText.setHint("请输入新手机号");
                mTextButton.setText("绑定");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void bindNewTel() {
        final String username = mEditText.getText().toString().trim();
        appAction.changeUsername(username, new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                UserBean userBean = new UserBean();
                userBean.username = username;
                application.setUserBean(userBean);
                refreshView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(application,message);
            }
        });
    }
}
