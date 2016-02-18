
package com.htlc.cyjk.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cyjk.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextButtonLogin, mTextButtonRegister, mTextButtonForget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        mTextButtonLogin = (TextView) findViewById(R.id.textButtonLogin);
        mTextButtonRegister = (TextView) findViewById(R.id.textButtonRegister);
        mTextButtonForget = (TextView) findViewById(R.id.textButtonForget);

        mTextButtonLogin.setOnClickListener(this);
        mTextButtonRegister.setOnClickListener(this);
        mTextButtonForget.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.textButtonLogin:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.textButtonRegister:
                intent = new Intent(this, RegisterActivity.class);
                break;
            case R.id.textButtonForget:
                intent = new Intent(this, PerfectInfoActivity.class);
                break;
        }
        startActivity(intent);
    }


}
