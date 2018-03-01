package com.i5i58.live.home.fragments;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.ZegoApiManager;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.utils.animation.AnimationUtil;
import com.i5i58.live.common.utils.json.GsonUtil;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.zego.zegoliveroom.callback.IZegoLivePlayerCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoVideoViewMode;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xiongxingxing on 16/12/3.
 */

@ContentView(R.layout.act_audience)
public class RoomFragment extends BaseFragment implements  AudienceOperateFragment.CleanOperateCallback {

    private ArrayList<ChannelData> channels;
    private static  RoomFragment instance;

    public static RoomFragment newInstance(List<ChannelData> channels) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
        if (null == instance) instance = new RoomFragment();
        if (!instance.isVisible())
        instance.setArguments(args);
        return instance;
    }

    private ArgbEvaluator argbEvaluator;
    private BaseFragmentAdapter baseFragmentAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    public AudienceOperateFragment audienceOperateFragment;
    private CountDownLatch countDownLatch = new CountDownLatch(2);
    private AnimationDrawable mAnimationDrawable;

    private String pullUrl; // 拉流地址

//    @ViewInject(R.id.audience_main_player)
//    private NEVideoView mVideoView;

    @ViewInject(R.id.textureView)
    private TextureView textureView;

    @ViewInject(R.id.audience_anchor_bg)
    private ImageView anchorImg;

    @ViewInject(R.id.audience_loading_ani)
    private ImageView loadingAni;

    @ViewInject(R.id.audience_main_player_clear)
    private ImageView clearIbt;

    @ViewInject(R.id.audience_player_layout_cid)
    private LinearLayout cidLayout;

    @ViewInject(R.id.audience_player_viewpager)
    private ViewPager pager;

    @ViewInject(R.id.audience_player_txt_cid)
    private TextView cIdTxt;

    @Event(R.id.audience_main_player_clear)
    private void clearClick(View v){
        pager.setCurrentItem(1);
    }

    private void initViewPager() {
        fragmentList.clear();
        fragmentList.add(new Fragment());
        fragmentList.add(audienceOperateFragment);
        argbEvaluator = new ArgbEvaluator();
        baseFragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(),fragmentList);
        pager.setAdapter(baseFragmentAdapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int evaluate = 0;
                if (position == 0){
                    evaluate = (int) argbEvaluator.evaluate(positionOffset, 255,0);
                    clearIbt.setAlpha(evaluate);
                    if (evaluate == 1){
                        clearIbt.setVisibility(View.GONE);
                    }else {
                        clearIbt.setVisibility(View.VISIBLE);
                    }
                    LogUtil.d("clearIbt  Alpha:" + evaluate);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    clearIbt.setEnabled(true);
                    audienceOperateFragment.isShowOperate = false;
                }else {
                    audienceOperateFragment.isShowOperate = true;
                    clearIbt.setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void stopLoading(){
        LogUtil.d("RoomFragment stopLoading ");
        mAnimationDrawable.stop();
        loadingAni.setVisibility(View.GONE);
    }

    /**
     * 更新动画
     */
    private void updateAnimation(){
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.ANIMATIONCONFIG)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                JSONObject data = js.getJSONObject("data");
                                SysPreferences.saveAnimationVersion(data.getString("animationConfigVersion"));
                                SysPreferences.saveAnimationZip(data.getString("animationZipConfig"));
                                SysPreferences.saveAnimationRes(data.getString("animationResConfig"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 更新坐骑
     */
    private void updateMount(){
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.MOUNTCONFIG)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(final JSONObject js, boolean success) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                        JSONObject data = js.getJSONObject("data");
                                        JSONArray mount = data.getJSONArray("mount");
                                        SysPreferences.saveGiftVersion(data.getString("mountConfigVersion"));
                                        SysPreferences.saveMountConfig(mount.toString());
                                        GsonUtil.setMountConfig(mount.toString());
                                        countDownLatch.countDown();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
    }

    /**
     * 更新礼物
     */
    private void updateGift(){
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GIFTCONFIG)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(final JSONObject js, boolean success) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
//                                        giftsUrls.clean();
                                        JSONObject data = js.getJSONObject("data");
                                        JSONArray gift = data.getJSONArray("gift");
                                        JSONArray node = data.getJSONArray("node");
                                        SysPreferences.saveGiftVersion(data.getString("giftConfigVersion"));
                                        SysPreferences.saveGiftConfig(gift.toString());
                                        SysPreferences.saveNode(node.toString());
                                        GsonUtil.setGiftNode(node.toString());
                                        GsonUtil.setGiftConfig(gift.toString());
                                        countDownLatch.countDown();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
    }

    /**
     * 检查直播间所需资源
     */
    private void checkResource() {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CHANNELCONFIG)
                .addParam("device", SystemCache.DEVICE)
                .addParam("giftVersion", SysPreferences.getGiftVersion())
                .addParam("mountVersion", SysPreferences.getMountVersion())
                .addParam("animationVersion", SysPreferences.getAnimationVersion())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                JSONObject data = js.getJSONObject("data");
                                if (data.has("gift") && data.getBoolean("gift")) {
                                    updateGift();
                                }else{//  加载礼物资源
                                    if ("-1".equals(SysPreferences.getNode()) || "-1".equals(SysPreferences.getGiftConfig())){
                                        updateGift();
                                    }else {
                                        GsonUtil.setGiftNode(SysPreferences.getNode());
                                        GsonUtil.setGiftConfig(SysPreferences.getGiftConfig());
                                        countDownLatch.countDown();
                                    }
                                }
                                if (data.has("mount") && data.getBoolean("mount")) {
                                    updateMount();
                                }else{//  加载坐骑资源
                                    if ("-1".equals(SysPreferences.getMountConfig())){
                                        updateGift();
                                    }else {
                                        GsonUtil.setGiftConfig(SysPreferences.getMountConfig());
                                        countDownLatch.countDown();
                                    }
                                }
                                if (data.has("animation") && data.getBoolean("animation")) {
                                    updateAnimation();
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            countDownLatch.await();
                                            audienceOperateFragment.enterChannel();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView(ChannelData channel) {
        loadingAni.setImageResource(com.handmark.pulltorefresh.library.R.drawable.drawable_waiting);
        mAnimationDrawable = (AnimationDrawable) loadingAni.getDrawable();
        mAnimationDrawable.start();
        Glide.with(this).load(API.OSS_URL_CHANNELCOVER + channel.getCoverUrl()).into(anchorImg);
        anchorImg.setVisibility(View.VISIBLE);
        cIdTxt.setText(channel.getChannelId());
        cidLayout.setVisibility(View.VISIBLE);
        cidLayout.setAnimation(AnimationUtil.moveFromViewRight());
        cidLayout.getBackground().setAlpha(100);
        clearIbt.setAlpha(0);
    }

    private final String mMediaType = "livestream"; //视屏类型
    private final boolean mHardware = false; //解码类型
    private boolean mEnableBackgroundPlay = true;//允许后台播放
    private final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    private final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    private final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    private final int NELP_LOG_DEBUG   = 3; //!< log输出模式：输出调试信息
    private final int NELP_LOG_INFO    = 4; //!< log输出模式：输出标准信息
    private final int NELP_LOG_WARN    = 5; //!< log输出模式：输出警告
    private final int NELP_LOG_ERROR   = 6; //!< log输出模式：输出错误
    private final int NELP_LOG_FATAL   = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    private final int NELP_LOG_SILENT  = 8; //!< log输出模式：不输出

    private String mRoomID;
    private String streamID;
    private boolean isPlaying;

    protected void logoutZEGO() {
        if (null != streamID){
            ZegoApiManager.getInstance().getZegoLiveRoom().stopPlayingStream(streamID);
            streamID = null;
            isPlaying = false;
        }
    }

    /**
     * 开始播放流.
     */
    protected void startPlay(String streamID) {
        if (TextUtils.isEmpty(streamID)) {
            return;
        }
        // 播放
        ZegoApiManager.getInstance().getZegoLiveRoom().startPlayingStream(streamID, textureView);
        ZegoApiManager.getInstance().getZegoLiveRoom().setViewMode(ZegoVideoViewMode.ScaleAspectFill, streamID);
    }

    /**
     * 观众登录房间成功.
     */
    protected void handleAudienceLoginRoomSuccess(ZegoStreamInfo[] zegoStreamInfos) {
        // 播放房间的流
        if (zegoStreamInfos != null && zegoStreamInfos.length > 0) {
            for (int i = 0; i < zegoStreamInfos.length; i++) {
                streamID = zegoStreamInfos[i].streamID;
                LogUtil.d("handleAudienceLoginRoomSuccess streamID : " +streamID );
                startPlay(zegoStreamInfos[i].streamID);
            }
        }
    }

    private void loginZEGO(){
        if (isPlaying) return;
        LogUtil.d("mRoonID : " + mRoomID);
        ZegoApiManager.getInstance().getZegoLiveRoom().loginRoom(mRoomID, ZegoConstants.RoomRole.Audience, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errorCode, ZegoStreamInfo[] zegoStreamInfos) {
                if(errorCode == 0){
                    handleAudienceLoginRoomSuccess(zegoStreamInfos);
                }else {
                    Toast.makeText(getActivity(), "LoginZego error !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ZegoApiManager.getInstance().getZegoLiveRoom().setZegoLivePlayerCallback(new IZegoLivePlayerCallback() {
            @Override
            public void onPlayStateUpdate(int stateCode, String streamID) {
                // 拉流状态更新
                if (stateCode == 0) {
//                    handlePlaySucc(streamID);
                } else {
//                    handlePlayStop(stateCode, streamID);
                }
            }

            @Override
            public void onPlayQualityUpdate(String streamID, ZegoStreamQuality streamQuality) {
                // 拉流质量回调
//                handlePlayQualityUpdate(streamID, streamQuality.quality, streamQuality.videoFPS, streamQuality.videoBitrate);
            }

            @Override
            public void onInviteJoinLiveRequest(int seq, String fromUserID, String fromUserName, String roomID) {

            }

            @Override
            public void onRecvEndJoinLiveCommand(String fromUserId, String fromUserName, String roomId) {

            }

            @Override
            public void onVideoSizeChangedTo(String streamID, int width, int height) {
                LogUtil.d("onVideoSizeChangedTo : " + width + " >>>> " + height);
                if (null != anchorImg) anchorImg.setVisibility(View.GONE);
                if (null != textureView) textureView.setVisibility(View.VISIBLE);
                isPlaying = true;
            }
        });

        ZegoApiManager.getInstance().getZegoLiveRoom().setZegoRoomCallback(new IZegoRoomCallback() {
            @Override
            public void onKickOut(int reason, String roomID) {

            }

            @Override
            public void onDisconnect(int errorCode, String roomID) {
//                handleDisconnect(errorCode, roomID);
            }

            @Override
            public void onStreamUpdated(final int type, final ZegoStreamInfo[] listStream, final String roomID) {
                if (listStream != null && listStream.length > 0) {
                    switch (type) {
                        case ZegoConstants.StreamUpdateType.Added:
//                            handleStreamAdded(listStream, roomID);
                            break;
                        case ZegoConstants.StreamUpdateType.Deleted:
//                            handleStreamDeleted(listStream, roomID);
                            break;
                    }
                }
            }

            @Override
            public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {

            }

            @Override
            public void onRecvCustomCommand(String userID, String userName, String content, String roomID) {

            }
        });

    }

    private void initAudienceParam() {
        loginZEGO();
        // 初始化拉流
//        mVideoView.setVisibility(View.VISIBLE);
//        mVideoView.setBufferStrategy(NELivePlayer.NELPLOWDELAY); //直播低延时
//        mVideoView.setMediaType(mMediaType);
//        mVideoView.setHardwareDecoder(mHardware);
//        mVideoView.setEnableBackgroundPlay(mEnableBackgroundPlay);
//        mVideoView.setVideoPath(pullUrl);
//        mVideoView.setLogLevel(NELP_LOG_DEFAULT); //设置log级别
//        mVideoView.setPlayer(this);
//        mVideoView.start();
    }

    private void loadView(int position){
        channels = getArguments().getParcelableArrayList("channels");
        ChannelData channel = channels.get(position);
        mRoomID = channel.getcId();
        initAudienceParam();
        LogUtil.d("cId : " + channel.getCId());
        LogUtil.d("channelId : " + channel.getChannelId());

        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETCHANNELINFO)
                .addParam("cId", channel.getCId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                ChannelData channel = GsonInner.getGsonInstance().fromJson(js.getJSONObject("data").getJSONObject("channel").toString(), ChannelData.class);
                                LiveRoomCache.setChannelId(channel.getCId());
                                pullUrl = channel.getHttpPullUrl();
                                initView(channel);
                                LogUtil.d("is inMainProcess : " + TigerApplication.getInstance().inMainProcess());
                                final String roomId = channel.getYunXinRId();
                                LiveRoomCache.setRoomId(roomId);
                                audienceOperateFragment = new AudienceOperateFragment(RoomFragment.this,channel);
                                audienceOperateFragment.enterRoom(roomId);
                                checkResource();
                                initViewPager();
                                stopLoading();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void change(int position) {
        LogUtil.d("change >>>> "+ position);
        logoutZEGO();
        loadView(position);
    }

    public void clear(boolean isBack) {
        if (null != audienceOperateFragment) {
            audienceOperateFragment.clean();
        }
        logoutZEGO();
    }

    public void showPlayBgImg(){
        if (null != anchorImg && anchorImg.getVisibility() == View.GONE){
            anchorImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openPlay() {
        if (!isPlaying){
            LogUtil.d("RoomFragment openPlay");
            initAudienceParam();
        }
    }

    @Override
    public void closeVideo() {
        textureView.setVisibility(View.GONE);
        logoutZEGO();
        showPlayBgImg();
    }

    @Override
    public void cleanOperate() {
        pager.setCurrentItem(0);
    }

    @Override
    public void stopLode() {
        stopLoading();
    }

    public void sizeChange(boolean isHoriz) {
        LogUtil.d("sizeChange" + isHoriz);
        if (null != audienceOperateFragment){
            audienceOperateFragment.onVideoSizeChanged(isHoriz);
        }
    }
}

