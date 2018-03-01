package com.i5i58.live.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.home.activity.MainAudienceActivity;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.login.activity.LoginMainActivity;
import com.i5i58.live.model.entity.rnConfig.RNConfig;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 启动界面
 * Created by Lee on 2017/4/7.
 */

public class WelcomeActivity extends BaseActivity{

    public static final String action = "com.WelcomeActivity.action";
//    private TextView versionTxt;

    /**
     * 登录云信
     */
    private void NimLogin(String account, String token){
        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intentAct(WelcomeActivity.this,intent);
                closeAct(WelcomeActivity.this);
            }

            @Override
            public void onFailed(int code) {
                TSBError("请登录");
                Intent intent = new Intent(WelcomeActivity.this, LoginMainActivity.class);
                intentAct(WelcomeActivity.this,intent);
                closeAct(WelcomeActivity.this);
            }

            @Override
            public void onException(Throwable exception) {
                TSBError("请登录");
                Intent intent = new Intent(WelcomeActivity.this, LoginMainActivity.class);
                intentAct(WelcomeActivity.this,intent);
                closeAct(WelcomeActivity.this);
            }
        });
    }

    private void checkLogin(){
        if(UserPreferences.isLogined()){
            NimLogin(UserPreferences.getAccId(), UserPreferences.getToken());
        }else{
            try {
                Thread.sleep(2000);
                Intent intent = new Intent(WelcomeActivity.this, LoginMainActivity.class);
                intentAct(this,intent);
                closeAct(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getChannelIndex(List<ChannelData> channels, String cId){
        int index = 0;
        for (int i = 0; i < channels.size() ; i++) {
            if (channels.get(i).getCId().equals(cId)){
                index = i;
            }
        }
        return index;
    }

    /**
     * web or share 进入直播间
     */
    private boolean channelOpenThird() {
        try {
            final Intent intent = getIntent();
            if (null != intent.getAction() && intent.getAction().equals("android.intent.action.MAIN")) return true;
            if (null != intent) {
                if (TigerApplication.getInstance().isLiving) {
                    Toast.makeText(this, "您正在直播，无法进入其他直播间！", Toast.LENGTH_SHORT).show();
                    finish();
                    return false;
                }
                TigerApplication.getInstance().isFromWeb = true;
                final Intent intentE = new Intent(WelcomeActivity.this, MainAudienceActivity.class);
                String cId;
                String CoverUrl;
                String RoomId;
                String HttpPullUrl;
                if (intent.getBooleanExtra("isNotification", false)){
                    cId          = intent.getStringExtra("cId");
                    CoverUrl     = intent.getStringExtra("CoverUrl");
                    RoomId       = intent.getStringExtra("RoomId");
                    HttpPullUrl  = intent.getStringExtra("HttpPullUrl");
                }else {
                    String uri = intent.getData().toString();
                    String params = uri.substring(13);
                    String param[] = params.split("&");
                    Log.i("webToApp", params);
                    cId          = param[0].substring(4);
                    CoverUrl     = param[1].substring(9);
                    RoomId       = param[2].substring(10);
                    HttpPullUrl  = param[3].substring(12);
                }
                intentE.putExtra("position", 0);
                Bundle bundle = new Bundle();
                ChannelData channel = new ChannelData();
                channel.setCId(cId);
                channel.setHttpPullUrl(HttpPullUrl);
                channel.setCoverUrl(CoverUrl);
                channel.setYunXinRId(RoomId);
                LiveRoomCache.setChannelData(channel);
                List<ChannelData> channels = new ArrayList<>();
                channels.add(channel);
                bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
                intentE.putExtras(bundle);

                if (TigerApplication.getInstance().isActive) {
                    if (TigerApplication.getInstance().isInRoom) {
                        LogUtil.d("isInRoom");
                        Toast.makeText(this, "您已经在直播间！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        startActivity(intentE);
                        finish();
                    }
                    return false;
                }
                return true;
            }
            return true;
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
            return true;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean goOn = channelOpenThird();
        if (goOn){
            appManager.addActivity(this);
            setContentView(R.layout.act_welcome);
            if(!"-1".equals(SysPreferences.getRnConfiger())){
                RNConfig rnConfig = GsonInner.getGsonInstance().fromJson(SysPreferences.getRnConfiger(), RNConfig.class);
                SystemCache.setRnConfig(rnConfig);
            }

            checkLogin();
        }
    }

}
