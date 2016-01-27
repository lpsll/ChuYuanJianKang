package com.htlc.cyjk.app.util;

/**
 * Created by sks on 2015/11/9.
 *
 */
public class ClickUtil {
    private static long lastClickTime;

    /**
     * 用于判断按钮或控件是否是快速点击
     * ture 为快速点击
     * false 为普通点击
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
