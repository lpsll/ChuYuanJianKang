package com.htlc.cyjk.app.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.util.AppManager;
import com.htlc.cyjk.app.util.SharedPreferenceUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sks on 2016/2/15.
 */
public class SplashActivity extends BaseActivity {
    public static final String IsFirstStart = "IsFirstStart";

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mImageView = (ImageView) findViewById(R.id.imageView);
        App.app.initDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        ValueAnimator animator = ValueAnimator.ofFloat(0.4f, 1.0f);
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mImageView.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                goNextActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void goNextActivity() {
        int isFirst = SharedPreferenceUtil.getInt(this, IsFirstStart, -1);
        if(isFirst == -1){
            Intent intent = new Intent(this,GuideActivity.class);
            startActivity(intent);
            finish();
        }else {
            application.initLoginStatus();
            if (application.isLogin()) {
                String flag = application.getUserBean().flag;
                switch (flag){
                    case "2":
                    case "3":
                        goLogin();
                        break;
                    case "0":
                    default:
                        goMain();
                }
            }else {
                goLogin();
            }
        }

    }

    private void goMain() {
        MainActivity.start(this, null);
        finish();
    }

    private void goLogin() {
        AppManager.getAppManager().finishBeforeActivity();
        LoginActivity.start(this, null);
        finish();
    }

    private void goRecommendation() {
        Intent intent = new Intent(SplashActivity.this, RecommendationActivity.class);
        intent.putExtra(RecommendationActivity.IsLoginBind,true);
        startActivity(intent);
        finish();
    }
}
