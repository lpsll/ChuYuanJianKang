
package com.htlc.cyjk.core;

import com.htlc.cyjk.model.ChargeBean;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.DischargeSummaryBean;
import com.htlc.cyjk.model.DrugBean;
import com.htlc.cyjk.model.InformationBean;
import com.htlc.cyjk.model.MedicalHistoryItemBean;
import com.htlc.cyjk.model.MessageBean;
import com.htlc.cyjk.model.NetworkCityBean;
import com.htlc.cyjk.model.PersonBean;
import com.htlc.cyjk.model.PriceBean;
import com.htlc.cyjk.model.UserBean;

import java.io.File;
import java.util.ArrayList;

public interface AppAction {
    /**
     * 发送验证码
     *
     * @param username 手机号
     * @param listener 回调监听器
     */
    public void sendSmsCode(String username, ActionCallbackListener<Void> listener);

    /**
     * 注册
     *
     * @param username 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    public void register(String username, String code, String password, ActionCallbackListener<Void> listener);

    /**
     * 登录
     *
     * @param username 登录名
     * @param password 密码
     * @param listener 回调监听器
     */
    public void login(String username, String password, ActionCallbackListener<UserBean> listener);

    /**
     * @param username 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    public void forget(String username, String code, String password, ActionCallbackListener<Void> listener);

    /**
     * @param userId
     * @param recommend
     * @param listener
     */
    void bindDoctor(String userId, String recommend, ActionCallbackListener<Void> listener);

    /**
     * @param userId
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param profession
     * @param address
     * @param photo
     * @param listener
     */
    public void postPersonInfo(String userId, String phone, String name, String sex, String age, String profession, String address, File photo, ActionCallbackListener<Void> listener);

    void changeUsername(String phone, ActionCallbackListener<UserBean> listener);
    /**
     *
     * @param userId
     * @param name
     * @param sex
     * @param age
     * @param address
     * @param photo
     * @param listener
     */
    void postPersonInfo(String userId, String name, String sex, String age, String address, File photo, ActionCallbackListener<Void> listener);

    /**
     * @param userId
     * @param startTime
     * @param endTime
     * @param totalTime
     * @param inDiagnose
     * @param outDiagnose
     * @param effect
     * @param specialItem
     * @param inStatus
     * @param atStatus
     * @param outStatus
     * @param outAdvice
     * @param listener
     */
    void dischargeSummary(String userId, String startTime, String endTime, String totalTime,
                          String inDiagnose, String outDiagnose, String effect, String specialItem, String inStatus, String atStatus, String outStatus, String outAdvice, String job, ActionCallbackListener<Void> listener);

    /**
     * @param userId
     * @param listener
     */
    void medicineHistory(String userId, ActionCallbackListener<ArrayList<MedicalHistoryItemBean>> listener);

    /**
     * @param drugName
     * @param listener
     */
    void drugsList(String drugName, ActionCallbackListener<ArrayList<DrugBean>> listener);

    /**
     * @param userId
     * @param date
     * @param drugsJson
     * @param listener
     */
    void postDrugs(String userId, String date, String drugsJson, ActionCallbackListener<Void> listener);

    /**
     * @param userId
     * @param listener
     */
    void contactList(String userId, ActionCallbackListener<ArrayList<ContactBean>> listener);

    /**
     * @param username
     * @param page
     * @param listener
     */
    void informationList(String username, int page, ActionCallbackListener<ArrayList<InformationBean>> listener);

    /**
     * @param userId
     * @param page
     * @param listener
     */
    void messageList(String userId, int page, ActionCallbackListener<ArrayList<MessageBean>> listener);

    /**
     * @param userId
     * @param listener
     */
    void myCenter(String userId, ActionCallbackListener<UserBean> listener);

    /**
     * @param userId
     * @param listener
     */
    void getPersonInfo(String userId, ActionCallbackListener<PersonBean> listener);

    /**
     *
     * @param userId
     * @param listener
     */
    void getDischargeSummary(String userId,ActionCallbackListener<DischargeSummaryBean> listener);

    /**
     *
     * @param doctorId
     * @param listener
     */
    void conversationPermission(String doctorId,ActionCallbackListener<Void> listener);

    void getPriceList(String doctorId,ActionCallbackListener<ArrayList<PriceBean>> listener);

    void payToDoctor(String doctorId, String timeLengthId, String channel,ActionCallbackListener<ChargeBean> listener);
    void createOrder(String drugsJson, ActionCallbackListener<Void> listener);

    void getAllCity(ActionCallbackListener<ArrayList<NetworkCityBean>> listener);
}
