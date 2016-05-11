package com.htlc.cyjk.app.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.htlc.cyjk.R;
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.api.net.okhttp.callback.ResultCallback;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.activity.BaseActivity;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.AppVersionBean;
import com.squareup.okhttp.Request;

import java.io.File;

/**
 * Created by sks on 2016/5/11.
 */
public class UpdateUtil {
    private BaseActivity activity;
    private ProgressDialog downloadDialog;

    public UpdateUtil(BaseActivity activity){
        this.activity = activity;
    }
    /**
     * 检查更新
     * @param flag  手动更新
     */
    public void canUpdate(final boolean flag) {
        activity.appAction.checkUpdate(new ActionCallbackListener<AppVersionBean>() {
            @Override
            public void onSuccess(AppVersionBean data) {
                if(!flag && SharedPreferenceUtil.getString(App.app,Constant.IGNORE_VERSION_CODE,"").equals(data.appVersionNo)){
                    return;
                }
                int versionCode = CommonUtil.getVersionCode();
                if(versionCode < Integer.parseInt(data.appVersionNo)){
                    doNewVersionUpdate(data);
                }else if(flag){
                    ToastUtil.showToast(App.app,"已经是最新版本！");
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(flag){
                    ToastUtil.showToast(App.app,message);
                }
            }
        });

    }

    /**
     * 有新版本
     * @param appVersionBean
     */
    private void doNewVersionUpdate( final AppVersionBean appVersionBean) {
        String verName = CommonUtil.getVersionName();
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(", 发现新版本:");
        sb.append(appVersionBean.appVersion);
        sb.append(", 是否更新?");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("软件更新");//设置对话框标题
        builder.setMessage(sb.toString());//设置显示的内容

        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                downloadApk(appVersionBean);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("忽略该版本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtil.putString(App.app, Constant.IGNORE_VERSION_CODE,appVersionBean.appVersionNo);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                dialog.dismiss();
            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }

    /**
     * 下载apk
     */
    private void downloadApk(AppVersionBean appVersionBean) {
        String url = String.format(Api.DownloadApk,Constant.PGY_APP_ID,Constant.PGY_API_KEY);
        String newVersionName = "V"+appVersionBean.appVersion;
        downloadDialog = new ProgressDialog(activity);
        downloadDialog.setTitle("更新中");
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setMax(100);
        downloadDialog.show();
        activity.appAction.downloadApk(url, activity.getString(R.string.app_name) + newVersionName+".apk", new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                if(downloadDialog !=null){
                    downloadDialog.dismiss();
                }
                ToastUtil.showToast(App.app,"网络出错！");
            }

            @Override
            public void onResponse(String response) {
                if(downloadDialog !=null){
                    downloadDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(response)),
                        "application/vnd.android.package-archive");
                activity.startActivity(intent);
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                progress = progress * 100;
                if(downloadDialog !=null){
                    downloadDialog.setProgress((int) progress);
                }
            }
        });
    }
}
