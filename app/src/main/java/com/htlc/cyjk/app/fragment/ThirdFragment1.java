package com.htlc.cyjk.app.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.activity.WebActivity;
import com.htlc.cyjk.app.adapter.ThirdAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.InformationBean;
import com.htlc.cyjk.model.UserBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class ThirdFragment1 extends HomeFragment{
    private WebView mWebView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_1,null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webView);
        initWebView();
        initData();
    }
    private void initWebView() {
        mWebView.getSettings().setSupportZoom(true);          //支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);  //启用内置缩放装置
//        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDisplayZoomControls(false);//隐藏缩放图标
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//缓存
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); //支持内容重新布局
        //运行js代码
        mWebView.getSettings().setJavaScriptEnabled(true);
        //先不加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        //给web view设置客户端
        mWebView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        //不显示滚动条
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }
    private void initData() {
        String phone = App.app.getUserBean().username;
        mWebView.loadUrl(Api.InformationList+"?phone="+phone);
    }

}
