package com.i5i58.live;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.i5i58.live.attachment.MyCustomAttachParser;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.system.SystemUtil;
import com.i5i58.live.main.activity.WelcomeActivity;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.IntentReactPackage;
import com.imagepicker.ImagePickerPackage;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by Lee on 2017/4/7.
 */

public class TigerApplication extends MultiDexApplication implements ReactApplication {

    private static TigerApplication m_myApplication;
    private DbManager dbManager;
    public boolean isLiving;
    public boolean isActive;
    public boolean isFromWeb;
    public boolean isInRoom;
    public OSSClient oss;
    private Handler handler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 单一实例
     */
    public static TigerApplication getInstance() {
        return m_myApplication;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DbManager dbManager){
        this.dbManager = dbManager;
    }

    /**
     * 检查程序是否退出
     * @param packageName
     * @return
     */
    public boolean checkAppExisted(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断是否在主进程
     * @return
     */
    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 获取当前版本的版本号
     * @return
     */
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    {
        PlatformConfig.setWeixin("wx880fa74e302e49c6", "ebd78ceb6555d6856379370780417123");
        PlatformConfig.setQQZone("1105831799", "KEYDN8AnxhE3FBXL3zZ");
        PlatformConfig.setSinaWeibo("1374374554", "f006e60c140f7721e8b2aa4e1dcd7283", "http://www.panglaohu.com");
    }

    /**
     * 初始化OSS
     */
    private void initOSS() {
        String endpoint = "http://gglivestatic.i5i58.com";
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("78xKgz5yZPGMjdtv", "PzsRMoiU4XsLFVURtNmi3jaxVeFoLT");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000);
        conf.setSocketTimeout(15 * 1000);
        conf.setMaxConcurrentRequest(5);
        conf.setMaxErrorRetry(2);
        oss = new OSSClient(this, endpoint, credentialProvider, conf);
    }

    private  void upLoadLog(String bucketName, String objectKey, String uploadFilePath){
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, FileUtils.File2byte(uploadFilePath));
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtil.d("upLoad Log success: ");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                LogUtil.d("upLoad Log failed: ");
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

    private void initNim() {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, null, new SDKOptions());
        if (inMainProcess()) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new MyCustomAttachParser()); // 监听的注册，必须在主进程中。

            // 如果有自定义通知是作用于全局的，不依赖某个特定的 Activity，那么这段代码应该在 Application 的 onCreate 中就调用
            NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
                @Override
                public void onEvent(CustomNotification message) {
                    // 在这里处理自定义通知。
                    try {
                        LogUtil.d("CustomNotification message: " + message.getContent());
                        String uploadFilePath = SystemCache.BASE_PATH + SystemCache.NIMLOG_PATH;
                        JSONObject msgJson = new JSONObject(message.getContent().toString());
                        switch (msgJson.getString("cmd")){
                            case "getLog":
                                LogUtil.d("uploadFilePath");
                                upLoadLog(msgJson.getString("bucketName"), msgJson.getString("objectKey"), uploadFilePath);
                                break;
//                            case "channelNotice":
//                                LogUtil.d("channelNotice");
//                                ChannelData channelData = GsonInner.getGsonInstance().fromJson(msgJson.getJSONObject("data").toString(), ChannelData.class);
//                                NotificationUtil.showNotification("直播通知", "直播通知", channelData.getOwnerName() + "正在直播，点击前往直播间", channelData, getApplicationContext());
//                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, true);
        }
    }

    /**
     * 自定义Umeng推送
     * */
    private void uPushRegister(final PushAgent mPushAgent){
        handler = new Handler();
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                LogUtil.d("" + msg.getRaw());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                    }
                });
            }
            /**
             * 自定义通知栏样式的回调方法
             * */
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                switch (msg.builder_id) {
//                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//
//                        return builder.getNotification();
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                    try {
//                        String uploadFilePath = SystemCache.BASE_PATH + SystemCache.NIMLOG_PATH;
                        JSONObject msgJson = new JSONObject(msg.custom);
                        switch (msgJson.getString("cmd")) {
//                            case "getLog":
//                                LogUtil.d("uploadFilePath");
//                                upLoadLog(msgJson.getString("bucketName"), msgJson.getString("objectKey"), uploadFilePath);
//                                break;
                            case "channelNotice":
                                LogUtil.d("channelNotice : " + msgJson);
                                if (isInRoom || isLiving){
                                    Toast.makeText(context, "请先退出直播间！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                JSONObject data = msgJson.getJSONObject("data");
                                Intent intent = new Intent(context, WelcomeActivity.class);
                                intent.putExtra("position", 0);
                                intent.putExtra("isNotification", true);
                                intent.putExtra("cId",              data.getString("cId"));
                                intent.putExtra("HttpPullUrl",      data.getString("httpPullUrl"));
                                intent.putExtra("CoverUrl",         data.getString("coverUrl"));
                                intent.putExtra("RoomId",           data.getString("yunXinRId"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
        };

        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.d("PushAgent register success ! token : " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e("PushAgent register onFailure ! \n :" + s + "\n :" + s1);
            }
        });

    }

    /**
     * *********************************************************************************************************************************************
     */
    public void onCreate() {
        super.onCreate();
        m_myApplication = this;
        SystemCache.setContext(this);
        SystemCache.setVersionName(getVersion());//填写版本号
        SystemUtil.getScreen(this);//填写屏幕信息

        initNim();//初始化云信

        x.Ext.init(this);//xUtils初始化
        x.Ext.setDebug(true);
        // 全局默认信任所有https域名 或 仅添加信任的https域名
        // 使用RequestParams#setHostnameVerifier(...)方法可设置单次请求的域名校验
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

//        //初始化下载器
//        FileDownloader.init(this);

        UMShareAPI.get(this); //实例化Umeng

        PushAgent mPushAgent = PushAgent.getInstance(this);//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setDebugMode(true);
        uPushRegister(mPushAgent);

        initOSS();

        LogUtil.d(SystemUtil.getHandSetInfo());
        LogUtil.d(SystemUtil.getCpuName());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    //===================================
    private static final IntentReactPackage myReactPackage = new IntentReactPackage();

    private final ReactNativeHost mReactNativeHost = new MyReactNativeHost(this) {
        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.asList(
                    new MainReactPackage(),
                    myReactPackage,
                    new ImagePickerPackage()
//                    new PickerViewPackage()
            );
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    public class MyReactNativeHost extends ReactNativeHost {

        protected MyReactNativeHost(Application application) {
            super(application);
        }

        @Override
        protected boolean getUseDeveloperSupport() {
            return false;
        }

        @Override
        protected @Nullable String getJSBundleFile() {
            String jsBundleFile = SystemCache.BASE_PATH + SystemCache.RNFOLDER + "/index.android.bundle";
            File file = new File(jsBundleFile);
            return file != null && file.exists() ? jsBundleFile : null;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return null;
        }
    }
}
