package com.i5i58.live.common.utils.system;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Lee on 2017/4/7.
 */

public class IntsallUtil {

    private static final String TAG = "InstallUtil";
    private static int versionCode;
    private static String versionName;

    /**
     * 是否已安装app
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            if (TextUtils.isEmpty(packageName))
                return false;
            return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return false;
        }
    }

    /**
     * 打开app
     * @param packageName
     * @param context
     */
    public static void openApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null)
            context.startActivity(intent);
    }

    /**
     * 某个app的版本号，未安装时返回null
     */
    public static final String getVersionName(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null) {
                return pi.versionName;
            } else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static final int getVersionCode(Context context) {
        if (versionCode == 0) {
            loadVersionInfo(context);
        }

        return versionCode;
    }

    /**
     * 易信版本号
     */
    public static final String getVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            loadVersionInfo(context);
        }

        return versionName;
    }

    private static final void loadVersionInfo(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pi != null) {
                versionCode = pi.versionCode;
                versionName = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装apk文件
     */
    public static Intent getInstallApkIntent(String filepath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(filepath);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }
}
