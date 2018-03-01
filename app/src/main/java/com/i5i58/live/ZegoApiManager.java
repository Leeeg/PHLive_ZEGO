package com.i5i58.live;


import android.text.TextUtils;
import android.widget.Toast;

import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.system.SystemUtil;
import com.i5i58.live.common.utils.system.TimeUtil;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.videocapture.VideoCaptureFactoryDemo;
import com.i5i58.live.videofilter.VideoFilterFactoryDemo;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.constants.ZegoAvConfig;

import org.xutils.common.util.LogUtil;


/**
 * des: zego api管理器.
 */
public class ZegoApiManager {

    private static ZegoApiManager sInstance = null;

    private ZegoLiveRoom mZegoLiveRoom = null;

    private ZegoAvConfig mZegoAvConfig;

    /**
     * 外部渲染开关.
     */
    private boolean mUseExternalRender = false;

    /**
     *  测试环境开关.
     */
    private boolean mUseTestEvn = false;

    /**
     * 外部采集开关.
     */
    private boolean mUseVideoCapture = false;

    /**
     * 外部滤镜开关.
     */
    private boolean mUseVideoFilter = false;

    private boolean mUseHardwareEncode = true;

    private boolean mUseHardwareDecode = false;

    private boolean mUseRateControl = false;

    private long mAppID = 0;
    private byte[] mSignKey = null;

    private ZegoApiManager() {
        mZegoLiveRoom = new ZegoLiveRoom();
    }

    public static ZegoApiManager getInstance() {
        if (sInstance == null) {
            synchronized (ZegoApiManager.class) {
                if (sInstance == null) {
                    sInstance = new ZegoApiManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 高级功能.
     */
    private void openAdvancedFunctions(){

        // 开启测试环境
        if(mUseTestEvn){
            ZegoLiveRoom.setTestEnv(true);
        }

        // 外部渲染
        if(mUseExternalRender){
            // 开启外部渲染
            ZegoLiveRoom.enableExternalRender(true);
        }

        // 外部采集
        if(mUseVideoCapture){
            // 外部采集
            VideoCaptureFactoryDemo factoryDemo = new VideoCaptureFactoryDemo();
            factoryDemo.setContext(TigerApplication.getInstance());
            ZegoLiveRoom.setVideoCaptureFactory(factoryDemo);
        }

        // 外部滤镜
        if(mUseVideoFilter){
            // 外部滤镜
            VideoFilterFactoryDemo videoFilterFactoryDemo = new VideoFilterFactoryDemo();
            ZegoLiveRoom.setVideoFilterFactory(videoFilterFactoryDemo);
        }
    }

    private void initUserInfo(){
        // 初始化用户信息
        String userID = UserPreferences.getAccId();
        String userName = UserPreferences.getNickName() == null? UserPreferences.getOpenId():UserPreferences.getNickName();
        if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(userName)) {
            long ms = System.currentTimeMillis();
            userID = ms + "";
            userName = "Android_" + TimeUtil.getNow() + "-" + ms;
        }
        // 必须设置用户信息
        LogUtil.d(userID + " >>> " + userName);
        ZegoLiveRoom.setUser(userID, userName);

    }


    private void init(long appID, byte[] signKey){

        initUserInfo();

        // 开发者根据需求定制
        openAdvancedFunctions();

        mAppID = appID;
        mSignKey = signKey;

        // 初始化sdk
        boolean ret = mZegoLiveRoom.initSDK(appID, signKey, TigerApplication.getInstance());
        if(!ret){
            // sdk初始化失败
            Toast.makeText(TigerApplication.getInstance(), "Zego SDK初始化失败!", Toast.LENGTH_LONG).show();
        } else {
            // 初始化设置级别为"High"
            mZegoAvConfig = new ZegoAvConfig(ZegoAvConfig.Level.High);
            mZegoLiveRoom.setAVConfig(mZegoAvConfig);
            mZegoLiveRoom.enableAEC(true);
            mZegoLiveRoom.enableAGC(true);
            mZegoLiveRoom.setTestEnv(false);
            // 开发者根据需求定制
            // 硬件编码
            setUseHardwareEncode(mUseHardwareEncode);
            // 硬件解码
            setUseHardwareDecode(mUseHardwareDecode);
            // 码率控制
            setUseRateControl(mUseRateControl);
        }
    }

    /**
     * 此方法是通过 appId 模拟获取与之对应的 signKey，强烈建议 signKey 不要存储在本地，而是加密存储在云端，通过网络接口获取
     * @param appId
     * @return
     */
    private byte[] requestSignKey(long appId) {
        return ZegoAppHelper.requestSignKey(appId);
    }

    /**
     * 初始化sdk.
     */
    public void initSDK(String mAppID, String mSignKey){
        if (SystemCache.isZegoInited()) return;
        SystemCache.setZegoInited(true);
        // 即构分配的key与id, 默认使用 UDP 协议的 AppId
//        if (mAppID <= 0) {
//            mAppID = ZegoAppHelper.RTMP_APP_ID;
//            mSignKey = requestSignKey(mAppID);
//        }
        init(Long.valueOf(mAppID), SystemUtil.getFromBase64(mSignKey));
    }

    public void reInitSDK(long appID, byte[] signKey) {
        init(appID, signKey);
    }

    public void releaseSDK() {
        // 清空高级设置
        ZegoLiveRoom.setTestEnv(false);
        ZegoLiveRoom.enableExternalRender(false);
        ZegoLiveRoom.setVideoCaptureFactory(null);
        ZegoLiveRoom.setVideoFilterFactory(null);
        SystemCache.setZegoInited(false);
        mZegoLiveRoom.unInitSDK();
    }

    public ZegoLiveRoom getZegoLiveRoom() {
        return mZegoLiveRoom;
    }

    public void setZegoConfig(ZegoAvConfig config) {
        mZegoAvConfig = config;
        mZegoLiveRoom.setAVConfig(config);
    }


    public ZegoAvConfig getZegoAvConfig(){
        return  mZegoAvConfig;
    }


    public void setUseTestEvn(boolean useTestEvn) {
        mUseTestEvn = useTestEvn;
    }

    public boolean isUseExternalRender(){
        return mUseExternalRender;
    }

    public void setUseExternalRender(boolean useExternalRender){
        mUseExternalRender = useExternalRender;
    }

    public void setUseVideoCapture(boolean useVideoCapture) {
        mUseVideoCapture = useVideoCapture;
    }

    public void setUseVideoFilter(boolean useVideoFilter) {
        mUseVideoFilter = useVideoFilter;
    }

    public boolean isUseVideoCapture() {
        return mUseVideoCapture;
    }

    public boolean isUseVideoFilter() {
        return mUseVideoFilter;
    }

    public void setUseHardwareEncode(boolean useHardwareEncode) {
        if(useHardwareEncode){
            // 开硬编时, 关闭码率控制
            if(mUseRateControl){
                mUseRateControl = false;
                mZegoLiveRoom.enableRateControl(false);
            }
        }
        mUseHardwareEncode = useHardwareEncode;
        ZegoLiveRoom.requireHardwareEncoder(useHardwareEncode);
    }

    public void setUseHardwareDecode(boolean useHardwareDecode) {
        mUseHardwareDecode = useHardwareDecode;
        ZegoLiveRoom.requireHardwareDecoder(useHardwareDecode);
    }

    public void setUseRateControl(boolean useRateControl) {
        if(useRateControl){
            // 开码率控制时, 关硬编
            if(mUseHardwareEncode){
                mUseHardwareEncode = false;
                ZegoLiveRoom.requireHardwareEncoder(false);
            }
        }
        mUseRateControl = useRateControl;
        mZegoLiveRoom.enableRateControl(useRateControl);
    }

    public long getAppID() {
        return mAppID;
    }

    public byte[] getSignKey() {
        return mSignKey;
    }

    public boolean isUseTestEvn(){
        return mUseTestEvn;
    }

}
