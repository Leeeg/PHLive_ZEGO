package com.i5i58.live.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.i5i58.live.model.entity.sys.SystemCache;

/**
 * Created by Lee on 2017/4/10.
 */

public class SysPreferences {

    private static final String IS_FIRST                     = "isFirst";
    private static final String PUSHABLE                     = "pushAble";
    private static final String VERSION_RN                   = "rnVersion";
    private static final String CONFIGER_RN                  = "rnConfiger";
    private static final String PHONE                        = "phone";
    private static final String ICON                         = "icon";
    private static final String GIFTVERSION                  = "giftVersion";
    private static final String MOUNTVERSION                 = "mountVersion";
    private static final String ANIMATIONVERSION             = "animationVersion";
    private static final String ANIMATIONZIP                 = "animationZipConfig";
    private static final String ANIMATIONRES                 = "animationResConfig";
    private static final String GIFTCONGFIG                  = "giftConfig";
    private static final String MOUNTCONFIG                  = "mountConfig";
    private static final String NODE                         = "node";
    private static final String ORG                          = "org";
    private static final String LOGINBYPHONE                 = "loginByPhone";


    public static boolean isPushAble() {
        return getBooleF(PUSHABLE);
    }

    public static void savePushAble(boolean pushAble) {
        saveBoole(PUSHABLE, pushAble);
    }

    public static boolean isLoginByPhone() {
        return getBooleF(LOGINBYPHONE);
    }

    public static void saveLoginByPhone(boolean isLoginByPhone) {
        saveBoole(LOGINBYPHONE, isLoginByPhone);
    }

    public static boolean isFirst() {
        return getBooleF(IS_FIRST);
    }

    public static void saveFirst(boolean isFirst) {
        saveBoole(IS_FIRST, isFirst);
    }

    public static void saveRnConfiger(String rnConfiger) {
        saveString(CONFIGER_RN, rnConfiger);
    }

    public static String getRnConfiger() {
        return getString(CONFIGER_RN);
    }

    public static String getRnVersion() {
        return getString(VERSION_RN);
    }

    public static void saveRnVersion(String rnVersion) {
        saveString(VERSION_RN, rnVersion);
    }

    public static String getPhone() {
        return getString(PHONE);
    }

    public static void savePhone(String phone) {
        saveString(PHONE, phone);
    }

    public static String getIcon() {
        return getString(ICON);
    }

    public static void saveIcon(String icon) {
        saveString(ICON, icon);
    }
    public static String getOrg() {
        return getString(ORG);
    }

    public static void saveOrg(String org) {
        saveString(ORG, org);
    }

    public static String getGiftVersion() {
        return getString(GIFTVERSION);
    }

    public static void saveGiftVersion(String giftVersion) {
        saveString(GIFTVERSION, giftVersion);
    }

    public static String getMountVersion() {
        return getString(MOUNTVERSION);
    }

    public static void saveMountVersion(String mountVersion) {
        saveString(MOUNTVERSION, mountVersion);
    }

    public static String getAnimationVersion() {
        return getString(ANIMATIONVERSION);
    }

    public static void saveAnimationVersion(String animationVersion) {
        saveString(ANIMATIONVERSION, animationVersion);
    }

    public static String getAnimationZip() {
        return getString(ANIMATIONZIP);
    }

    public static void saveAnimationZip(String animationZipConfig) {
        saveString(ANIMATIONZIP, animationZipConfig);
    }

    public static String getAnimationRes() {
        return getString(ANIMATIONRES);
    }

    public static void saveAnimationRes(String animationResConfig) {
        saveString(ANIMATIONRES, animationResConfig);
    }

    public static String getGiftConfig() {
        return getString(GIFTCONGFIG);
    }

    public static void saveGiftConfig(String giftConfig) {
        saveString(GIFTCONGFIG, giftConfig);
    }

    public static String getMountConfig() {
        return getString(MOUNTCONFIG);
    }

    public static void saveMountConfig(String mountConfig) {
        saveString(MOUNTCONFIG, mountConfig);
    }

    public static String getNode() {
        return getString(NODE);
    }

    public static void saveNode(String node) {
        saveString(NODE, node);
    }



    private static void saveBoole(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBoole(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    private static boolean getBooleF(String key) {
        return getSharedPreferences().getBoolean(key, true);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, "-1");
    }

    public static void clearPreference(){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }

    static SharedPreferences getSharedPreferences() {
        return SystemCache.getContext().getSharedPreferences("Tiger", Context.MODE_PRIVATE);
    }
}
