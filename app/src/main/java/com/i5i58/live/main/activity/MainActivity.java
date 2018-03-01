package com.i5i58.live.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.ZegoApiManager;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.aop.permission.PermissionCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.enums.AppUpdateEnum;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.utils.system.SystemUtil;
import com.i5i58.live.common.view.viewPager.NoHorizontalScrollViewpager;
import com.i5i58.live.home.activity.ZegoAnchorActivity;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.main.HomeMainFragment;
import com.i5i58.live.login.activity.LoginMainActivity;
import com.i5i58.live.mine.main.MineFragment;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.AppUpdate;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.upDate.UpdateAgent;
import com.i5i58.live.upDate.UpdateInfo;
import com.i5i58.live.upDate.UpdateManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lee on 2017/4/14.
 */

@ContentView(R.layout.act_main)
public class MainActivity extends BaseActivity {

    private BaseFragmentAdapter baseAdapter;
    private long exitTime = 0;

    private String mUpdateUrl;
    private String mCheckUrl = "000";
    private String updateContent;
    private String newVersionName;
    private int versionCode;
    private long size;

    @ViewInject(R.id.main_pager)
    private NoHorizontalScrollViewpager mainPager;

    @ViewInject(R.id.tab_bottom_home)
    private ImageView homeBt;

    @ViewInject(R.id.tab_bottom_mine)
    private ImageView mineBt;

    @Event(R.id.main_ll_home)
    private void homeClick(View v){
        mainPager.setCurrentItem(0);
        homeBt.setImageDrawable(getResources().getDrawable(R.drawable.bt_table_bottom_home_yes));
        mineBt.setImageDrawable(getResources().getDrawable(R.drawable.bt_table_bottom_mine_no));
    }

    @Event(R.id.main_ll_mine)
    private void mineClick(View v){
        mainPager.setCurrentItem(1);
        homeBt.setImageDrawable(getResources().getDrawable(R.drawable.bt_table_bottom_home_no));
        mineBt.setImageDrawable(getResources().getDrawable(R.drawable.bt_table_bottom_mine_yes));
    }

    @Event(R.id.main_ibt_startlive)
    private void startLive(View v){
        checkAnchor();
    }

    /**
     * 开始直播
     */
    private void checkAnchor() {
//        if (SystemCache.getPersonalMsg().getAccount().isAnchor()) {
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.GETMYPUSHMOBILE)
                    .addParam("device", SystemCache.DEVICE)
                    .addParam("serialNum", SystemCache.getSerialNumber())
                    .addParam("model", SystemCache.getPhoneVersion())
                    .addParam("osVersion", SystemCache.getSystemVersion())
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                    JSONObject data = js.getJSONObject("data");
                                    if (data.has("pushUrl")) {
                                        LiveRoomCache.setRoomId(data.getString("rId"));
                                        LiveRoomCache.setChannelId(data.getString("cId"));

                                        boolean tbEnableFrontCam = true;//开启前置摄像头
                                        boolean tbEnableTorch = false;//开启手电筒
                                        int mSelectedBeauty = 0;//美颜
                                        int mSelectedFilter = 0;//滤镜
                                        ZegoAnchorActivity.actionStart(MainActivity.this,
                                                "Title_Tiger",
                                                tbEnableFrontCam,
                                                tbEnableTorch,
                                                mSelectedBeauty,
                                                mSelectedFilter,
                                                getWindowManager().getDefaultDisplay().getRotation(),
                                                data,
                                                data.getString("cId"));
                                    }
                                }else {
                                    TSBSuccess(js.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//        } else {
//            TSBSuccess("还不是主播,请到胖虎娱乐官网认证成为主播！");
//        }
    }

    private void initViewPager(){
        List<Fragment> fragmentList = new ArrayList();
        fragmentList.add(new HomeMainFragment());
        fragmentList.add(new MineFragment());
        baseAdapter = new BaseFragmentAdapter(getSupportFragmentManager(),fragmentList);
        mainPager.setAdapter(baseAdapter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击退出！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                registerObservers(false);
                ZegoApiManager.getInstance().releaseSDK();
                TigerApplication.getInstance().isActive = false;
                appManager.AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);

//        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(sysMessageObserver, register);
//        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);

    }
//
//    private Observer<SystemMessage> sysMessageObserver = new Observer<SystemMessage>() {
//        @Override
//        public void onEvent(SystemMessage systemMessage) {
//            LogUtil.d("System message: " + systemMessage.getContent());
//        }
//    };
//
//    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
//        @Override
//        public void onEvent(Integer unreadCount) {
//            LogUtil.d("System message unreadCount: " + unreadCount);
//        }
//    };

    private void init(){
        registerObservers(true);
        initViewPager();
        TigerApplication.getInstance().isActive = true;
        if (null == TigerApplication.getInstance().getDbManager()){
            LogUtil.d("init  db !");
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
        }
    }

    private void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int notifyId) {
        UpdateManager.create(this).setUrl(mCheckUrl).setManual(isManual).setNotifyId(notifyId).setParser(new UpdateAgent.InfoParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;         //有更新
                info.updateContent = updateContent;
                info.versionCode = versionCode;
                info.versionName = newVersionName;
                info.url = mUpdateUrl;
                info.md5 = "56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = size;                   //大小
                info.isForce = isForce;             //强制
                info.isIgnorable = isIgnorable;     //忽略
                info.isSilent = isSilent;           //静默
                return info;
            }

            @Override
            public void updateLater() {

            }
        }).check();
    }

    private void getPlatformConfig(){
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETPLATFROMCONFIG)
                .addParam("device" , SystemCache.DEVICE)
                .addParam("main"   , SystemCache.MAIN)
                .addParam("sub"    , SystemCache.SUB )
                .addParam("func"   , SystemCache.FUNC)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                JSONObject data = js.getJSONObject("data");
                                AppUpdate appUpdate = GsonInner.getGsonInstance().fromJson(data.toString(),AppUpdate.class);
                                LogUtil.d("can init ZegoSDK : " + TigerApplication.getInstance().inMainProcess() + "");
                                // 初始化sdk
                                ZegoApiManager.getInstance().initSDK(appUpdate.getZegoKey(), appUpdate.getZegoRtmpSignKey());
                                updateContent = "• 修复部分bug！;\n• 增强用户体验！";
                                size = 1024*1000*40;
                                if (appUpdate.getAppUpdate() == AppUpdateEnum.NOUPDATE) {//不需要更新
                                    return;
                                } else if (appUpdate.getAppUpdate() == AppUpdateEnum.ENABLEUPDATE) {
                                    mUpdateUrl = appUpdate.getUpdateUrl();
                                    versionCode = 115;
                                    newVersionName = "胖虎直播1.1.9";
                                    check(true, true, false, false, false, 998);
                                }else if(appUpdate.getAppUpdate() == AppUpdateEnum.FORCEUPDATE){
                                    mUpdateUrl = appUpdate.getUpdateUrl();
                                    versionCode = 115;
                                    newVersionName = "胖虎直播1.1.9";
                                    check(true, true, true, false, false, 998);
                                }
                            } else {
                                TSBError(js.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Jump Start Interface
     * 提示是否跳转设置自启动界面
     */
    private void jumpStartInterface() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("请进入自启动管理页面打开胖虎直播自启动权限，\n否则将无法接收后台消息推送！");
            builder.setPositiveButton("立即设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SystemUtil.jumpStartInterface(MainActivity.this);
                        }
                    });
            builder.setNegativeButton("暂不设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);
        appManager.addActivity(this);
        SystemCache.setStatusBarHeight(SystemUtil.getStatusBarHeight(getApplicationContext()));
        init();
        getPlatformConfig();
        getSerialNumber();
        PushAgent.getInstance(this).getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                LogUtil.d("Umeng setTag : " + isSuccess);
            }
        }, UserPreferences.getAccId());
        if (SysPreferences.isPushAble()){
            jumpStartInterface();
            SysPreferences.savePushAble(false);
        }
    }

    @PermissionCut(permission = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void getSerialNumber(){
        try {
            TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);       //获取设备信息
            SystemCache.setSerialNumber(telephonemanage.getDeviceId().toString());
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
            MobclickAgent.reportError(TigerApplication.getInstance(), e.toString());
        }
    }

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            if (onlineClients == null || onlineClients.size() == 0) {
            } else {
                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
                    case ClientType.Windows:
                        TSBSuccess("您的账号在电脑登录！");
                        break;
                    case ClientType.Web:
                        TSBSuccess("您的账号在网页登录！");
                        break;
                    case ClientType.iOS:
                    case ClientType.Android:
                        TSBSuccess("您的账号在移动端登录！");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            String msg = "";
            if (code == StatusCode.KICKOUT){
                msg = "您的账号被迫下线!";
            }else if (code == StatusCode.PWD_ERROR){
                msg = "账号或密码有误!";
            }else if (code == StatusCode.KICK_BY_OTHER_CLIENT){
                msg = "您的账号已在其他手机登录!";
            }else if (code == StatusCode.FORBIDDEN){
                msg = "您的账号已被禁用!";
            }else {
                return;
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            UserPreferences.saveLogin(false);

            Intent broad = new Intent(WelcomeActivity.action);
            sendBroadcast(broad);

            SystemCache.clear();

            Intent intent = new Intent(MainActivity.this, LoginMainActivity.class);
            intentAct(MainActivity.this,intent);
            closeAct(MainActivity.this);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
