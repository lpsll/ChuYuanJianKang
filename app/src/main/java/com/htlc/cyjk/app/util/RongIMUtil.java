package com.htlc.cyjk.app.util;

import android.util.Log;

import com.htlc.cyjk.app.App;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by sks on 2016/2/19.
 */
public class RongIMUtil {
    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connect(String token) {
        LogUtil.e("RongIMUtil","connect");
        if (App.app.getApplicationInfo().packageName.equals(App.getCurProcessName(App.app.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("RongIM TOKEN connect", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtil.e("RongIM TOKEN connect", "--onSuccess" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e("RongIM TOKEN connect","error:"+errorCode);
                }
            });
        }
    }
}
