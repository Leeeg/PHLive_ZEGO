package com.i5i58.live.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.i5i58.live.model.entity.sys.SystemCache;

/**
 * Created by Lee on 2017/4/10.
 */

public class UserPreferences {

    private static final String IS_LOGINED      = "isLogined";
    private static final String LOGINVERSION    = "loginVersion";
    private static final String ACCID           = "accId";
    private static final String TOKEN           = "token";
    private static final String NAME            = "nickName";
    private static final String FACE            = "faceUrl";
    private static final String OPENID          = "openId";
    private static final String YXTOKEN         = "yxToken";




    public static boolean isLogined() {
        return getBoole(IS_LOGINED);
    }

    public static void saveLogin(boolean isLogined) {
        saveBoole(IS_LOGINED, isLogined);
    }


    public static String getOpenId() {
        return getString(OPENID);
    }

    public static void saveOpenId(String openId) {
        saveString(OPENID, openId);
    }

    public static String getFace() {
        return getString(FACE);
    }

    public static void saveFace(String faceUrl) {
        saveString(FACE, faceUrl);
    }

    public static String getNickName() {
        return getString(NAME);
    }

    public static void saveNickName(String nickName) {
        saveString(NAME, nickName);
    }


    public static String getAccId() {
        return getString(ACCID);
    }

    public static void saveAccId(String accId) {
        saveString(ACCID, accId);
    }

    public static String getToken() {
        return getString(TOKEN);
    }

    public static void saveToken(String token) {
        saveString(TOKEN, token);
    }

    public static String getYxToken() {
        return getString(YXTOKEN);
    }

    public static void saveYxToken(String yxToken) {
        saveString(YXTOKEN, yxToken);
    }

    public static String getLoginVersion() {
        return getString(LOGINVERSION);
    }

    public static void saveLoginVersion(String loginVersion) {
        saveString(LOGINVERSION, loginVersion);
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
        return SystemCache.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
    }
}
