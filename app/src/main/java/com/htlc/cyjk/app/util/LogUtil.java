package com.htlc.cyjk.app.util;

import android.util.Log;

/**
 * Created by sks on 2015/10/28.
 * 用于打印日志
 * 在开发过程中，isDebug设置为true，打印日志
 * 打包发布apk时，isDebug设置false，应用不再打印日志，提高应用的效率
 */
public class LogUtil {
    private static boolean isDebug = true;

    /**
     * tag为字符串 msg 日志信息
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg){
        if(isDebug){
            Log.i(tag,msg);
        }
    }

    /**
     * tag 为 obj对象的类名  msg为 日志信息
     * @param obj
     * @param msg
     */
    public static void i(Object obj, String msg){
        if(isDebug){
            Log.i(obj.getClass().getSimpleName().toString(),msg);
        }
    }
    /**
     * tag为字符串 msg 日志信息
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg){
        if(isDebug){
            Log.e(tag,msg);
        }
    }

    /**
     * tag 为 obj对象的类名  msg为 日志信息
     * @param obj
     * @param msg
     */
    public static void e(Object obj, String msg){
        if(isDebug){
            Log.e(obj.getClass().getSimpleName().toString(),msg);
        }
    }
}
