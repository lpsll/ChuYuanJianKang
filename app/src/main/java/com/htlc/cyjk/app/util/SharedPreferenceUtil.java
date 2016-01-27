package com.htlc.cyjk.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sks on 2015/10/30.
 * 用于对sharedPreferences文件的操作
 * 如：增put删remove查get
 */
public class SharedPreferenceUtil {
    public static final String CONFIG_FILE_NAME = "config";
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static float getFloat(Context context, String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        float value = sp.getFloat(key, defValue);
        return value;
    }
    public static void putInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static void putFloat(Context context, String key, float value){
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void remove(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

}
