package com.i5i58.live.model.entity.sys;

import android.content.Context;
import android.os.Environment;

import com.i5i58.live.model.entity.account.PersonalMsg;
import com.i5i58.live.model.entity.rnConfig.RNConfig;

import java.io.File;

/**
 * Created by Lee on 2017/4/10.
 */

public class SystemCache {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    public static final String ROOT_PATH = "/PHLive";
    public static final String NIMLOG_PATH = "/com.i5i58.live/nim/log/";
    public static final String RNFOLDER = ROOT_PATH + "/React";
    public static final String QRCODE = ROOT_PATH + "/myQrCode";
    public static final String GIFT = ROOT_PATH + "/Gifts";
    public static final String MOUNT = ROOT_PATH + "/Mounts";
    public static final String DB = "/sdcard/PHLive/DB";

    private static String       versionName;             //版本号（a.a.a）
    public static String        MAIN;                    //主
    public static String        SUB;                     //次
    public static String        FUNC;                    //功能
    public static String        DEVICE = "4";            //设备
    private static int          screenWith;              //屏幕宽度
    private static int          screenHeight;            //屏幕高度
    private static int          stateHeight;             //状态栏高度
    private static Context      context;                 //Context
    private static RNConfig     rnConfig;                //RN配置
    private static String       serialNumber = "";       //设备号
    private static PersonalMsg  personalMsg;             //用户个人信息
    private static String       shareUrl;                //分享地址
    private static boolean      isFirst;                 //是否第一次打开app
    private static boolean      authed;                  //是否主播
    private static int          statusBarHeight;         //状态栏高度
    private static String       pohonVersion;            //手机型号
    private static String       SDKVersion;              //SDK版本
    private static String       systemVersion;           //系统版本
    private static boolean      zegoInited;              //初始化ZEGO


    public static boolean isZegoInited() {
        return zegoInited;
    }

    public static void setZegoInited(boolean zegoInited) {
        SystemCache.zegoInited = zegoInited;
    }

    public static String getPhoneVersion() {
        return pohonVersion;
    }

    public static void setPhoneVersion(String pohonVersion) {
        SystemCache.pohonVersion = pohonVersion;
    }

    public static String getSystemVersion() {
        return systemVersion;
    }

    public static void setSystemVersion(String systemVersion) {
        SystemCache.systemVersion = systemVersion;
    }

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    public static void setStatusBarHeight(int statusBarHeight) {
        SystemCache.statusBarHeight = statusBarHeight;
    }

    public static String getShareUrl() {
        return shareUrl;
    }

    public static void setShareUrl(String shareUrl) {
        SystemCache.shareUrl = shareUrl;
    }

    public static PersonalMsg getPersonalMsg() {
        return personalMsg;
    }

    public static void setPersonalMsg(PersonalMsg personalMsg) {
        SystemCache.personalMsg = personalMsg;
    }

    public static String getSerialNumber() {
        return serialNumber;
    }

    public static void setSerialNumber(String serialNumber) {
        SystemCache.serialNumber = serialNumber;
    }

    public static RNConfig getRnConfig() {
        return rnConfig;
    }

    public static void setRnConfig(RNConfig rnConfig) {
        SystemCache.rnConfig = rnConfig;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static void setVersionName(String versionName) {
        SystemCache.versionName = versionName;
        String[] version = versionName.split("\\.");
        SystemCache.MAIN        = version[0];
        SystemCache.SUB         = version[1];
        SystemCache.FUNC        = version[2];
    }

    public static int getScreenWith() {
        return screenWith;
    }

    public static void setScreenWith(int screenWith) {
        SystemCache.screenWith = screenWith;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        SystemCache.screenHeight = screenHeight;
    }

    public static int getStateHeight() {
        return stateHeight;
    }

    public static void setStateHeight(int stateHeight) {
        SystemCache.stateHeight = stateHeight;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SystemCache.context = context.getApplicationContext();
    }

    public static boolean isFirst() {
        return isFirst;
    }

    public static void setIsFirst(boolean isFirst) {
        SystemCache.isFirst = isFirst;
    }

    public static boolean isAuthed() {
        return authed;
    }

    public static void setAuthed(boolean authed) {
        SystemCache.authed = authed;
    }

    public static void clear(){
        rnConfig = null;
        personalMsg = null;
    }

}
