package com.htlc.cyjk.app.util;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;

/**
 * activity堆栈式管理
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月30日 下午6:22:05
 * 
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {}

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                LogUtil.e(this,"size="+activityStack.size()+";"+activityStack.get(i).getClass().getSimpleName()+"current:"+i);
            }
        }

    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 移除指定Activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        activityStack.remove(activity);
    }

    /**
     * 返回到指定的Activity
     * @param activity
     */
    public void backToActivity(Activity activity){
        if(activity == null){
            return;
        }
        if(activityStack.size()>0){
            if(activityStack.lastElement() != activity){
                activityStack.lastElement().finish();
                LogUtil.e(this, activityStack.lastElement()+"");
                activityStack.remove(activityStack.lastElement());
                backToActivity(activity);
            }
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束之前的所有activity
     */
    public void finishBeforeActivity(){
        if(activityStack == null) return;
        while (activityStack.firstElement() != activityStack.lastElement()){
            finishActivity(activityStack.firstElement());
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
                break;
            }
        }
        activityStack.clear();
    }


    /**
     * 获取指定的Activity
     * 
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }



    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}