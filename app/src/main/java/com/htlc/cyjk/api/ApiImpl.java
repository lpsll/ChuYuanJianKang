
package com.htlc.cyjk.api;


import android.text.TextUtils;
import android.util.Pair;

import com.htlc.cyjk.api.net.okhttp.callback.ResultCallback;
import com.htlc.cyjk.api.net.okhttp.request.OkHttpRequest;
import com.htlc.cyjk.api.utils.EncryptUtil;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.model.ChargeBean;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.DischargeSummaryBean;
import com.htlc.cyjk.model.InformationBean;
import com.htlc.cyjk.model.MedicalHistoryItemBean;
import com.htlc.cyjk.model.MessageBean;
import com.htlc.cyjk.model.PersonBean;
import com.htlc.cyjk.model.PriceBean;
import com.htlc.cyjk.model.UpdateCityBean;
import com.htlc.cyjk.model.UserBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ApiImpl implements Api {
    @Override
    public void sendSmsCode(String username, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        String url = Api.SendSmsCode;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }
    @Override
    public void register(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("verifycode", smsCode);
        params.put("type", "0");
        String url = Api.Register;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }
    @Override
    public void login(String username, String password, ResultCallback<ApiResponse<UserBean>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("type", "0");
        String url = Api.Login;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }
    @Override
    public void forget(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("verifycode", smsCode);
        params.put("type", "0");
        String url = Api.Forget;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void changeUsername(String newPhone, ResultCallback<ApiResponse<UserBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("phone", newPhone);
        String url = Api.ChangeUsername;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void bindDoctor(String userId, String recommend, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("recom", recommend);
        String url = Api.BindDoctor;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void postPersonInfo(String userId, String phone, String name, String sex, String age, String profession, String address, File photo, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        if (!TextUtils.isEmpty(phone)) {
            params.put("phone", phone);
        }
        if (!TextUtils.isEmpty(name)) {
            params.put("name", name);
        }
        if (!TextUtils.isEmpty(sex)) {
            params.put("sex", sex);
        }
        if (!TextUtils.isEmpty(age)) {
            params.put("age", age);
        }
        if (!TextUtils.isEmpty(profession)) {
            params.put("profession", profession);
        }
        if (!TextUtils.isEmpty(address)) {
            params.put("region", address);
        }
        String url = Api.PostPersonInfo;
        LogUtil.e(this, url);
        OkHttpRequest.Builder builder = new OkHttpRequest.Builder().url(url).params(params);
        if (photo != null) {
            builder.files(new Pair<String, File>("photo", photo));
        }
        builder.upload(callback);
    }

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
    @Override
    public void dischargeSummary(String userId, String startTime, String endTime, String totalTime,
                                 String inDiagnose, String outDiagnose, String effect, String specialItem,
                                 String inStatus, String atStatus, String outStatus, String outAdvice, String job, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("hospitalize", startTime);
        params.put("discharged", endTime);
        params.put("inDay", totalTime);
        if (!TextUtils.isEmpty(inDiagnose)) {
            params.put("inDiagnose", inDiagnose);
            LogUtil.e(this,"入院"+inDiagnose);
        }
        if (!TextUtils.isEmpty(outDiagnose)) {
            params.put("outDiagnose", outDiagnose);
        }
        if (!TextUtils.isEmpty(effect)) {
            params.put("result", effect);
        }
        if (!TextUtils.isEmpty(specialItem)) {
            params.put("checkNum", specialItem);
        }
        if (!TextUtils.isEmpty(inStatus)) {
            params.put("inInfo", inStatus);
        }
        if (!TextUtils.isEmpty(atStatus)) {
            params.put("onIfo", atStatus);
        }
        if (!TextUtils.isEmpty(outStatus)) {
            params.put("outInfo", outStatus);
        }
        if (!TextUtils.isEmpty(outAdvice)) {
            params.put("advice", outAdvice);
        }
        if (!TextUtils.isEmpty(job)) {
            params.put("profession", job);
        }
        String url = Api.DischargeSummary;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void medicineHistory(String userId, ResultCallback<ApiResponse<MedicalHistoryItemBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);
        params.put("patient", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.MedicineHistory;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void drugsList(String drugName, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token)?"":userId);

        if (!TextUtils.isEmpty(drugName)) {
            params.put("name", drugName);
        }
        String url = Api.DrugsList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void postDrugs(String userId, String date, String drugsJson, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("date", date);
        params.put("pills", drugsJson);
        String url = Api.PostDrugs;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void contactList(String userId, ResultCallback<ApiResponse<ContactBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("flag", "0");
        String url = Api.ContactList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void informationList(String username, String page, ResultCallback<ApiResponse<InformationBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("phone", username);
        params.put("num", page);
        String url = Api.InformationList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void messageList(String userId, String page, ResultCallback<ApiResponse<MessageBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("flag", "0");
        params.put("num", page);
        String url = Api.MessageList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void myCenter(String userId, ResultCallback<ApiResponse<UserBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.MyCenter;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getPersonInfo(String userId, ResultCallback<ApiResponse<PersonBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.GetPersonInfo;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getDischargeSummary(String userId, ResultCallback<ApiResponse<DischargeSummaryBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.GetDischargeSummary;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void conversationPermission(String doctorId, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("doctorid", doctorId);
        String url = Api.ConversationPermission;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getPriceList(String doctorId, ResultCallback<ApiResponse<PriceBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("doctorid", doctorId);
        String url = Api.GetPriceList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void payToDoctor(String doctorId, String timeLengthId, String channel, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("doctorid", doctorId);
        params.put("priceid", timeLengthId);
        params.put("channel", channel);
        String url = Api.PayToDoctor;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void createOrder(String drugsJson, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("drug", drugsJson);
        String url = Api.CreateOrder;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getAllCity(String lastModifyData, ResultCallback<ApiResponse<UpdateCityBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", lastModifyData);
        String url = Api.GetAllCity;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

}
