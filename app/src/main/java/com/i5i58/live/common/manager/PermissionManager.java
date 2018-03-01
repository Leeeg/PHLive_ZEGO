package com.i5i58.live.common.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限检查Manager
 * Created by Lee on 2017/4/11.
 */

public class PermissionManager {

    private String TAG = "PermissionManager";
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private static volatile PermissionManager permissionManager;

    public PermissionManager(){}

    //DCL单例模式
//    public static PermissionManager getInstance(){
//        if (permissionManager == null){
//            synchronized (PermissionManager.class){
//                if (permissionManager == null){
//                    permissionManager = new PermissionManager();
//                }
//            }
//        }
//        return permissionManager;
//    }

    private static class InnerInsatance{
        public static final PermissionManager instance = new PermissionManager();
    }

    //内部类单例模式
    public static PermissionManager getInnerInstance(){
        synchronized (PermissionManager.class){
            return InnerInsatance.instance;
        }
    }

    /**
     * 查询权限
     * @param context
     * @param permissions
     * @return
     */
    public String[] checkPermission(Context context, String[] permissions){
        List list = new ArrayList();
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission: permissions){
                Log.i(TAG, "检查的权限：" + permission);
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission);
                }
            }
            String[] needPermissions = (String[]) list.toArray(new String[list.size()]);
            return needPermissions;
        }
        return null;
    }

    /**
     * 请求权限
     * @param permissions 请求的权限
     */
    public void askPermission(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, WRITE_PERMISSION_REQ_CODE);
    }

}
