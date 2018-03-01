package com.i5i58.live.common.utils.system;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.i5i58.live.model.entity.sys.SystemCache;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/7.
 */

public class SystemUtil {

    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;
        // APPManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }
            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取屏幕信息
     */
    public static void getScreen(Context context) {
        // 1.获取屏幕的宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        SystemCache.setScreenWith(width);
        SystemCache.setScreenHeight(height);
        // 2.获取状态栏的高度
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        SystemCache.setStateHeight(statusBarHeight);
        // 3.计算屏幕中心点的坐标
        int centerX = width / 2;
        int centerY = (height - statusBarHeight) / 2;
    }


    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getHandSetInfo(){
        String handSetInfo=
                        "手机型号:" + android.os.Build.MODEL +
                        ",SDK版本:" + android.os.Build.VERSION.SDK +
                        ",系统版本:" + android.os.Build.VERSION.RELEASE;
        SystemCache.setPhoneVersion(android.os.Build.MODEL);
        SystemCache.setSystemVersion(android.os.Build.VERSION.RELEASE);
        return handSetInfo;
    }

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * [获取cpu类型和架构]
     *
     * @return
     * 三个参数类型的数组，第一个参数标识是不是ARM架构，第二个参数标识是V6还是V7架构，第三个参数标识是不是neon指令集
     */
//    public static Object[] getCpuArchitecture() {
//
//        if ((Integer) mArmArchitecture[1] != -1) {
//            return mArmArchitecture;
//        }
//        try {
//            InputStream is = new FileInputStream("/proc/cpuinfo");
//            InputStreamReader ir = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(ir);
//            try {
//                String nameProcessor = "Processor";
//                String nameFeatures = "Features";
//                String nameModel = "model name";
//                String nameCpuFamily = "cpu family";
//                while (true) {
//                    String line = br.readLine();
//                    String[] pair = null;
//                    if (line == null) {
//                        break;
//                    }
//                    pair = line.split(":");
//                    if (pair.length != 2)
//                        continue;
//                    String key = pair[0].trim();
//                    String val = pair[1].trim();
//                    if (key.compareTo(nameProcessor) == 0) {
//                        String n = "";
//                        for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {
//                            String temp = val.charAt(i) + "";
//                            if (temp.matches("\\d")) {
//                                n += temp;
//                            } else {
//                                break;
//                            }
//                        }
//                        mArmArchitecture[0] = "ARM";
//                        mArmArchitecture[1] = Integer.parseInt(n);
//                        continue;
//                    }
//
//                    if (key.compareToIgnoreCase(nameFeatures) == 0) {
//                        if (val.contains("neon")) {
//                            mArmArchitecture[2] = "neon";
//                        }
//                        continue;
//                    }
//
//                    if (key.compareToIgnoreCase(nameModel) == 0) {
//                        if (val.contains("Intel")) {
//                            mArmArchitecture[0] = "INTEL";
//                            mArmArchitecture[2] = "atom";
//                        }
//                        continue;
//                    }
//
//                    if (key.compareToIgnoreCase(nameCpuFamily) == 0) {
//                        mArmArchitecture[1] = Integer.parseInt(val);
//                        continue;
//                    }
//                }
//            } finally {
//                br.close();
//                ir.close();
//                is.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return mArmArchitecture;
//    }


    public static boolean isMyAppLauncherDefault(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        // the packageName of your application
        String packageName = context.getPackageName();
        List<ComponentName> preferredActivities = new ArrayList<ComponentName>();
        final PackageManager packageManager = context.getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, preferredActivities, packageName);

        if (preferredActivities != null && preferredActivities.size()> 0) {
            return true;
        }
        return false;
    }

    /**
     * Get Mobile Type
     *
     * @return
     */
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * GoTo Open Self Setting Layout
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    /***
     * encode by Base64
     */
    public static String encodeBase64(byte[]input) throws Exception{
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod= clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj=mainMethod.invoke(null, new Object[]{input});
        return (String)retObj;
    }
    /***
     * decode by Base64
     */
    public static byte[] decodeBase64(String input) throws Exception{
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod= clazz.getMethod("decode", String.class);
        mainMethod.setAccessible(true);
        Object retObj=mainMethod.invoke(null, input);
        return (byte[])retObj;
    }

    // 解密
    public static byte[] getFromBase64(String str) {
        byte[] result = null;
        if (str != null) {
            try {
                result = Base64.decode(str, Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("init ZEGO SDK error by decodeBase64 !");
            }
        }
        return result;
    }
}
