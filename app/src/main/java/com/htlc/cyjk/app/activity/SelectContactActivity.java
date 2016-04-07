package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.view.View;

import com.htlc.cyjk.R;

/**
 * Created by sks on 2016/3/24.
 */
public class SelectContactActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
