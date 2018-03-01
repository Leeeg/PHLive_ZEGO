package com.i5i58.live;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Lee on 2017/7/25.
 */

public class Alarmreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        if (intent.getAction().equals("arui.alarm.action")) {
//            if (!isServiceWork(context, "com.umeng.message.UmengMessageBootReceiver")) {
//                // 友盟service
//                Intent n = new Intent();
//                n.setClass(context, UmengMessageBootReceiver.class);
//                context.startService(n);
//            }
//        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}

