
package com.htlc.cyjk.core;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.htlc.cyjk.R;
import com.htlc.cyjk.api.Api;
import com.htlc.cyjk.api.ApiImpl;
import com.htlc.cyjk.api.ApiResponse;
import com.htlc.cyjk.api.net.okhttp.callback.ResultCallback;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.db.DbManager;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.app.util.JsonUtil;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.RegExUtil;
import com.htlc.cyjk.app.util.SharedPreferenceUtil;
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
import com.htlc.cyjk.model.UpdateCityBean;
import com.htlc.cyjk.model.UserBean;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class AppActionImpl implements AppAction {

    private final static int LOGIN_OS = 1; // 表示Android
    private final static int PAGE_SIZE = 20; // 默认每页20条

    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void sendSmsCode(final String username, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.sendSmsCode(username, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });

    }

    @Override
    public void register(final String username, final String code, final String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.register(username, password, code, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });

    }

    @Override
    public void login(final String username, final String password, final ActionCallbackListener<UserBean> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "登录名不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        // 请求Api
        api.login(username, password, new ResultCallback<ApiResponse<UserBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UserBean> response) {
                if ("1".equals(response.code)) {
                    LogUtil.e(AppActionImpl.this,"注册状态："+response.data.flag);
                    listener.onSuccess(response.data);
                    App.app.setUserBean(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void forget(String username, String code, String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.forget(username, password, code, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void bindDoctor(String userId, String recommend, final ActionCallbackListener<Void> listener) {
        api.bindDoctor(userId, recommend, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void postPersonInfo(String userId, String phone, String name, String sex, String age, String profession, String address, File photo, final ActionCallbackListener<Void> listener) {
        LogUtil.e(this,name+"");
        if (TextUtils.isEmpty(name)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入姓名");
            }
            return;
        }
        if (TextUtils.isEmpty(age)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入年龄");
            }
            return;
        }
        if (TextUtils.isEmpty(profession)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入职业");
            }
            return;
        }
        if (TextUtils.isEmpty(address)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入地区");
            }
            return;
        }
        api.postPersonInfo(userId, phone, name, sex, age, profession, address, photo, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void changeUsername(String phone, final ActionCallbackListener<UserBean> listener) {
        if (TextUtils.isEmpty(phone)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(phone)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        api.changeUsername(phone, new ResultCallback<ApiResponse<UserBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UserBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void postPersonInfo(String userId, String name, String sex, String age, String address, File photo, final ActionCallbackListener<Void> listener) {
        LogUtil.e(this,name+"");
        api.postPersonInfo(userId, "", name, sex, age, "", address, photo, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void dischargeSummary(String userId, String startTime, String endTime, String totalTime,
                                 String inDiagnose, String outDiagnose, String effect, String specialItem,
                                 String inStatus, String atStatus, String outStatus, String outAdvice,String job, final ActionCallbackListener<Void> listener) {
        api.dischargeSummary(userId, startTime, endTime, totalTime, inDiagnose, outDiagnose, effect, specialItem, inStatus, atStatus, outStatus, outAdvice,job, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void medicineHistory(String userId, final ActionCallbackListener<ArrayList<MedicalHistoryItemBean>> listener) {
        api.medicineHistory(userId, new ResultCallback<ApiResponse<MedicalHistoryItemBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<MedicalHistoryItemBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void drugsList(String drugName, final ActionCallbackListener<ArrayList<DrugBean>> listener) {
        api.drugsList(drugName, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(String response) {
                LogUtil.e(AppActionImpl.this, response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    String code = responseObj.getString("code");
                    if (listener != null) {
                        if (code.equals(Constant.REQUEST_ERROR)) {
                            String msg = responseObj.getString("msg");
                            listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                        } else if (code.equals(Constant.REQUEST_SUCCESS)) {
                            ArrayList<DrugBean> list = new ArrayList<>();
                            String data = responseObj.getString("data");
                            JSONObject jsonObject = new JSONObject(data);
                            Iterator<String> iterator = jsonObject.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                String value = jsonObject.getString(key);
                                List<DrugBean> temp = (List<DrugBean>) JsonUtil.parseJsonToList(value, new TypeToken<List<DrugBean>>() {
                                }.getType());
                                for (int i = 0; i < temp.size(); i++) {
                                    temp.get(i).group = key;
                                }
                                list.addAll(temp);
                            }
                            Collections.sort(list);
                            listener.onSuccess(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
                }

            }
    });
    }

    @Override
    public void postDrugs(String userId, String date, String drugsJson, final ActionCallbackListener<Void> listener) {
        api.postDrugs(userId, date, drugsJson, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void contactList(String userId, final ActionCallbackListener<ArrayList<ContactBean>> listener) {
        api.contactList(userId, new ResultCallback<ApiResponse<ContactBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ContactBean> response) {
                Collections.sort(response.dataArray);
                listener.onSuccess(response.dataArray);
            }

            /*@Override
            public void onResponse(String response) {
                LogUtil.e(AppActionImpl.this, response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    String code = responseObj.getString("code");
                    if (listener != null) {
                        if (code.equals(Constant.REQUEST_ERROR)) {
                            String msg = responseObj.getString("msg");
                            listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                        } else if (code.equals(Constant.REQUEST_SUCCESS)) {
                            ArrayList<ContactBean> list = new ArrayList<>();
                            String data = responseObj.getString("data");
                            JSONObject jsonObject = new JSONObject(data);
                            Iterator<String> iterator = jsonObject.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                String value = jsonObject.getString(key);
                                List<ContactBean> temp = (List<ContactBean>) JsonUtil.parseJsonToList(value, new TypeToken<List<ContactBean>>() {
                                }.getType());
                                for (int i = 0; i < temp.size(); i++) {
                                    temp.get(i).head = key;
                                }
                                list.addAll(temp);
                            }
                            Collections.sort(list);
                            listener.onSuccess(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
                }

            }*/
        });
    }

    @Override
    public void informationList(String username, int page, final ActionCallbackListener<ArrayList<InformationBean>> listener) {
        api.informationList(username, page + "", new ResultCallback<ApiResponse<InformationBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<InformationBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void messageList(String userId, int page, final ActionCallbackListener<ArrayList<MessageBean>> listener) {
        api.messageList(userId, page+"", new ResultCallback<ApiResponse<MessageBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<MessageBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void myCenter(String userId, final ActionCallbackListener<UserBean> listener) {
        api.myCenter(userId, new ResultCallback<ApiResponse<UserBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UserBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getPersonInfo(String userId, final ActionCallbackListener<PersonBean> listener) {
        api.getPersonInfo(userId, new ResultCallback<ApiResponse<PersonBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<PersonBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getDischargeSummary(String userId, final ActionCallbackListener<DischargeSummaryBean> listener) {
        api.getDischargeSummary(userId, new ResultCallback<ApiResponse<DischargeSummaryBean>>() {
            @Override
            public void onError(Request request, Exception e) {
               e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<DischargeSummaryBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void conversationPermission(String doctorId, final ActionCallbackListener<Void> listener) {
        api.conversationPermission(doctorId, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getPriceList(String doctorId, final ActionCallbackListener<ArrayList<PriceBean>> listener) {
        api.getPriceList(doctorId, new ResultCallback<ApiResponse<PriceBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<PriceBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void payToDoctor(String doctorId, String timeLengthId, String channel, final ActionCallbackListener<ChargeBean> listener) {
        api.payToDoctor(doctorId, timeLengthId, channel, new ResultCallback<ApiResponse<ChargeBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ChargeBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void createOrder(String drugsJson, final ActionCallbackListener<Void> listener) {
        api.createOrder(drugsJson, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getAllCity(final ActionCallbackListener<ArrayList<NetworkCityBean>> listener) {
        String lastModifyDate = SharedPreferenceUtil.getString(App.app, DbManager.DATABASE_LAST_MODIFY,"0");
        LogUtil.e("getAllCity",lastModifyDate);
        api.getAllCity(lastModifyDate, new ResultCallback<ApiResponse<UpdateCityBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UpdateCityBean> response) {
                if ("1".equals(response.code)) {
                    SharedPreferenceUtil.putString(App.app, DbManager.DATABASE_LAST_MODIFY, response.data.update);
                    listener.onSuccess(response.data.city);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }
}
