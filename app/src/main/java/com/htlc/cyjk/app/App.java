package com.htlc.cyjk.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.htlc.cyjk.app.db.DbManager;
import com.htlc.cyjk.app.db.ProvinceDao;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.RongIMUtil;
import com.htlc.cyjk.app.util.SharedPreferenceUtil;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.core.AppAction;
import com.htlc.cyjk.core.AppActionImpl;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.NetworkCityBean;
import com.htlc.cyjk.model.UserBean;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by sks on 2015/12/29.
 */
public class App extends Application {
    private AppAction appAction;
    public static App app;
    private boolean isLogin = false;//用户是否登录了
    private ArrayList<ContactBean> mContactList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appAction = new AppActionImpl(this);
        initImageLoader(this);
        initRongIM();
        initJPush();
        LogUtil.e("App OnCreate", "App OnCreate");

    }

    /**
     * 初始化数据库
     */
    public void initDatabase() {
        appAction.getAllCity(new ActionCallbackListener<ArrayList<NetworkCityBean>>() {
            @Override
            public void onSuccess(final ArrayList<NetworkCityBean> data) {
                new ProvinceDao().updateCityListTable(data);
                LogUtil.e("initDatabase", "入库成攻？" + data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                LogUtil.e("initDatabase", message);
            }
        });
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.stopPush(getApplicationContext());
    }

    public void initRongIMUserInfoProvider() {
        /**
         * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
         *
         * @param userInfoProvider 用户信息提供者。
         * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
         *                         如果 App 提供的 UserInfoProvider。
         *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
         *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
         * @see UserInfoProvider
         */
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }

        }, true);
    }

    /**
     * 初始化登录状态
     */
    public void initLoginStatus() {
        String username = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.USERNAME, "");
        String password = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.PASSWORD, "");
        String user_id = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.USER_ID, "");
        String token = SharedPreferenceUtil.getString(CommonUtil.getApplication(), Constant.TOKEN, "ss");
        if (TextUtils.isEmpty(username)) {
            return;
        }
        if (TextUtils.isEmpty(password)) {
            return;
        }
        if (TextUtils.isEmpty(user_id)) {
            return;
        }
        if (TextUtils.isEmpty(token)) {
            return;
        }
        RongIMUtil.connect(token);
        LogUtil.e(this, "isLogin = true");
        setIsLogin(true);
    }


    /**
     * 融云初始化
     */
    private void initRongIM() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            //扩展功能自定义
            InputProvider.ExtendProvider[] provider = {
                    new ImageInputProvider(RongContext.getInstance()),//图片
                    new CameraInputProvider(RongContext.getInstance()),//相机
            };
            RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);

            /**
             * 设置会话界面操作的监听器。
             */
            RongIM.setConversationBehaviorListener(new RongIMUtil.MyConversationBehaviorListener());
        }
    }

    /**
     * 返回用户  name,photo
     *
     * @param userId
     * @return
     */
    private UserInfo findUserById(String userId) {
        if (userId.equals(getUserBean().userid)) {
            UserBean userBean = getUserBean();
            return new UserInfo(userBean.userid, userBean.name, Uri.parse(userBean.photo));
        }
        for (ContactBean bean : mContactList) {
            if (userId.equals(bean.userid)) {
                UserInfo userInfo = new UserInfo(bean.userid, bean.name, Uri.parse(bean.photo));
                return userInfo;
            }
        }
        return null;
    }

    public void setContactList(ArrayList<ContactBean> conctactList) {
        mContactList.clear();
        mContactList.addAll(conctactList);
    }

    public ArrayList<ContactBean> getContactList() {
        return mContactList;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public AppAction getAppAction() {
        return appAction;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
        if (isLogin) {
            JPushInterface.resumePush(getApplicationContext());
        } else {
            JPushInterface.stopPush(getApplicationContext());
        }
    }

    public void setUserBean(UserBean userBean) {
        if (!TextUtils.isEmpty(userBean.username)) {
            SharedPreferenceUtil.putString(this, Constant.USERNAME, userBean.username);
        }
        if (!TextUtils.isEmpty(userBean.password)) {
            SharedPreferenceUtil.putString(this, Constant.PASSWORD, userBean.password);
        }
        if (!TextUtils.isEmpty(userBean.token)) {
            SharedPreferenceUtil.putString(this, Constant.TOKEN, userBean.token);
        }
        if (!TextUtils.isEmpty(userBean.userid)) {
            SharedPreferenceUtil.putString(this, Constant.USER_ID, userBean.userid);
        }
        if (!TextUtils.isEmpty(userBean.name)) {
            SharedPreferenceUtil.putString(this, Constant.NAME, userBean.name);
        }
        if (!TextUtils.isEmpty(userBean.photo)) {
            SharedPreferenceUtil.putString(this, Constant.IMAGE, userBean.photo);
        }
        if (!TextUtils.isEmpty(userBean.flag)) {
            SharedPreferenceUtil.putString(this, Constant.USER_INFO_FLAG, userBean.flag);
        }


    }

    public void clearUserBean() {
        RongIM.getInstance().logout();
        setIsLogin(false);
        SharedPreferenceUtil.remove(this, Constant.USERNAME);
        SharedPreferenceUtil.remove(this, Constant.PASSWORD);
        SharedPreferenceUtil.remove(this, Constant.TOKEN);
        SharedPreferenceUtil.remove(this, Constant.USER_ID);
        SharedPreferenceUtil.remove(this, Constant.NAME);
        SharedPreferenceUtil.remove(this, Constant.IMAGE);
        SharedPreferenceUtil.remove(this, Constant.USER_INFO_FLAG);
    }

    public UserBean getUserBean() {
        UserBean bean = new UserBean();
        bean.username = SharedPreferenceUtil.getString(this, Constant.USERNAME, "");
        bean.password = SharedPreferenceUtil.getString(this, Constant.PASSWORD, "");
        bean.userid = SharedPreferenceUtil.getString(this, Constant.USER_ID, "");
        bean.name = SharedPreferenceUtil.getString(this, Constant.NAME, "");
        bean.photo = SharedPreferenceUtil.getString(this, Constant.IMAGE, "");
        bean.token = SharedPreferenceUtil.getString(this, Constant.TOKEN, "");
        bean.flag = SharedPreferenceUtil.getString(this, Constant.USER_INFO_FLAG, "");
        return bean;
    }
}
