package com.i5i58.live.common.manager;


import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理类
 * Created by Lee on 2017/4/7.
 */
public class APPManager {

    private static Stack<Activity> activityStack;

    private static APPManager instance;

    private APPManager() {

    }

    /**
     * 单一实例
     */
    public static APPManager getInstance() {
        if (instance == null) {
            instance = new APPManager();
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
    public void finishLastActivity() {
        Activity activity = activityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void removeClass(Class<?> cls) {
        if (null == activityStack || activityStack.size() == 0) return;
        for (Activity activity : activityStack) {
            if (activityStack.pop().getClass().equals(cls)) {
                removeActivity(activityStack.pop());
            }

        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结并只保留最后一个Activity
     */
    public void SaveLastActivity() {
        for (int i = 0, size = activityStack.size()-1; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager.restartPackage(context.getPackageName());
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

