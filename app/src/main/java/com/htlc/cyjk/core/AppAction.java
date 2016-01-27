
package com.htlc.cyjk.core;

public interface AppAction {
    /**
     * 发送验证码
     *
     * @param phoneNum 手机号
     * @param listener 回调监听器
     */
    public void sendSmsCode(String phoneNum, ActionCallbackListener<Void> listener);

    /**
     * 注册
     *
     * @param phoneNum 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    public void register(String phoneNum, String code, String password, ActionCallbackListener<Void> listener);

    /**
     * 登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @param listener  回调监听器
     */
    public void login(String loginName, String password, ActionCallbackListener<Void> listener);
}
