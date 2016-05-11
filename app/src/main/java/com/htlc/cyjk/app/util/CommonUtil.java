package com.htlc.cyjk.app.util;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.htlc.cyjk.app.App;

import java.io.ByteArrayOutputStream;


public class CommonUtil {
    /**
     * dp转xp
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    /**
     * 获取application对象
     * @return
     */
    public static App getApplication() {
        return App.app;
    }

    /**
     * 获取当前应用版本号
     * @return
     */
    public static String getVersionName() {
        try {
            String packageName = getApplication().getPackageName();
            LogUtil.i(getApplication(),packageName);
            PackageInfo packageInfo = getApplication().getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.i(getApplication(), "获取版本版本名出错");
        }
        return "0";
    }
    /**
     * 获取当前应用版本号
     * @return
     */
    public static int getVersionCode() {
        try {
            String packageName = getApplication().getPackageName();
            LogUtil.i(getApplication(),packageName);
            PackageInfo packageInfo = getApplication().getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.i(getApplication(), "获取版本版本名出错");
        }
        return 1;
    }




    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值,或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获得资源文件中定义的颜色
     * @param colorId
     * @return
     */
    public static int getResourceColor(int colorId){
       return getApplication().getResources().getColor(colorId);
    }
    /**
     * 获得资源文件中定义的String数组
     * @param stringArrayId
     * @return
     */
    public static String[] getResourceStringArray(int stringArrayId){
        return getApplication().getResources().getStringArray(stringArrayId);
    }

    /**
     * 获得资源文件中定义的String
     * @param stringId
     * @return
     */
    public static String getResourceString(int stringId){
        return getApplication().getResources().getString(stringId);
    }

    /**
     * 将bitmap对象转换为数组
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

