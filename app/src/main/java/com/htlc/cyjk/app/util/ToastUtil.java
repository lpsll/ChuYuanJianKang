package com.htlc.cyjk.app.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static Toast mToast;

    /**
     * 立即连续弹吐司
     *
     * @param mContext
     * @param msg
     */
    public static void showToast(final Context mContext, final String msg) {


        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 立即连续弹吐司
     *
     * @param mContext
     * @param msgId
     */
    public static void showToast(final Context mContext, final int msgId) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msgId);
        mToast.show();
    }
}
