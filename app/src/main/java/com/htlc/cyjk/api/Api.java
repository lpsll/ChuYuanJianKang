
package com.htlc.cyjk.api;


import com.htlc.cyjk.api.net.okhttp.callback.ResultCallback;
import com.htlc.cyjk.model.ChargeBean;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.DischargeSummaryBean;
import com.htlc.cyjk.model.InformationBean;
import com.htlc.cyjk.model.MedicalHistoryItemBean;
import com.htlc.cyjk.model.MessageBean;
import com.htlc.cyjk.model.NetworkCityBean;
import com.htlc.cyjk.model.PersonBean;
import com.htlc.cyjk.model.PriceBean;
import com.htlc.cyjk.model.UpdateCityBean;
import com.htlc.cyjk.model.UserBean;

import java.io.File;

public interface Api {
    String Host = "http://z3.damaimob.com/index.php/home/";
    String SendSmsCode = Host + "user_verifycode";
    String Register = Host + "user_enroll";
    String Login = Host + "user_login";
    String Forget = Host + "user_resetpwd";
    String Protocol = Host + "user_getprotocol";
    String ChangeUsername = Host + "user_updatephone ";

    String BindDoctor = Host + "sundry_adddoctor";
    String PostPersonInfo = Host + "user_patientinfo";
    String DischargeSummary = Host + "user_outinfo";
    String MedicineHistory = Host + "infowrite_medicinehistory";
    String DrugsList = Host + "infowrite_querymedicine";
    String PostDrugs = Host + "infowrite_takepill";
    String ContactList = Host + "sundry_sessionlist";
    String InformationList = Host + "sundry_magazinelist";
    String InformationDetail = Host + "sundry_magazineinfo";
    String MessageList = Host + "sundry_msglist";
    String MessageDetail = Host + "sundry_getmsginfo";
    String MyCenter = Host + "sundry_getphoto";
    String GetPersonInfo = Host + "user_getuserinfo";
    String GetDischargeSummary = Host + "user_getoutinfo";
    String ConversationPermission = Host + "sundry_getlimit";
    String GetPriceList = Host + "pay_getdoctorprice";
    String PayToDoctor = Host + "pay_payto";
    String CreateOrder = Host + "order_createorder";


    String GetProvince = Host + "city_getprovince";
    String GetCity = Host + "city_getcity";
    String GetCounty = Host + "city_getarea";
    String GetAllCity = Host + "city_getall";



    /**
     * 获取验证码
     * @param username 手机号码
     * @return 成功时返回：{ "code": 0, "msg":"success" }
     */
    void sendSmsCode(String username, ResultCallback<ApiResponse<Void>> callback);

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param smsCode
     * @param callback
     */
    void register(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback);

    /**
     * @param username
     * @param password
     * @param callback
     */
    void login(String username, String password, ResultCallback<ApiResponse<UserBean>> callback);

    /**
     * @param username
     * @param password
     * @param smsCode
     * @param callback
     */
    void forget(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param newPhone
     * @param callback
     */
    void changeUsername(String newPhone,ResultCallback<ApiResponse<UserBean>> callback);
    /**
     *
     * @param userId
     * @param recommend
     * @param callback
     */
    void bindDoctor(String userId, String recommend, ResultCallback<ApiResponse<Void>> callback);

    /**
     * @param userId
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param profession
     * @param address
     * @param photo
     * @param callback
     */
    void postPersonInfo(String userId, String phone, String name, String sex, String age, String profession, String address, File photo, ResultCallback<ApiResponse<Void>> callback);

    /**
     * 参数:*userid 用户id
     * hospitalize 入院日期
     * discharged 出院日期
     * inDay 住院天数
     * inDiagnose 入院诊断
     * outDiagnose 出院诊断
     * result 治疗结果
     * checkNum 特殊检查号
     * inInfo 入院情况
     * onIfo 住院情况
     * outInfo 出院情况
     * advice 出院医嘱
     */
    void dischargeSummary(String userId,String startTime,String endTime,String totalTime,
                          String inDiagnose,String outDiagnose,String effect,String specialItem,String inStatus,String atStatus,String outStatus,String outAdvice,String job, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void medicineHistory(String userId,ResultCallback<ApiResponse<MedicalHistoryItemBean>> callback);

    /**
     *
     * @param drugName
     * @param callback
     */
    void drugsList(String drugName, ResultCallback<String> callback);

    /**
     *
     * @param userId
     * @param date
     * @param drugsJson
     * @param callback
     */
    void postDrugs(String userId, String date, String drugsJson, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void contactList(String userId, ResultCallback<ApiResponse<ContactBean>> callback);

    /**
     *
     * @param username
     * @param page
     * @param callback
     */
    void informationList(String username, String page, ResultCallback<ApiResponse<InformationBean>> callback);

    /**
     *
     * @param userId
     * @param page
     * @param callback
     */
    void messageList(String userId,String page, ResultCallback<ApiResponse<MessageBean>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void myCenter(String userId,ResultCallback<ApiResponse<UserBean>> callback);

    /**
     * 获取用户信息
     * @param userId
     * @param callback
     */
    void getPersonInfo(String userId,ResultCallback<ApiResponse<PersonBean>> callback);

    /**
     * 获取出院小结信息
     * @param userId
     * @param callback
     */
    void getDischargeSummary(String userId, ResultCallback<ApiResponse<DischargeSummaryBean>> callback);

    /**
     *
     * @param doctorId
     * @param callback
     */
    void conversationPermission(String doctorId,ResultCallback<ApiResponse<Void>> callback);

    void getPriceList(String doctorId,ResultCallback<ApiResponse<PriceBean>> callback);

    void payToDoctor(String doctorId,String timeLengthId,String channel, ResultCallback<ApiResponse<ChargeBean>> callback);

    void createOrder(String drugsJson, ResultCallback<ApiResponse<Void>> callback);

    void getAllCity(String lastModifyData, ResultCallback<ApiResponse<UpdateCityBean>> callback);
}
