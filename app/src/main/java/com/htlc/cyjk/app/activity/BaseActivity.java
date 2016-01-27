
package com.htlc.cyjk.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.htlc.cyjk.app.App;
import com.htlc.cyjk.core.AppAction;

public abstract class BaseActivity extends AppCompatActivity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public App application;
    // 核心层的Action实例
    public AppAction appAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (App) this.getApplication();
        appAction = application.getAppAction();
    }
}
