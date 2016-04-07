
package com.htlc.cyjk.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.util.AppManager;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.core.AppAction;
import com.kaopiz.kprogresshud.KProgressHUD;

public abstract class BaseActivity extends AppCompatActivity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public App application;
    // 核心层的Action实例
    public AppAction appAction;
    protected KProgressHUD mProgressHUD;
    protected Dialog mTipsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (App) this.getApplication();
        appAction = application.getAppAction();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showProgressHUD(){
        mProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f).show();
    }
    protected void dismissProgressHUD(){
        if(mProgressHUD!=null){
            mProgressHUD.dismiss();
        }
    }
    private void showReLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录过期");//设置对话框标题
        builder.setMessage("请重新登录！");//设置显示的内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                LoginActivity.start(BaseActivity.this,null);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }
    public boolean handleNetworkOnFailure(String errorEvent, String message){
        if("请重新登录".equals(message)){
            application.setIsLogin(false);
            application.clearUserBean();
            showReLoginDialog();
            return true;
        }
        return false;
    }
    public void showTipsDialog(View view, int width, int height,boolean cancelable) {
        mTipsDialog = new Dialog(this, R.style.CenterDialog);
        mTipsDialog.setContentView(view);
        mTipsDialog.setCancelable(cancelable);
        Window dialogWindow = mTipsDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        if(width!=0){
            lp.width = width * d.getWidth() / Constant.ScreenWidth; // 宽度
        }
        if(height!=0){
            lp.height = height * d.getHeight() / Constant.ScreenHeight; // 高度
        }
        lp.alpha = 1.0f; // 透明度
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        mTipsDialog.show();
    }
    public void dismissTipsDialog(){
        if(mTipsDialog!=null){
            mTipsDialog.dismiss();
        }
    }
}
