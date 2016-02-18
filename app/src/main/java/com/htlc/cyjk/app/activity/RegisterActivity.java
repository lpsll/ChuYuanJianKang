
package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.LogUtil;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextButtonRegister, mTextButtonGetVerification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupView();
    }

    private void setupView() {
        mTextButtonRegister = (TextView) findViewById(R.id.textButtonRegister);
        mTextButtonGetVerification = (TextView) findViewById(R.id.textButtonGetVerification);

        mTextButtonRegister.setOnClickListener(this);
        mTextButtonGetVerification.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButtonGetVerification:
                LogUtil.e(this,"textButtonGetVerification");
                break;
            case R.id.textButtonRegister:
                break;
            case R.id.textButtonForget:
                break;
        }
    }
}
