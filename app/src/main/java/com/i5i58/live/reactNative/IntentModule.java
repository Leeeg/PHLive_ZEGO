package com.i5i58.live.reactNative;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.home.activity.MainAudienceActivity;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.model.entity.sys.SystemCache;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class IntentModule extends ReactContextBaseJavaModule {

    public IntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "IntentModule";
    }

    /**
     * Activtiy跳转到JS页面，传输数据
     */
    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            Log.e("currentActivity", currentActivity.toString());
            String msg;
            String accId = currentActivity.getIntent().getStringExtra("accId");
            String token = currentActivity.getIntent().getStringExtra("token");
            if (null != currentActivity.getIntent().getStringExtra("cId")) {
                String cId = currentActivity.getIntent().getStringExtra("cId");
                msg = accId + "|" + token + "|" + cId;
            } else if (null != currentActivity.getIntent().getStringExtra("ta")) {
                String ta = currentActivity.getIntent().getStringExtra("ta");
                msg = accId + "|" + token + "|" + ta;
            } else {
                msg = accId + "|" + token;
            }
            Log.e("currentActivity", accId + "======" + token);
            if (TextUtils.isEmpty(accId) && TextUtils.isEmpty(token)) {
                accId = "没有数据";
                token = "没有数据";
            }
            successBack.invoke(msg);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }

    /**
     * 从JS页面跳转到原生activity
     */
    @ReactMethod
    public void startActivityFromJS(String name) {
        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
                Class toActivity = Class.forName(name);
                Intent intent = new Intent(currentActivity, toActivity);
                currentActivity.startActivity(intent);
            }
        } catch (Exception e) {
            throw new JSApplicationIllegalArgumentException(
                    "不能打开Activity : " + e.getMessage());
        }
    }

    /**
     * 从JS页面拉起游戏并携带参数
     */
    @ReactMethod
    public void callGame(String kindId, String url) {
        Log.e("callGame", kindId + "    " + url);
        Activity currentActivity = getCurrentActivity();
        Intent intent = currentActivity.getPackageManager().getLaunchIntentForPackage("com.i5i58.gameplaza");
        if (intent != null) {
            intent.putExtra("userName", UserPreferences.getAccId());
            intent.putExtra("password", UserPreferences.getToken());
            intent.putExtra("kindId", kindId);
            intent.putExtra("openId", SystemCache.getPersonalMsg().getAccount().getOpenId());
            currentActivity.startActivity(intent);
            currentActivity.finish();
        } else {
//            Uri uri = Uri.parse("https://" + url);
            Uri uri = Uri.parse("https://www.i5i58.com/social/load-game");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            currentActivity.startActivity(intent);
        }
    }

    /**
     * 从JS页面进入直播间
     */
    @ReactMethod
    public void callChannel(final String roomId, final String cId, final String pullUrl, final String coverImage, final String channelId) {
        Log.e("callChannel", roomId + "    " + roomId);
        Log.e("callChannel", cId + "    " + cId);
        Log.e("callChannel", pullUrl + "    " + pullUrl);
        Log.e("callChannel", coverImage + "    " + coverImage);
        Log.e("callChannel", channelId + "    " + channelId);
        final Activity currentActivity = getCurrentActivity();
        if (TigerApplication.getInstance().isInRoom){
            Toast.makeText(currentActivity, "您已经在直播间！", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Intent intent = new Intent(currentActivity, MainAudienceActivity.class);
                    intent.putExtra("position", 0);
                    Bundle bundle = new Bundle();
                    ChannelData channel = new ChannelData();
                    channel.setCId(cId);
                    channel.setHttpPullUrl(pullUrl);
                    channel.setCoverUrl(coverImage);
                    channel.setYunXinRId(roomId);
                    channel.setChannelId(channelId);
                    List<ChannelData> channels = new ArrayList<>();
                    channels.add(channel);
                    bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
                    intent.putExtras(bundle);
                    Log.e("callChannel", intent + "    " + intent);
                    if (intent != null) {
                        try {
                            sleep(1000);
                            currentActivity.startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
//        Intent broad = new Intent(WelcomeActivity.action);
//        currentActivity.sendBroadcast(broad);
    }

    @ReactMethod
    public void OSSupLoadImages(String bucketName, String objectKey, String uploadFilePaths, final Callback callback){
        LogUtil.d(objectKey);
        LogUtil.d(uploadFilePaths);
            PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, FileUtils.File2byte(uploadFilePaths.substring(7, uploadFilePaths.length())));
            TigerApplication.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    callback.invoke(true);
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    callback.invoke(false);
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                        LogUtil.e("OSS : 本地异常如网络异常等");
                    }
                    if (serviceException != null) {
                        // 服务异常
                        LogUtil.e("ErrorCode" + serviceException.getErrorCode());
                        LogUtil.e("RequestId" + serviceException.getRequestId());
                        LogUtil.e("HostId" + serviceException.getHostId());
                        LogUtil.e("RawMessage" + serviceException.getRawMessage());
                    }
                }
            });

    }
    /**
     * JS点击返回按钮
     */
    @ReactMethod
    public void finishJS() {
        Activity currentActivity = getCurrentActivity();
        currentActivity.finish();
    }

    /**
     * JS点击返回并刷新token
     */
    @ReactMethod
    public void refreshToken(String token) {
        UserPreferences.saveToken(token);
    }

    /**
     * 支付宝支付
     */
    @ReactMethod
    public void payByAly() {
//       new Recharge().alPay();
    }

    /**
     * 微信宝支付
     */
    @ReactMethod
    public void payByWeiChat() {
//        new Recharge().weiChatPay();
    }

    /**
     * 从JS页面跳转到Activity界面，并且等待从Activity返回的数据给JS
     */
    @ReactMethod
    public void startActivityFromJSGetResult(String className, int requestCode, Callback successBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                Class toActivity = Class.forName(className);
                Intent intent = new Intent(currentActivity, toActivity);
                currentActivity.startActivityForResult(intent, requestCode);
                //进行回调数据
                successBack.invoke("callBackMsg : success!");
            }
        } catch (Exception e) {
            successBack.invoke("callBackMsg : error!");
            e.printStackTrace();
        }
    }

    /**
     * 该方法用于给JavaScript进行调用
     */
    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }
}
