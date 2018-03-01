package com.i5i58.live.login.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.aop.permission.PermissionCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.db.User;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoad;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoadCallback;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.main.activity.MainActivity;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.account.AccByLogin;
import com.i5i58.live.model.entity.rnConfig.RNConfig;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 快捷登录界面
 * Created by Lee on 2017/4/7.
 */

@ContentView(R.layout.act_loginmain)
public class LoginMainActivity extends BaseActivity{

    private boolean hasPhonePermission = false;
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private AbortableFuture<LoginInfo> loginRequest;
    private PopupWindow popWindow;
    private ProgressDialog dialog;
    private final SnsPlatform[] platforms =
            {
                    SHARE_MEDIA.WEIXIN.toSnsPlatform(),
                    SHARE_MEDIA.QQ.toSnsPlatform(),
                    SHARE_MEDIA.SINA.toSnsPlatform()
            };

    @Event(value = R.id.loginmain_ll_phone)
    private void intentToLoginByPhone(View v){
        if(hasPhonePermission){
            Intent intent = new Intent(LoginMainActivity.this, LoginByPhoneActivity.class);
            intentAct(this,intent);
        }
    }

    @Event(value = R.id.sina)
    private void sina(View v){
        if(hasPhonePermission){
            UMShareAPI.get(this).getPlatformInfo(LoginMainActivity.this, platforms[2].mPlatform, authListener);
        }
    }

    @Event(value = R.id.qq)
    private void qq(View v){
        if(hasPhonePermission){
            UMShareAPI.get(this).getPlatformInfo(LoginMainActivity.this, platforms[1].mPlatform, authListener);
        }
    }

    @Event(value = R.id.weichat)
    private void weiChat(View v){
        if(hasPhonePermission){
            UMShareAPI.get(this).getPlatformInfo(LoginMainActivity.this, platforms[0].mPlatform, authListener);
        }
    }

    @PermissionCut(permission = {Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void getSerialNumber(){
        hasPhonePermission = true;
        try {
            TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);       //获取设备信息
            SystemCache.setSerialNumber(telephonemanage.getDeviceId().toString());
            initDb();
            checkRn();
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
            MobclickAgent.reportError(TigerApplication.getInstance(), e.toString());
        }
    }

    //清除授权
    private void cleanOauth(int index){
        UMShareAPI.get(this).deleteOauth(this, platforms[index].mPlatform, authListener);
    }

    private String getGender(String gender) {
        if (gender.equals("男") || gender.equals("m")) {
            return "0";
        } else {
            return "1";
        }
    }

    /**
     * 进入主界面
     */
    private void intentToMain(){
        intentAct(this,new Intent(LoginMainActivity.this, MainActivity.class));
        UserPreferences.saveLogin(true);
        closeAct(this);
    }

    private void loadingPopWindow() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_loading, null);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        ImageView imageView = (ImageView) popView.findViewById(R.id.pop_loading_img);
        imageView.setImageResource(com.handmark.pulltorefresh.library.R.drawable.drawable_waiting);
        final AnimationDrawable mAnimationDrawable = (AnimationDrawable) imageView.getDrawable();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopupAnimation);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                mAnimationDrawable.stop();
                popWindow = null;
            }
        });
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mAnimationDrawable.start();
    }

    /**
     * 登录云信
     */
    private void NimLogin(String account, String token){
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.d("Nim login success!");
                popWindow.dismiss();
                intentToMain();
            }

            @Override
            public void onFailed(int code) {
                LogUtil.d("Nim login failed!");
                popWindow.dismiss();
                if (code == 302 || code == 404) {
                    TSBError("code:" + code + "\n账号或密码错误");
                } else {
                    TSBError("code:" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                popWindow.dismiss();
            }
        });
    }

    private void thirdLogin(Map mMap, int third) {
        cleanOauth(third);
        String id;
        if (third == 2){
            id = mMap.get("uid").toString();
        }else {
            id = mMap.get("openid").toString();
        }
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.THIRDLOGIN)
                .addParam("third"       , String.valueOf(third))
                .addParam("thirdId"     , id)
                .addParam("name"        , mMap.get("name").toString())
                .addParam("face"        , mMap.get("iconurl").toString())
                .addParam("gender"      , getGender(mMap.get("gender").toString()))
                .addParam("device"      , SystemCache.DEVICE)
                .addParam("serialNum"   , SystemCache.getSerialNumber())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                JSONObject data = js.getJSONObject("data");
                                AccByLogin acc = GsonInner.getGsonInstance().fromJson(data.getJSONObject("acc").toString(),AccByLogin.class);
                                String accId = acc.getAccId();
                                String token = data.getString("token");
                                SystemCache.setAuthed(acc.isAuthed());
                                SysPreferences.saveIcon(acc.getFaceSmallUrl());
                                SysPreferences.saveOrg(acc.getFaceOrgUrl());
                                SysPreferences.saveLoginByPhone(false);
                                UserPreferences.saveAccId(accId);
                                UserPreferences.saveToken(token);
                                UserPreferences.saveFace(acc.getFaceSmallUrl());
                                UserPreferences.saveNickName(acc.getNickName());
                                UserPreferences.saveOpenId(acc.getOpenId());
                                LogUtil.d("third login success");
                                NimLogin(accId, token);
                            } else {
                                popWindow.dismiss();
                                TSBError("登陆出错：" + js.getString("msg"));
                            }
                        } catch (JSONException e) {
                            popWindow.dismiss();
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }catch (NullPointerException e){
                            popWindow.dismiss();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            LogUtil.d("platform >>> " + platform);
            if (null != data) {
                LogUtil.d("openid:" + data.get("openid"));
                LogUtil.d("uid:" + data.get("uid"));
                LogUtil.d("name:" + data.get("name"));
                LogUtil.d("iconurl:" + data.get("iconurl"));
                LogUtil.d("gender:" + data.get("gender"));
                if (!hasPhonePermission) {
                    TSBErrorLongtimeShow("未打开设备读取权限，无法登陆，请到系统设置中打开！");
                    return;
                }
                loadingPopWindow();
                switch (platform.toString()) {
                    case "WEIXIN":
                        thirdLogin(data, 0);
                        break;
                    case "QQ":
                        thirdLogin(data, 1);
                        break;
                    case "SINA":
                        thirdLogin(data, 2);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            TSBError("登录失败");
            LogUtil.e(t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            TSBError("已取消");
        }
    };

    /**
     * 检查Rn配置
     */
    private void checkRn() {
        final String filePath = SystemCache.BASE_PATH + SystemCache.RNFOLDER;
        String rnVersion = SysPreferences.getRnVersion();
        if (SysPreferences.getRnConfiger().equals("-1")){
            rnVersion = "rn";
        }
        if (!FileUtils.isExist(filePath + "/index.android.bundle")){
            rnVersion = "rn";
        }
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CHECKRN)
                .addParam("version", rnVersion)
                .addParam("device" , SystemCache.DEVICE)
                .addParam("main"   , SystemCache.MAIN)
                .addParam("sub"    , SystemCache.SUB )
                .addParam("func"   , SystemCache.FUNC)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject jsonObject, boolean success) {
                        try {
                            if (jsonObject.getString("code").equals("success")) {
                                if(jsonObject.has("data")){     //RN版本有更新
                                    final JSONObject data = jsonObject.getJSONObject("data");
                                    final RNConfig rnConfig = GsonInner.getGsonInstance().fromJson(data.toString(), RNConfig.class);
                                    final String rnConfigVersion = rnConfig.getRnConfigVersion();
                                    if (rnConfig.getRnZip() != null) {
                                        String url = API.OSS_URL_RN + rnConfig.getRnZip() + ".zip";
                                        XUtilsDownLoad.downloadOneFile(url, filePath + ".zip", new XUtilsDownLoadCallback() {
                                                    @Override
                                                    public void success() {
                                                        SysPreferences.saveRnVersion(rnConfigVersion);
                                                        SysPreferences.saveRnConfiger(data.toString());
                                                        SystemCache.setRnConfig(rnConfig);
                                                        Toast.makeText(SystemCache.getContext(), "下载成功", Toast.LENGTH_SHORT).show();
                                                        try {
                                                            File file = new File(filePath + ".zip");
                                                            FileUtils.extractZipFile(file, filePath);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                }else {
                                    if(!"-1".equals(SysPreferences.getRnConfiger())){
                                        RNConfig rnConfig = GsonInner.getGsonInstance().fromJson(SysPreferences.getRnConfiger(), RNConfig.class);
                                        SystemCache.setRnConfig(rnConfig);
                                    }
                                }
                            } else {
                                TSBError(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_PERMISSION_REQ_CODE){
            boolean isAllowed = false;
            for(int i = 0;i < grantResults.length;i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.e("允许权限：" + permissions[i]);
                    isAllowed = true;
                }
            }
            if(isAllowed){
                getSerialNumber();
            }else{
                TSBErrorLongtimeShow("缺少权限");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);

        getSerialNumber();
        UserPreferences.clearPreference();

        dialog = new ProgressDialog(this);
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                SocializeUtils.safeShowDialog(dialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize succeed", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize onError", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize onCancel", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }




    private void initDb(){
        if (SysPreferences.isFirst()){//首次启动
            SystemCache.setIsFirst(false);
            SysPreferences.saveFirst(false);
            FileUtils.deleteFolderFile(SystemCache.BASE_PATH + SystemCache.ROOT_PATH,true);
        }
        if (FileUtils.isExist(SystemCache.BASE_PATH + SystemCache.ROOT_PATH + SystemCache.DB)){
            return;
        }
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("phLive.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File(SystemCache.DB)) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //数据库升级
                    }
                });
        DbManager dbManager = x.getDb(daoConfig);
        TigerApplication.getInstance().setDbManager(dbManager);
        try {
            List list = dbManager.findAll(User.class);
            if(null != list){
                LogUtil.d(list.size()+"");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
