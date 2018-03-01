package com.i5i58.live.home.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.ZegoApiManager;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.attachment.SingleChatAttachment;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.constants.Constants;
import com.i5i58.live.common.constants.IntentExtra;
import com.i5i58.live.common.enums.BGMEnum;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.net.ThreadPool;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.animation.AnimationUtil;
import com.i5i58.live.common.utils.countDown.LCountDownTimer;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoad;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoadCallback;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.imageLoader.ImagePickerGLideImageLoader;
import com.i5i58.live.common.utils.imgUtil.BitmapUtil;
import com.i5i58.live.common.utils.json.GsonUtil;
import com.i5i58.live.common.utils.layoutManager.ScrollSpeedLinearLayoutManger;
import com.i5i58.live.common.view.barrage.BarrageView;
import com.i5i58.live.common.view.gift2dview.Box2DFragment;
import com.i5i58.live.common.view.gift2dview.Tools.ScreenParamUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.common.view.listview.HorizontalListView;
import com.i5i58.live.common.view.recyclerView.MyRecyclerView;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.common.view.swipeitem.recyclerview.SwipeItemLayout;
import com.i5i58.live.emoji.EmoticonPickerView;
import com.i5i58.live.emoji.IEmoticonSelectedListener;
import com.i5i58.live.emoji.InputTypeEnum;
import com.i5i58.live.home.adapters.ChatRoomMessageAdapter;
import com.i5i58.live.home.adapters.FilterAdapter;
import com.i5i58.live.home.adapters.SofaListAdapter;
import com.i5i58.live.home.constant.PushLinkConstant;
import com.i5i58.live.home.dialogFragment.MusicListDialogFragment;
import com.i5i58.live.home.dialogFragment.MusicOperateDialogFragment;
import com.i5i58.live.home.dialogFragment.PersonalDialogFragment;
import com.i5i58.live.home.dialogFragment.SingleChatDialogFragment;
import com.i5i58.live.home.dialogFragment.SingleChatInfoDialogFragment;
import com.i5i58.live.home.fragments.GameLoadingView;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.MessageCallBack;
import com.i5i58.live.home.helper.bgmHelper.Mp3Info;
import com.i5i58.live.home.helper.channelGift.GiftMessageQueue;
import com.i5i58.live.home.helper.periscope.PeriscopeLayout;
import com.i5i58.live.luban.Luban;
import com.i5i58.live.luban.OnCompressListener;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.account.Account;
import com.i5i58.live.model.entity.account.SofaAccount;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.permission.MPermission;
import com.i5i58.live.permission.annotation.OnMPermissionDenied;
import com.i5i58.live.permission.annotation.OnMPermissionGranted;
import com.i5i58.live.permission.annotation.OnMPermissionNeverAskAgain;
import com.i5i58.live.permission.util.MPermissionUtil;
import com.i5i58.live.reactNative.MyReactActivity;
import com.i5i58.live.recharge.Recharge;
import com.i5i58.live.webSocket.receive.Channel;
import com.i5i58.live.webSocket.receive.Owner;
import com.i5i58.live.webSocket.receive.Self;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMemberUpdate;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomUpdateInfo;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoAudioPrepCallback2;
import com.zego.zegoliveroom.callback.IZegoLivePublisherCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoAvConfig;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoVideoViewMode;
import com.zego.zegoliveroom.entity.AuxData;
import com.zego.zegoliveroom.entity.ZegoAudioFrame;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;
import com.zhouwei.blurlibrary.EasyBlur;

import org.dync.giftlibrary.widget.GiftControl;
import org.dync.giftlibrary.widget.GiftFrameLayout;
import org.dync.giftlibrary.widget.GiftModel;
import org.dync.giftlibrary.widget.NumberTextView;
import org.egret.egretframeworknative.EgretRuntime;
import org.egret.egretframeworknative.GL2JNIView;
import org.egret.egretframeworknative.engine.EgretGameEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Lee on 2017/4/24.
 */

@ContentView(R.layout.act_anchor_zego)
public class ZegoAnchorActivity extends BaseActivity implements MessageCallBack, AndroidFragmentApplication.Callbacks, IEmoticonSelectedListener {

    private final int PICTURE_REQUEST_CODE    = 1000;
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private boolean isPermissionGrant = false;

    private int networkQuality = -1;
    private PopupWindow popWindow;

    private ArrayList<ImageItem> images;
    private Drawable coverDraw;         //封面图
    private boolean hasCoverImg = false;
    private boolean isChangeCoverImg = false;
    private boolean hasTitle = false;
    private boolean isChangeTitle = true;
    private boolean backEnabled = true;
    private GiftMessageQueue giftMessageQueue;

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};

    @ViewInject(R.id.number_countdown)
    private NumberTextView countDownNumber; // 开播倒计时

    @ViewInject(R.id.time_live)
    private Chronometer livaTime; // 直播计时

    @ViewInject(R.id.net_qualtiy)
    private TextView netQualityImg; // 直播网络状态

    @ViewInject(R.id.textureView)
    private TextureView textureView; // 预览画面

    @ViewInject(R.id.startAv_iv_setCoverImage)
    private ImageView coverImage;

    @ViewInject(R.id.startAv_tv_setTitle)
    private TextView titleTxt;

    @ViewInject(R.id.layout_startLive_before)
    private RelativeLayout startLiveBefore;

    @ViewInject(R.id.anchor_operate_layout)
    private FrameLayout operateLayout;

    @ViewInject(R.id.startAv_tv_setTitle)
    private EditText title;

    @Event({R.id.change_camera, R.id.anchor_camera})
    private void changeCamera(View v){
        mEnableFrontCam = !mEnableFrontCam;
        mZegoLiveRoom.setFrontCam(mEnableFrontCam);
    }

    @Event({R.id.bt_anchor_exit, R.id.anchor_operate_top_exit})
    private void exitClick(View v){
        closeAct();
    }

    @Event(R.id.close_filter_view)
    private void closeFilterClick(View v){
        filterLayout.setVisibility(View.GONE);
        filterLayout.setAnimation(AnimationUtil.moveToViewBottom());
    }

    @Event(R.id.play_tv_rules)
    private void rulesClick(View view){
        Intent intent = new Intent(this, MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "SecretIndex";
        startActivity(intent);
    }

    @Event(R.id.startAv_iv_setCoverImage)
    private void coverImageClick(View v){
        Intent intent = new Intent(ZegoAnchorActivity.this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,false); // 是否是直接打开相机
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    @Event(R.id.bt_play)
    private void startLiveBt(View v){
        if (!hasCoverImg){
            TSBError("请设置直播封面！");
            return;
        }
        if (!hasTitle || null == title.getText() || "".equals(title.getText())){
            TSBError("请输入标题！");
            return;
        }
        if(mIsPublishing) {
            return;
        }
        initOSS();

        loginZEGO();

    }

    private Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            countDownNumber(msg.obj.toString());
        }
    };

    private void countDownNumber(String number){
        if (number.equals("0")){
            countDownNumber.setVisibility(View.GONE);
            livingTime.setVisibility(View.VISIBLE);
            joinChannel();
            ThreadPool.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    startTime = System.currentTimeMillis();
                    livaTime.setBase(SystemClock.elapsedRealtime());//计时器清零
                    int hour = (int) ((SystemClock.elapsedRealtime() - livaTime.getBase()) / 1000 / 60);
                    livaTime.setFormat("0"+String.valueOf(hour)+":%s");
                    livaTime.start();
                    LogUtil.e("livaTime start");
                    backEnabled = true;
                }
            });
        }else {
            backEnabled = false;
            if (startLiveBefore.getVisibility() == View.VISIBLE)
            startLiveBefore.setVisibility(View.GONE);

            //倒数动画
            countDownNumber.setVisibility(View.VISIBLE);
            countDownNumber.updataNumber(number);

            // 设置透明度渐变动画
            final AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            //设置动画持续时间
            alphaAnimation.setDuration(1000);
            countDownNumber.startAnimation(alphaAnimation);

            // 设置缩放渐变动画
            final ScaleAnimation scaleAnimation =new ScaleAnimation(2f, 0.5f, 2, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1000);
            countDownNumber.startAnimation(scaleAnimation);
        }
    }

    //***********************************************************************直播端操作界面***********************************************************************

    private CountDownLatch countDownLatch = new CountDownLatch(2);
//    private WebSocket webSocket;
    private boolean webSocketLost = false;
    private List<SofaAccount> sofaAccountList = new ArrayList<>();
    private SofaListAdapter sofaListAdapter;
    private List<ChatRoomMessage> messageList = new ArrayList();
    private ChatRoomMessageAdapter messageAdapter;
    //    private MessageAdapter messageAdapter;
    private int mIndex = 0;
    private ArrayList<Bitmap> pList;
    private InputMethodManager imm;
    private boolean isBGM = true;
    private String bgFilePath;

    private String egretRoot;
    private final String EGRET_ROOT = "egret";
    private String gameId;
    private EgretGameEngine gameEngine;
    private boolean engineInited = false;
    //若bUsingPlugin为true，开启插件
    private static boolean bUsingPlugin = false;
    private String loaderUrl;
    private String updateUrl;
    private boolean isMenuShow = false;
    private Animation menuToUp;
    private Animation menuToDown;
    private ScaleAnimation menuOpen;
    private ScaleAnimation menuClose;
    private int audio = 50; // 媒体音量
    private String musicName = "未播放";//伴奏名字
    private String musicSingler = "";//伴奏演唱者
    private BGMEnum bgmType = BGMEnum.STOP;
    private List<Mp3Info> bgmList = new ArrayList<>();
    private int playIndex = -1;
    private Timer timer;
    private Map<String, Object> updateMap = new HashMap();
    private String wallet = "0";

    private final String GIVEHEART = "giveHeart";
    private final String FOLLWOANCHOR = "followAnchor";
    private final String OPENCLUB = "openClub";
    private final String GIFT = "gift";
    private final String OPENGUARD = "openGuard";
    private final String ENTER = "enter";
    private final String CONNMIC = "connMic";
    private final String DRIFTCOMMENT = "driftComment";

    private GiftModel giftModel;
    private GiftControl giftControl;

    private LCountDownTimer mLCountDownTimer = null;

    private InputTypeEnum inputType = InputTypeEnum.KEYBOARD;
    private String atTarget;
    private String atAccId;
    private boolean isAt = false;
    private boolean isBarrageOpen = false;

    private long startTime, closeTime, watchTimes = 0;

    //分享
    private ShareAction mShareAction;
    private ShareBoardConfig config;

    //box2D
    private Box2DFragment m_box2dFgm;

    @ViewInject(R.id.listview_filter)
    public HorizontalListView horizontalListView;

    @ViewInject(R.id.seekBer_face)
    public SeekBar seekBarFace;

    @ViewInject(R.id.seekBer_filter)
    public SeekBar seekBarFilter;

    @ViewInject(R.id.layout_filter)
    public LinearLayout filterLayout;

    @ViewInject(R.id.filter_layout_bottom)
    public LinearLayout filterLayoutBottom;

    @ViewInject(R.id.layout_live_close)
    private FrameLayout closeLayout;

    @ViewInject(R.id.closeav_tv_time)
    private TextView livingTime;

    @ViewInject(R.id.closeav_tv_watchsum)
    private TextView watchCount;

    @ViewInject(R.id.closeav_iv_bg)
    private ImageView bg_close;

    @ViewInject(R.id.power_braager)
    private ImageView braagerImg;

    @ViewInject(R.id.emoji_buttonSingle)
    private ImageView emojiImg;

    @ViewInject(R.id.emoticon_picker_view)
    private EmoticonPickerView pickerView;

    @ViewInject(R.id.gift_layout1)
    private GiftFrameLayout giftFrameLayout1;

    @ViewInject(R.id.gift_layout2)
    private GiftFrameLayout giftFrameLayout2;

    @ViewInject(R.id.lyt_container)
    private FrameLayout m_container;

    @ViewInject(R.id.anchor_menuLayout)
    private LinearLayout menuLayout;

    @ViewInject(R.id.periscope)
    private PeriscopeLayout periscope;

    @ViewInject(R.id.containerView)
    private BarrageView containerView;

    @ViewInject(R.id.editTextSingleMessage)
    private EditText messageEdit;

    @ViewInject(R.id.layout1)
    private RelativeLayout layout1;

    @ViewInject(R.id.textroomleMessageLayout)
    public LinearLayout messageLayout;

    @ViewInject(R.id.audience_operate_top_person)
    private LinearLayout personLayout;

    @ViewInject(R.id.operate_listview_message)
    private RecyclerView messageRecyclerView;

    @ViewInject(R.id.frg_operate_fl_bottom)
    public RelativeLayout bottomLl;

    @ViewInject(R.id.frg_operate_fl_top)
    private LinearLayout topLl;

    @ViewInject(R.id.audience_player_layout_cid)
    private LinearLayout cIdLayout;

    @ViewInject(R.id.audience_operate_top_contribution)
    private LinearLayout contributionLayout;

    @ViewInject(R.id.audience_player_txt_cid)
    private TextView cIdTxt;

    @ViewInject(R.id.operate_top_txt_ad)
    private TextView adTxt;

    @ViewInject(R.id.tv_owner_name)
    private TextView anchorName;

    @ViewInject(R.id.tv_onlinepeaple)
    private TextView onlineTxt;

    @ViewInject(R.id.operate_tv_contribution)
    private TextView contributionTxt;

    @ViewInject(R.id.audience_operate_top_icon)
    private ImageView anchorIcon;

    @ViewInject(R.id.iv_sliveroom_bottom_iv_bgm)
    private ImageView bgmImg;

    @ViewInject(R.id.message_recycle_scrotobottom)
    private ImageView scrollToBottom;

    @ViewInject(R.id.liveroom_bottom_iv_keyboard)
    private ImageView talkImg;

    @ViewInject(R.id.iv_sliveroom_bottom_iv_menu)
    private ImageView menu;

    @ViewInject(R.id.iv_sliveroom_bottom_iv_single)
    private ImageView singleImg;

    @ViewInject(R.id.audience_operate_top_sofa)
    private MyRecyclerView sofaRecyclerView ;

    @ViewInject(R.id.menu_gift)
    private ImageView menuGift;

    @ViewInject(R.id.menu_mount)
    private ImageView menuMount;

    @Event(R.id.anchor_filter)
    private void filterMenuClick(View view){
        if (isMenuShow){
            animationMenuClose(menu);
            menuLayout.setVisibility(View.GONE);
            menuLayout.setAnimation(AnimationUtil.moveToViewRight());
            isMenuShow = !isMenuShow;
        }
        filterLayout.setVisibility(View.VISIBLE);
        filterLayout.setAnimation(AnimationUtil.moveFromViewBootom());
    }

    @Event(R.id.anchor_gift)
    private void giftOpenClick(View view){
        if (LiveRoomCache.isGiftOpen()){
            menuGift.setImageResource(R.drawable.bg_anchor_gift_close);
            LiveRoomCache.setIsGiftOpen(false);
        }else {
            menuGift.setImageResource(R.drawable.bg_anchor_gift_open);
            LiveRoomCache.setIsGiftOpen(true);
        }
    }

    @Event(R.id.anchor_mount)
    private void mountOpenClick(View view){
        if (LiveRoomCache.isMountOpen()){
            menuMount.setImageResource(R.drawable.bg_anchor_mount_close);
            LiveRoomCache.setIsMountOpen(false);
        }else {
            menuMount.setImageResource(R.drawable.bg_anchor_mount_open);
            LiveRoomCache.setIsMountOpen(true);
        }
    }

    @Event(R.id.bt_close)
    private void liveCloseClick(View view){
        closeAct(this);
    }

    @Event({R.id.audience_operate_top_icon, R.id.ll_persion})
    private void personClick(View v){
        PersonalDialogFragment fragment = new PersonalDialogFragment(LiveRoomCache.getOwner().getAccId(), true, false, new PersonalDialogFragment.PersonalCallback() {
            @Override
            public void report(String taAccId) {
                reportPopWindow(taAccId);
            }

            @Override
            public void attention(boolean isFollow) {

            }

            @Override
            public void singleChat(String name, String iconUrl, String accId) {

            }

            @Override
            public void at(String accId, String at) {

            }
        });
        fragment.show(getSupportFragmentManager(),PersonalDialogFragment.class.getSimpleName());
    }

    @Event(R.id.liveroom_bottom_iv_keyboard)
    private void talkClick(View view){
        bottomLl.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        messageEdit.setFocusable(true);
        messageEdit.setFocusableInTouchMode(true);
        messageEdit.requestFocus();
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Event(R.id.iv_sliveroom_bottom_iv_single)
    private void singleClick(View v){
        SingleChatDialogFragment fragment = new SingleChatDialogFragment();
        fragment.show(getSupportFragmentManager(),SingleChatDialogFragment.class.getSimpleName());
        singleImg.setImageResource(R.drawable.bt_operate_bottom_singlechat);
    }

    @Event(R.id.audience_operate_top_contribution)
    private void intentToContribute(View v){
        Intent intent = new Intent(this, ContributeActivity.class);
        intentAct(this, intent);
    }

    @Event(R.id.icon_mac)
    private void intentToMic(View v){
        Intent intent = new Intent(this, MicActivity.class);
        intentAct(this, intent);
    }

    @Event(R.id.message_recycle_scrotobottom)
    private void scrollToBottom(View v){
        scrollToBottom.setVisibility(View.GONE);
        messageRecyclerView.smoothScrollToPosition(messageList.size());
    }

    private void sendBarrageMsg(String content){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.DRIFTCOMMENT)
                .addParam("device",     SystemCache.DEVICE)
                .addParam("cId",        mRoomID)
                .addParam("content",    content)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                LogUtil.d("sendGift success !");
                            }else {
                                LogUtil.d("sendGift error : " + js);
                                String msg = js.getString("msg");
                                String code = js.getString("code");
                                if (code.equals("igold_not_enough")){
                                    imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    rechargePopWindow();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Event(R.id.buttonSendMessage)
    private void sendMessage(View v){
        CharSequence msg = messageEdit.getText();
        if (null != msg && !"".equals(msg.toString().trim())){
            if (isAt && !isBarrageOpen){
                if (msg.length() < atTarget.length()){
                    isAt = false;
                }else if (msg.length() >= atTarget.length()){
                    String atStr = msg.toString().substring(0, atTarget.length());
                    if (!atStr.equals(atTarget)){
                        isAt = false;
                    }
                }
            }

            ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(LiveRoomCache.getRoomId(),msg.toString().trim());
            message.setLocalExtension(updateMap);

            if (isAt && !isBarrageOpen){//@
                Map<String, Object> ext = new HashMap<>();
                ext.put("cmd", "at");
                ext.put("source", message.getFromAccount());
                ext.put("target", atAccId);
                ext.put("content", msg.toString());
                message.setRemoteExtension(ext);

                NIMClient.getService(ChatRoomService.class).sendMessage(message, false);
                messageList.add(message);
                if (messageList.size() > 100){
                    messageList.remove(0);
                }
                messageAdapter.notifyDataSetChanged();
                messageRecyclerView.smoothScrollToPosition(messageList.size());
                isAt = false;
            }else if (isBarrageOpen && !isAt){//漂屏
                String content = "{\"source\":\"" + UserPreferences.getAccId() + "\",\"type\":\"text\",\"contents\":\"" + " " + msg.toString() + "\"}";
                sendBarrageMsg(content);
//                BarrageParams params = new BarrageParams();
//                params.setContent(content);
//                BarrageEntity be = new BarrageEntity();
//                be.setCmd("driftComment");
//                be.setParams(params);
//                if (null != webSocket && webSocket.isOpen()){
//                    webSocket.send(GsonInner.getGsonInstance().toJson(be));
//                }else {
//                    TSBError("webSocket is lost");
//                    LogUtil.d("webSocket lose to connect");
//                    webSocketLost = true;
//                    initWebSocket();
//                }
            }else if (isAt && isBarrageOpen){//@+漂屏
                String content = "{\"source\":\"" + UserPreferences.getAccId() + "\",\"type\":\"at\",\"contents\":\"" + " " + msg.toString() + "\",\"target\":\"" + atAccId + "\"}";
                sendBarrageMsg(content);
//                BarrageParams params = new BarrageParams();
//                params.setContent(content);
//                BarrageEntity be = new BarrageEntity();
//                be.setCmd("driftComment");
//                be.setParams(params);
//                if (null != webSocket && webSocket.isOpen()){
//                    webSocket.send(GsonInner.getGsonInstance().toJson(be));
//                }else {
//                    TSBError("webSocket is lost");
//                    LogUtil.d("webSocket lose to connect");
//                    webSocketLost = true;
//                    initWebSocket();
//                }
            }else {//普通文本消息
                NIMClient.getService(ChatRoomService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        LogUtil.e("消息发送成功");
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_CHATROOM_MUTED) {
                            TSBError("用户被禁言！");
                        } else {
                            LogUtil.e("消息发送失败： " + code);
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogUtil.e("消息发送出错: " + exception.toString());
                    }
                });
                messageList.add(message);
                if (messageList.size() > 100){
                    messageList.remove(0);
                }
                messageAdapter.notifyDataSetChanged();
                messageRecyclerView.smoothScrollToPosition(messageList.size());
            }
            messageEdit.setText("");
        }else {
            Toast.makeText(this, "请输入文字", Toast.LENGTH_SHORT).show();
        }
    }

    @Event(value = {R.id.view_close, R.id.view_close2}, type = View.OnTouchListener.class)
    private boolean closeInput(View view, MotionEvent motionEvent){
        LogUtil.d("Anchor on close keyBoard click");
        hideSoftInput(messageEdit);
        bottomLl.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
        messageEdit.setText("");
        pickerView.setVisibility(View.GONE);
        if (inputType == InputTypeEnum.EMOJI){
            inputType = InputTypeEnum.KEYBOARD;
            emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
        }
        if (isMenuShow){
            animationMenuClose(menu);
            menuLayout.setVisibility(View.GONE);
            menuLayout.setAnimation(AnimationUtil.moveToViewRight());
            isMenuShow = !isMenuShow;
        }
        return false;
    }

    @Event(R.id.iv_sliveroom_bottom_iv_bgm)
    private void bgmClick(View view){
        if (!isBGM) isBGM = !isBGM;
        bgmImg.setEnabled(false);
        openBGMOperate(bgmType, musicName, musicSingler);
    }

    @Event(R.id.iv_sliveroom_bottom_iv_menu)
    private void menuClick(View view){
        if (isMenuShow){
            animationMenuClose(view);
            menuLayout.setVisibility(View.GONE);
            menuLayout.setAnimation(AnimationUtil.moveToViewRight());
        }else {
            animationMenuOpen(view);
            menuLayout.setVisibility(View.VISIBLE);
            menuLayout.setAnimation(AnimationUtil.moveFromViewRight());
        }
        isMenuShow = !isMenuShow;
    }

    @Event(R.id.anchor_share)
    private void shareClick(View view){
        if (isMenuShow){
            animationMenuClose(menu);
            menuLayout.setVisibility(View.GONE);
            menuLayout.setAnimation(AnimationUtil.moveToViewRight());
            isMenuShow = !isMenuShow;
        }
        mShareAction.open(config);
    }

    @Event(R.id.emoji_buttonSingle)
    private void onEmojiClick(View view){
        if (inputType == InputTypeEnum.KEYBOARD){
            hideSoftInput(messageEdit);
            pickerView.setVisibility(View.VISIBLE);
            pickerView.show(this);
            emojiImg.setImageResource(R.drawable.icon_input_keyboard);
            inputType = InputTypeEnum.EMOJI;
        }else {
            pickerView.setVisibility(View.GONE);
            messageEdit.setFocusable(true);
            messageEdit.setFocusableInTouchMode(true);
            messageEdit.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
            inputType = InputTypeEnum.KEYBOARD;
        }
    }

    @Event(R.id.power_braager)
    private void onBraagerImgClick(View view){
        if (isBarrageOpen){
            braagerImg.setImageResource(R.drawable.barrage_close);
//            if (null == messageEdit.getText()){
                messageEdit.setHint("说点什么吧");
//            }
            isBarrageOpen = false;
        }else {
            braagerImg.setImageResource(R.drawable.barrage_open);
//            if (null == messageEdit.getText()){
                messageEdit.setHint("发送弹幕,0.1虎币/条");
//            }
            isBarrageOpen = true;
        }
    }

    private void animationMenuOpen(View view) {
        view.startAnimation(menuToDown);
    }

    private void animationMenuClose(View view) {
        view.startAnimation(menuToUp);
    }

    private void animationMenuLayoutOpen(View view){
        menuOpen.setFillAfter(!menuOpen.getFillAfter());
        view.startAnimation(menuOpen);
    }
    private void animationMenuLayoutClose(View view){
        menuClose.setFillAfter(!menuClose.getFillAfter());
        view.startAnimation(menuClose);
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
                                            enterChannel();
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

    private void updateChannelMemberMeg(){
        ChatRoomMemberUpdate chatRoomMemberUpdate = new ChatRoomMemberUpdate();
        updateMap.put("superUser",          LiveRoomCache.getSelf().isSuperUser());
        updateMap.put("guard",              LiveRoomCache.getSelf().getGuard());
        updateMap.put("vip",                LiveRoomCache.getSelf().getVip());
        updateMap.put("richScore",          LiveRoomCache.getSelf().getRichScore());
        updateMap.put("fansClub",           LiveRoomCache.getSelf().getFansClub());
        updateMap.put("clubName",           LiveRoomCache.getSelf().getClubName());
        updateMap.put("clubLevel",          LiveRoomCache.getSelf().getClubLevel());

        updateMap.put("guardDeadLine",      LiveRoomCache.getSelf().getGuardDeadLine());
        updateMap.put("score",              LiveRoomCache.getSelf().getScore());
        updateMap.put("face",               LiveRoomCache.getSelf().getFace());
        updateMap.put("name",               LiveRoomCache.getSelf().getName());
        updateMap.put("accId",              LiveRoomCache.getSelf().getAccId());
        updateMap.put("fansClubDeadLine",   LiveRoomCache.getSelf().getFansClubDeadLine());
        updateMap.put("vipDeadLine",        LiveRoomCache.getSelf().getVipDeadLine());
        chatRoomMemberUpdate.setExtension(updateMap);

        NIMClient.getService(ChatRoomService.class).updateMyRoomRole(LiveRoomCache.getRoomId(), chatRoomMemberUpdate, false, null).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.d("updateChannelMemberMeg success");
            }

            @Override
            public void onFailed(int i) {
                LogUtil.d("updateChannelMemberMeg onFailed : " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                LogUtil.d("updateChannelMemberMeg onException :" + throwable.toString());
            }
        });
    }

    private void openPush(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.OPENPUSH)
                .addParam("cId", LiveRoomCache.getChannelId())
                .addParam("device", SystemCache.DEVICE)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                LogUtil.d("open push :" + js);
                            }else {
                                stopAllStream();
                                doCompletelyFinish();//关播
                                mIsPublishing = false;
                                if (null != js.getString("msg")) TSBError(js.getString("msg"));
                                LogUtil.e("isStartLiving = false");
                            }
                        } catch (JSONException e) {
                            stopAllStream();
                            doCompletelyFinish();//关播
                            TSBError("无法开播，请检查网络！");
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void enterChannel(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.ENTERCHANNEL)
                .addParam("device", SystemCache.DEVICE)
                .addParam("cId", mRoomID)
                .addParam("sessionId", "0")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.get("code").equals(ResultCode.SUCCESS.getCode())) {
                                JSONObject data = js.getJSONObject("data");

                                String welcomeMsg = data.getString("welcome");
                                View heardView = LayoutInflater.from(ZegoAnchorActivity.this).inflate(R.layout.chatroommessage_item_header_layout,null);
                                TextView heardMsg = (TextView) heardView.findViewById(R.id.chatroommessage_heardview_heardmsg);
                                heardMsg.setText(welcomeMsg);
                                messageAdapter.addHeaderView(heardView);

                                Owner owner = GsonInner.getGsonInstance().fromJson(data.getJSONObject("owner").toString(), Owner.class);
                                Channel channel = GsonInner.getGsonInstance().fromJson(data.getJSONObject("channel").toString(), Channel.class);
                                Self self = GsonInner.getGsonInstance().fromJson(data.getJSONObject("self").toString(), Self.class);
                                LiveRoomCache.setOwner(owner);
                                LiveRoomCache.setChannel(channel);
                                LiveRoomCache.setSelf(self);
                                if (null != channel.getStatus()) {
                                    LogUtil.d("channel.getStatus()  :" + channel.getStatus());
                                    if (channel.getStatus().equals("1")) {
//                                        webSocketCallback.play(true);
                                    } else {
//                                        webSocketCallback.play(false);
                                    }
                                }
                                openPush();
                                showTop();
                                showBottom();
                                updateChannelMemberMeg();

                                if (owner.isAnchor()){
                                    XUtilsDownLoad.downloadOneFile(API.OSS_URL_CHANNELCOVER + channel.getCoverUrl(), SystemCache.BASE_PATH + SystemCache.ROOT_PATH + "FACE.png", new XUtilsDownLoadCallback() {
                                        @Override
                                        public void success() {
                                            LiveRoomCache.setCoverFile(SystemCache.BASE_PATH + SystemCache.ROOT_PATH + "FACE.png");
                                            LogUtil.d("封面下载成功！");
                                        }
                                    });
                                }
                            }else {
                                LogUtil.d("enter channel failed !");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void exitChannel(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.EXITCHANNEL)
                .addParam("cId", mRoomID)
                .addParam("sessionId", "0")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.get("code").equals(ResultCode.SUCCESS.getCode())) {
                                LogUtil.d("exit channel success !");
                            }else {
                                LogUtil.d("exit channel failed !");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

//    /**
//     * 创建WebSocket
//     */
//    public void initWebSocket() {
//        final String channelId = LiveRoomCache.getChannelId();
//        webSocket = new WebSocket(URI.create(API.WEBSOCKET), channelId, new WebSocket.WebSocketCallback() {
//            @Override
//            public void open(String msg) {//发送进场消息
//                LogUtil.d("webSocket is connected ~!");
//                if (webSocketLost){
//                    Params params = new Params()
//                            .setAccId(UserPreferences.getAccId())
//                            .setToken(UserPreferences.getToken())
//                            .setDevice(SystemCache.DEVICE)
//                            .setcId(channelId)
//                            .setGiftVersion(SysPreferences.getGiftVersion())
//                            .setMountVersion(SysPreferences.getMountVersion())
//                            .setAnimationVersion(SysPreferences.getAnimationVersion());
//                    Enter enter = new Enter()
//                            .setCmd("reEnter")
//                            .setParams(params);
//                    String enterMsg = GsonInner.getGsonInstance().toJson(enter);
//                    webSocket.send(enterMsg);
//                    webSocketLost = false;
//                }else {
//                    webSocket.send(msg);
//                }
////                cleanCallback.stopLode();
//            }
//
//            @Override
//            public void success() {//成功收到进场消息
//                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
//                        .addRouteUrl(API.OPENPUSH)
//                        .addParam("cId", LiveRoomCache.getChannelId())
//                        .addParam("device", SystemCache.DEVICE)
//                        .getResult(new HttpCallback() {
//                            @Override
//                            @HttpResultCut
//                            public void success(JSONObject js, boolean success) {
//                                try {
//                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
//                                        LogUtil.d("open push :" + js);
//                                    }else {
//                                        stopAllStream();
//                                        doCompletelyFinish();//关播
//                                        mIsPublishing = false;
//                                        if (null != js.getString("msg")) TSBError(js.getString("msg"));
//                                        LogUtil.e("isStartLiving = false");
//                                    }
//                                } catch (JSONException e) {
//                                    stopAllStream();
//                                    doCompletelyFinish();//关播
//                                    TSBError("无法开播，请检查网络！");
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                showTop();
//                showBottom();
//                //更新本人在聊天室内的信息
//                updateChannelMemberMeg();
//            }
//
//            @Override
//            public void play(boolean play) {//直播状态为：1（直播中）
//
//            }
//
//            @Override
//            public void failed(String failedMsg) {
//                LogUtil.e("WebSocket出错：" + failedMsg);
//            }
//
//            @Override
//            public void lost() {//WebSocket意外断开
//                webSocketLost = true;
//                initWebSocket();
//            }
//
//            @Override
//            public void welcome(final String welcomeMsg) {//收到直播间欢迎公告
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        View heardView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chatroommessage_item_header_layout,null);
//                        TextView heardMsg = (TextView) heardView.findViewById(R.id.chatroommessage_heardview_heardmsg);
//                        heardMsg.setText(welcomeMsg);
//                        messageAdapter.addHeaderView(heardView);
//                    }
//                });
//            }
//
//            @Override
//            public void recharge(String msg) {
//            }
//
//            @Override
//            public void guard(String msg) {
//
//            }
//        });
//        webSocket.connect();
//    }

    /**
     * 进入云信聊天室
     */
    public void enterRoom(String roomId){
        try{
            EnterChatRoomData enterData = new EnterChatRoomData(roomId);
            enterData.setAvatar(com.i5i58.live.common.utils.string.StringUtil.checkIcon(UserPreferences.getFace()));
            enterData.setNick(SystemCache.getPersonalMsg().getAccount().getName());
            NIMClient.getService(ChatRoomService.class).enterChatRoom(enterData).setCallback(new RequestCallback() {
                @Override
                public void onSuccess(Object param) {
                    registerObservers(true);
                    LogUtil.d(param.toString());
                }

                @Override
                public void onFailed(int code) {
                    LogUtil.d(code+"");
                }

                @Override
                public void onException(Throwable exception) {
                    LogUtil.d(exception.toString());
                }
            });
        }catch (IllegalArgumentException e){
            LogUtil.e(e.toString());
        }
    }

    private void showTop(){
        if (topLl.getVisibility() == View.VISIBLE){
            return;
        }
        try {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
                    Account account = SystemCache.getPersonalMsg().getAccount();
                    if (account == null) {
                        if (topLl.getVisibility() == View.GONE){
                            topLl.setVisibility(View.VISIBLE);
                            topLl.setAnimation(AnimationUtil.moveFromViewTop());
                        }
                        return;
                    }
                    Glide.with(TigerApplication.getInstance().getApplicationContext()).load(com.i5i58.live.common.utils.string.StringUtil.checkIcon(account.getFaceSmallUrl()))
                            .error(R.drawable.icon_mine_personal_default)
                            .transform(new GlideCircleTransform(TigerApplication.getInstance().getApplicationContext()))
                            .into(anchorIcon);
                    if (null != account.getStageName()){
                        anchorName.setText(account.getStageName());
                    }else if (null != account.getName()){
                        anchorName.setText(account.getName());
                    }else {
                        anchorName.setText(account.getOpenId());
                    }
                    if (null != LiveRoomCache.getChannel().getPlayerTimes() && !"".equals(LiveRoomCache.getChannel().getPlayerTimes()))
                    watchTimes = Long.valueOf(LiveRoomCache.getChannel().getPlayerTimes());
                    onlineTxt.setText(watchTimes + "人次");
                    cIdTxt.setText(LiveRoomCache.getChannel().getChannelId());
                    contributionTxt.setText(String.valueOf(LiveRoomCache.getChannel().getWeekOffer()));
                    if (topLl.getVisibility() == View.GONE){
                        topLl.setVisibility(View.VISIBLE);
                        topLl.setAnimation(AnimationUtil.moveFromViewTop());
                    }
//                }
//            });
        }catch (Exception e){
            LogUtil.e(e.toString());
            MobclickAgent.reportError(this, "CATCH >>> " + e.toString());
        }

    }

    private void showBottom(){
        if (bottomLl.getVisibility() == View.VISIBLE){
            return;
        }
        try {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
                    if (bottomLl.getVisibility() == View.GONE){
                        bottomLl.setVisibility(View.VISIBLE);
                        bottomLl.setAnimation(AnimationUtil.moveFromViewBootom());
                    }
//                }
//            });
        }catch (IllegalArgumentException e){
            LogUtil.e(e.toString());
        }

    }

    /**
     * 注册接收消息
     * @param register
     */
    public void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.ChatRoom
                && message.getSessionId() != null
                && message.getSessionId().equals(LiveRoomCache.getRoomId());
    }

    private int getGiftType(int count) {
        int i = 0;
        if (count >= 1314) {
            i = 6;
        } else if (count >= 520) {
            i = 5;
        } else if (count >= 188) {
            i = 4;
        } else if (count >= 66) {
            i = 3;
        } else if (count >= 30) {
            i = 2;
        } else if (count >= 10) {
            i = 1;
        }
        return i;
    }

    private void showMessage(String cmd, Map<?, ?> data) {
        try {
            LogUtil.d("ChatRoomMessage CMD: " + cmd);
            switch (cmd){
                case ENTER:
                    watchTimes++;
                    if (null != data.get("mtsId")) {
                        String mountsType = null;
                        if (null != data.get("gMtsId")) {
                            mountsType = data.get("gMtsId").toString();
                            LogUtil.d("with:" + SystemCache.getScreenWith());
                            LogUtil.d("with:" + SystemCache.getScreenHeight());
                            LogUtil.d("mountsType:" + mountsType);
                        }else if (null != data.get("mtsId")) {
                            mountsType = data.get("mtsId").toString();
                            LogUtil.d("with:" + SystemCache.getScreenWith());
                            LogUtil.d("with:" + SystemCache.getScreenHeight());
                            LogUtil.d("mountsType:" + mountsType);
                        }
                        if (LiveRoomCache.isMountOpen()){
                            MountConfig mountConfig = LiveRoomCache.getMountConfigMap().get(Integer.valueOf(mountsType));
                            if (null != mountConfig && null != mountConfig.getFunction() && mountConfig.getFunction().equals("playMount")){
                                gameEngine.callEgretInterface("playMount", mountsType + "|" + SystemCache.getScreenWith() + "|" + SystemCache.getScreenHeight());
                            }
                        }
                    }
                    break;
                case GIFT:
                    String giftId = data.get("id").toString();
                    GiftConfig giftConfig = LiveRoomCache.getGiftConfigMap().get(Integer.valueOf(giftId));
                    if (LiveRoomCache.isGiftOpen()){
                        String function = giftConfig.getFunction();
                        if (!"0".equals(giftId) && null != function && !"".equals(function) && null != data.get("condition") && data.get("condition").toString().equals("true")) {
                            LogUtil.d("giftId:" + giftId);
                            gameEngine.callEgretInterface("playGift", giftId + "|" + SystemCache.getScreenWith() + "|" + SystemCache.getScreenHeight());
                        }
                    }
                    String giftUrl = API.OSS_URL_GIFT + giftConfig.getPath() + ".png";
                    String nickName = data.get("name").toString();
                    int giftCtis = Integer.valueOf(data.get("ctis").toString());
                    String fromPic = "";
                    if (null != data.get("face")){
                        fromPic = com.i5i58.live.common.utils.string.StringUtil.checkIcon(data.get("face").toString());
                    }
                    int giftCount = Integer.valueOf(data.get("ct").toString());
                    giftModel = new GiftModel(giftId, giftConfig.getName(), 1, giftUrl, "accId", nickName, fromPic, System.currentTimeMillis(), getGiftType(giftCount), giftCount, giftCtis);
                    giftControl.loadGift(giftModel);
                    //刷新贡献榜
                    if (null != data.get("weekOffer") && null != contributionTxt){
                        contributionTxt.setText(data.get("weekOffer").toString());
                    }
                    break;
                case GIVEHEART:
                    m_box2dFgm.addBall(new Random().nextBoolean());
                    break;
                case DRIFTCOMMENT:
                    try {
                        JSONObject content = new JSONObject(data.get("content").toString());
                        String type = content.getString("type");
                        if (null == type) {
                            return;
                        }
                        if (type.equals("text")) {
                            Map<String, String> map = new HashMap<>();
                            if (null != data.get("face")) {
                                map.put("icon", data.get("face").toString());
                            }
                            if (null != data.get("vip")) {
                                map.put("vip", data.get("vip").toString());
                            }
                            if (null != data.get("guard")) {
                                map.put("guard", data.get("guard").toString());
                            }
                            if (null != data.get("richScore")) {
                                map.put("lv", data.get("richScore").toString());
                            }
                            if (null != data.get("name")) {
                                map.put("name", data.get("name").toString());
                            }
                            if (null != data.get("content")) {
                                map.put("content", content.get("contents").toString());
                            }
                            containerView.generateItem(map);
                        } else if (type.equals("at")) {
                            String taAccId = content.getString("target");
                            String myAccId = SystemCache.getPersonalMsg().getAccount().getAccId();
                            String fromAccId = content.getString("source");
                            Map<String, String> map = new HashMap<>();
                            if (null != data.get("face")) {
                                map.put("icon", data.get("face").toString());
                            }
                            if (null != data.get("vip")) {
                                map.put("vip", data.get("vip").toString());
                            }
                            if (null != data.get("guard")) {
                                map.put("guard", data.get("guard").toString());
                            }
                            if (null != data.get("richScore")) {
                                map.put("lv", data.get("richScore").toString());
                            }
                            if (null != data.get("name")) {
                                map.put("name", data.get("name").toString());
                            }
                            if (null != data.get("content")) {
                                map.put("content", content.get("contents").toString());
                            }
                            if (myAccId.equals(taAccId) || myAccId.equals(fromAccId)){
                                map.put("color", "1");
                            }
                            containerView.generateItem(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case OPENCLUB:
                case OPENGUARD:
                case FOLLWOANCHOR:

                    break;
                default:
                    break;
            }
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }catch (Resources.NotFoundException e){
            LogUtil.e(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 聊天室消息
     */
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {// 处理新收到的消息
            if (messages == null || messages.isEmpty()) {
                return;
            }
            for (ChatRoomMessage message: messages){
                MsgTypeEnum msgType = message.getMsgType();
                if (message.getMsgType() == MsgTypeEnum.custom){//自定义文本消息
                    if (isMyMessage(message)){
                        Map<?, ?> customMsg = message.getRemoteExtension();
                        if (null != customMsg){
                            String cmd = customMsg.get("cmd").toString();
                            LogUtil.d("customMsg============" + cmd);
                            if ("exit".equals(cmd)) return;
                            if ("micSeqChanged".equals(cmd)) return;
                            if ("openPush".equals(cmd)) return;
                            if ("kick".equals(cmd)){
                                Map kickMap = (Map) customMsg.get("data");
                                if (null != kickMap && null != kickMap.get("accId") && kickMap.get("accId").equals(UserPreferences.getAccId())){
                                    Toast.makeText(ZegoAnchorActivity.this, "您已被房管踢出！", Toast.LENGTH_SHORT).show();
                                    onTakeSnapshotResult();
                                    closeAct(ZegoAnchorActivity.this);
                                    return;
                                }
                            }else if ("guardKick".equals(cmd)){
                                Map kickMap = (Map) customMsg.get("data");
                                String gAccId = ((Map)kickMap.get("guard")).get("accId").toString();
                                String gName = ((Map)kickMap.get("guard")).get("name").toString();
                                String kAccId = ((Map)kickMap.get("kicked")).get("accId").toString();
                                String kName = ((Map)kickMap.get("kicked")).get("name").toString();
                                String guardName = gName == null? gAccId:gName;
                                String kickEdName = kName == null? kAccId:kName;
//                                    Toast.makeText(getActivity(), kickEdName + "被" + guardName + "踢出房间！", Toast.LENGTH_SHORT).show();
                                if (kAccId.equals(UserPreferences.getAccId())){
                                    onTakeSnapshotResult();
                                    closeAct(ZegoAnchorActivity.this);
                                    return;
                                }
                            }else if ("setMute".equals(cmd)){
                                Map muteMap = (Map) customMsg.get("data");
                                if (muteMap.get("accId").equals(UserPreferences.getAccId()))
                                    Toast.makeText(ZegoAnchorActivity.this, muteMap.get("content").toString(), Toast.LENGTH_SHORT).show();
                            }
                            messageList.add(message);
                            if (messageList.size() > 100){
                                messageList.remove(0);
                            }
                            messageAdapter.notifyDataSetChanged();
                            if (!messageRecyclerView.canScrollVertically(1)){//表示是否能向上滚动，false表示已经滚动到底部
                                messageRecyclerView.smoothScrollToPosition(messageList.size());
                            }else {
                                scrollToBottom.setVisibility(View.VISIBLE);
                            }
                            LogUtil.d("customMsg============");
                            Map<?, ?> data = (Map<?, ?>) customMsg.get("data");
                            showMessage(cmd, data);
                        }
                    }
                }else if (message.getMsgType() == MsgTypeEnum.text){//普通文本消息
                    if (isMyMessage(message)){
//                        if (null == message.getContent() || "".equals(message.getContent())) return;
                        messageList.add(message); if (messageList.size() > 100){
                            messageList.remove(0);
                        }
                        messageAdapter.notifyDataSetChanged();
                        if (!messageRecyclerView.canScrollVertically(1)){//表示是否能向上滚动，false表示已经滚动到底部
                            messageRecyclerView.smoothScrollToPosition(messageList.size());
                        }else {
                            scrollToBottom.setVisibility(View.VISIBLE);
                        }
                        LogUtil.d("text============");
                    }
                }
            }
        }
    };

    /**
     * 普通消息
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(final List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            for (IMMessage msg : messages) {
                if (null != msg.getAttachment() && msg.getAttachment() instanceof SingleChatAttachment){
                    SingleChatAttachment attachment = (SingleChatAttachment) msg.getAttachment();
                    if (attachment.getcId().equals(LiveRoomCache.getChannelId())){
                        singleImg.setImageResource(R.drawable.newmsg);
                    }
                }
            }
        }
    };

    @Override
    public void exit() {

    }

    @Override
    public void onEmojiSelected(String key) {
        if (key.equals("/DEL")) {
            messageEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = messageEdit.getSelectionStart();
            int end = messageEdit.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            messageEdit.getText().replace(start, end, key);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {

    }

    //Egret
    private interface IRuntimeInterface {
        void callback(String message);
        // 因为遗留问题 callBack 也是接受的
    }

    private void setLoaderUrl(int mode) {
        switch (mode) {
            case 2:
                // local DEBUG mode
                // 本地DEBUG模式，发布请使用0本地zip，或者1网络获取zip
                break;
            case 1:
                // http request zip RELEASE mode, use permission INTERNET
                // 请求网络zip包发布模式，需要权限 INTERNET
                loaderUrl = SysPreferences.getAnimationZip();
                updateUrl = SysPreferences.getAnimationRes();
                LogUtil.d("loadUrl:" + loaderUrl);
                LogUtil.d("updateUrl:" + updateUrl);
                break;
            default:
                // local zip RELEASE mode, default mode, `egret publish -compile --runtime native`
                // 私有空间zip包发布模式, 默认模式, `egret publish -compile --runtime native`
                break;
        }
    }

    private HashMap<String, Object> getGameOptions() {
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put(EgretRuntime.OPTION_EGRET_GAME_ROOT, egretRoot);
        options.put(EgretRuntime.OPTION_GAME_ID, gameId);
        options.put(EgretRuntime.OPTION_GAME_LOADER_URL, loaderUrl);
        options.put(EgretRuntime.OPTION_GAME_UPDATE_URL, updateUrl);
        options.put(EgretRuntime.OPTION_GAME_GLVIEW_TRANSPARENT, "true");
        if (bUsingPlugin) {
            String pluginConf = "{'plugins':[{'name':'androidca','class':'org.egret.egretframeworknative.CameraAudio','types':'jar,so'}]}";
            options.put(EgretRuntime.OPTION_GAME_GLVIEW_TRANSPARENT, "true");
            options.put(EgretRuntime.OPTION_EGRET_PLUGIN_CONF, pluginConf);
        }
        return options;
    }

    private void setInterfaces() {
        // Egret（TypeScript）－Runtime（Java）通讯
        // setRuntimeInterface(String name, IRuntimeInterface interface) 用于设置一个runtime的目标接口
        // callEgretInterface(String name, String message) 用于调用Egret的接口，并传递消息
        gameEngine.setRuntimeInterface("RuntimeInterface", new IRuntimeInterface() {
            @Override
            public void callback(String message) {
                gameEngine.callEgretInterface("EgretInterface", "A message from runtime");
            }
        });

        gameEngine.setRuntimeInterface("sendToNative", new IRuntimeInterface() {
            @Override
            public void callback(String message) {
                Log.d("externalInterface", "message : " + message);
            }
        });
    }

    /**
     * 实例化Egret
     */
    public void initEgret(Context context) {
        egretRoot = new File(context.getFilesDir(), EGRET_ROOT).getAbsolutePath();
        gameId = "local";
        gameEngine = new EgretGameEngine();
        setLoaderUrl(1);
        HashMap<String, Object> options = getGameOptions();
        gameEngine.game_engine_set_options(options); // 设置游戏的选项  (set game options)
        gameEngine.game_engine_set_loading_view(new GameLoadingView(context));// 设置加载进度条  (set loading progress bar)
        setInterfaces();// 创建Egret<->Runtime的通讯 (create pipe between Egret and Runtime)
        gameEngine.game_engine_init(context);// 初始化并获得渲染视图 (initialize game engine and obtain rendering view)
        engineInited = true;
        View gameEngineView = gameEngine.game_engine_get_view();

        ViewGroup vg = ((ViewGroup) gameEngineView);
        vg.removeViewAt(0);//去掉。保留这个控件是为了兼容2.X 的引擎，默认是存在的，如果使用3.0以上的引擎版本则可以去掉

        GL2JNIView mySurfaceView = ((GL2JNIView) vg.getChildAt(0));
        mySurfaceView.setZOrderOnTop(true);

        SurfaceHolder mSurfaceHolder = mySurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        layout1.addView(gameEngineView);
        gameEngineView.setVisibility(View.INVISIBLE);
    }

    private void initOperateView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        talkImg.setVisibility(View.VISIBLE);
        singleImg.setVisibility(View.VISIBLE);
        personLayout.getBackground().setAlpha(100);
        contributionLayout.getBackground().setAlpha(100);
        cIdLayout.getBackground().setAlpha(100);
        adTxt.getBackground().setAlpha(100);

        menuToDown    = AnimationUtils.loadAnimation(this, R.anim.set_anchor_menu_close);
        menuToDown.setFillAfter(true);
        menuToDown.setInterpolator(new LinearInterpolator());

        menuToUp      = AnimationUtils.loadAnimation(this, R.anim.set_anchor_menu_open);
        menuToUp.setFillAfter(true);
        menuToUp.setInterpolator(new LinearInterpolator());

        LinearLayoutManager manager = new ScrollSpeedLinearLayoutManger(this);
        manager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(manager);
        messageRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        messageAdapter = new ChatRoomMessageAdapter(this, messageList, this);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        messageRecyclerView.addItemDecoration(new SpacesItemDecoration(0, 15, getResources().getColor(R.color.transparent)));
        messageRecyclerView.setVisibility(View.VISIBLE);
        messageAdapter.setOnItemClickListener(new ChatRoomMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.d("" + position);
                if (position == 0) return;
                ChatRoomMessage roomMessage = messageList.get(position - 1);
                if (null == roomMessage){
                    return;
                }
                String memberAaccId = roomMessage.getFromAccount();
                LogUtil.d(memberAaccId);
                if (memberAaccId == null){
                    return;
                }
                PersonalDialogFragment fragment = new PersonalDialogFragment(memberAaccId, false, true, new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {

                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {
                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getSupportFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String at) {
                        isAt = true;
                        atAccId = accId;
                        atTarget = at;
                        bottomLl.setVisibility(View.GONE);
                        messageLayout.setVisibility(View.VISIBLE);
                        messageEdit.setFocusable(true);
                        messageEdit.setFocusableInTouchMode(true);
                        messageEdit.requestFocus();
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        Bitmap atBitmap = BitmapUtil.createBitmap(at + " ");
                        if (null != atBitmap){
                            //根据Bitmap对象创建ImageSpan对象
                            ImageSpan imageSpan = new ImageSpan(getApplicationContext(), atBitmap);
                            SpannableString spannableString = new SpannableString(at);
                            spannableString.setSpan(imageSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            messageEdit.setText(spannableString + " ");
                            messageEdit.setSelection(spannableString.length() + 1);
                        }
                    }
                });
                fragment.show(getSupportFragmentManager(),PersonalDialogFragment.class.getSimpleName());
            }
        });

        giftControl = new GiftControl(giftFrameLayout1, giftFrameLayout2);
        periscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periscope.addHeart();
            }
        });

        messageEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        messageEdit.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pickerView.setVisibility(View.GONE);
                    imm.showSoftInput(messageEdit, 0);
                    emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
                    inputType = InputTypeEnum.KEYBOARD;
                }
                return false;
            }
        });
        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null !=s && 50 < s.toString().trim().length()){
                    Toast.makeText(ZegoAnchorActivity.this, "最多只能输入50个字哦！", Toast.LENGTH_SHORT).show();
                    String text = s.toString().trim().substring(0, 50);
                    messageEdit.setText(text);
                    messageEdit.setSelection(text.length());
                }
            }
        });

        filterLayoutBottom.getBackground().setAlpha(200);
        FilterAdapter filterAdapter = new FilterAdapter(this);
        horizontalListView.setAdapter(filterAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtil.d("horizontalListView onItemClick : " + i);
                mZegoLiveRoom.setFilter(i);
            }
        });
        seekBarFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.d("change beautyLevel : " + seekBar.getProgress());
                mZegoLiveRoom.setPolishStep((float) seekBar.getProgress());
            }
        });

        seekBarFace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.d("change FilterLevel : " + seekBar.getProgress());
                mZegoLiveRoom.setWhitenFactor(seekBar.getProgress() / 100);// 取值范围[0,1], 参数越大亮度越暗
            }
        });
        initUmeng();
        fetchOnlineCount();
        getWallet();
    }

    private void getWallet() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETWALLET)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                String iGold = js.getJSONObject("data").getString("iGold");
                                if (null == iGold) return;
                                double iGoldF = Double.valueOf(iGold) / 100;
                                iGold = String.valueOf(iGoldF);
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith(".")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                wallet = iGold;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // 一分钟轮询一次在线人数
    private void fetchOnlineCount() {
        if (timer == null) {
            timer = new Timer();
        }

        //开始一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 800, 800);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    periscope.addHeart();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 实例化沙发列表
     */
    private void initSofaList() {
        sofaListAdapter = new SofaListAdapter(this, sofaAccountList);
        sofaRecyclerView.setAdapter(sofaListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        sofaRecyclerView.setLayoutManager(linearLayoutManager);
        sofaListAdapter.setOnItemClickLitener(new SofaListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonalDialogFragment fragment = new PersonalDialogFragment(sofaAccountList.get(position).getAccId(), false, true, new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {
                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {
                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getSupportFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String at) {
                        isAt = true;
                        atAccId = accId;
                        atTarget = at;
                        bottomLl.setVisibility(View.GONE);
                        messageLayout.setVisibility(View.VISIBLE);
                        messageEdit.setFocusable(true);
                        messageEdit.setFocusableInTouchMode(true);
                        messageEdit.requestFocus();
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        Bitmap atBitmap = BitmapUtil.createBitmap(at);
                        if (null != atBitmap){
                            //根据Bitmap对象创建ImageSpan对象
                            ImageSpan imageSpan = new ImageSpan(getApplicationContext(), atBitmap);
                            SpannableString spannableString = new SpannableString(at);
                            spannableString.setSpan(imageSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            messageEdit.setText(spannableString);
                            messageEdit.setSelection(spannableString.length());
                        }
                    }
                });
                fragment.show(getSupportFragmentManager(),PersonalDialogFragment.class.getSimpleName());
            }
        });
        getSofaListData();
    }

    /**
     * 沙发列表数据刷新
     */
    public void getSofaListData() {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.SOFALIST)
                .addParam("cId", LiveRoomCache.getChannelId())
                .addParam("pageNum", "0")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.getString("code").equals("success") && js.has("data")) {
                                    JSONArray array = js.getJSONArray("data");
                                    if (null != array && array.length() > 0){
                                        sofaAccountList.clear();
                                        sofaAccountList = GsonUtil.getSofaAccountList(array.toString(),sofaAccountList);
                                        sofaListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 举报
     */
    private void report(String taAccId, String reason){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.REPORT)
                .addParam("reportedUser", taAccId)
                .addParam("reason", reason)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.get("code").toString().equals("success")) {
                                    TSBSuccess("举报成功！");
                                }else{
                                    LogUtil.e("举报出错");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initBox2d() {
        m_box2dFgm = new Box2DFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.lyt_container, m_box2dFgm).commit();
        showBox2dFgmNormalScreen();
    }

    private void showBox2dFgmFullScreen() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m_container.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        m_container.setLayoutParams(params);
    }

    private void showBox2dFgmNormalScreen() {
        int width = ScreenParamUtil.GetScreenWidthPx(this);
        int height = ScreenParamUtil.GetScreenHeightPx(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m_container.getLayoutParams();
        params.width = width;
        params.height = height;
        m_container.setLayoutParams(params);
    }

    /**
     * 初始化OSS
     */
    private void initOSS() {
        if (isChangeCoverImg){//修改封面图
            Luban.get(this)
                    .load(new File(images.get(1).path))//传入要压缩的图片
                    .putGear(Luban.SECOND_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onSuccess(final File file) {//上传高清图
                            String orgUuid = UUID.randomUUID().toString();
                            upLoadPicture(file.getPath(),orgUuid);
                        }
                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("图片压缩失败");
                        }
                    }).launch();    //启动压缩
        }
        if (isChangeTitle){//修改标题
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.CHANNELTITLE)
                    .addParam("title", title.getText().toString())
                    .addParam("cId", LiveRoomCache.getChannelId())
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals("success")) {
                                    LogUtil.e("title已更新");
                                } else {
                                    LogUtil.e("title更新出错");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                            }
                        }
                    });
        }

    }

    //上传图片
    private void upLoadPicture(String filePath, final String uuid) {
        PutObjectRequest put = new PutObjectRequest("gg78live", "ChannelCover/" + uuid, FileUtils.File2byte(filePath));
        TigerApplication.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                            .addRouteUrl(API.COVERIMAGE)
                            .addParam("imageUrl", uuid)
                            .addParam("cId", LiveRoomCache.getChannelId())
                            .getResult(new HttpCallback() {
                                @Override
                                @HttpResultCut
                                public void success(JSONObject js, boolean success) {
                                    try {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.e("封面已更新");
                                        } else {
                                            LogUtil.e("更新封面出错");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                    }
                                }
                            });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
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

    private int getPlatformIndex(SHARE_MEDIA platform){
        int index = 0;
        if (platform == SHARE_MEDIA.SINA)               index = 0;
        else if (platform == SHARE_MEDIA.WEIXIN)        index = 1;
        else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) index = 2;
        else if (platform == SHARE_MEDIA.QQ)            index = 4;
        else if (platform == SHARE_MEDIA.QZONE)         index = 5;
        return index;

    }

    /**
     * 初始化Umeng
     */
    private void initUmeng() {
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        new ShareAction(ZegoAnchorActivity.this)
                                .setPlatform(share_media)
                                .withMedia(setUmWeb())
                                .setCallback(umShareListener)
                                .share();
                    }
                });
        config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setTitleText("分享到");
        config.setTitleTextColor(R.color.black);
        config.setIndicatorVisibility(false);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
    }

    private UMWeb setUmWeb() {
        String name = "";
        if (null != LiveRoomCache.getOwner().getName()){
            name = LiveRoomCache.getOwner().getName();
        }else if (null != LiveRoomCache.getOwner().getStageName()){
            name = LiveRoomCache.getOwner().getStageName();
        }else {
            name = LiveRoomCache.getOwner().getOpenId();
        }
        String iconUrl = LiveRoomCache.getChannel().getCoverUrl();
        UMImage image = new UMImage(ZegoAnchorActivity.this, API.OSS_URL_CHANNELCOVER + iconUrl);//网络图片
        UMWeb web = new UMWeb(SystemCache.getShareUrl() + LiveRoomCache.getChannelId());
        web.setTitle(name + "正在直播");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("再不看直播你就掉队啦，主播" + name + "正在直播，快让主播带你飞。");//描述
        return web;
    }

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(final SHARE_MEDIA platform) {
            //分享开始的回调
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.SHARE)
                    .addParam("cId", LiveRoomCache.getChannelId())
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                    LogUtil.d("post  share success !");
                                    //分享后公屏消息
                                    ChatRoomMessage shareMsg = ChatRoomMessageBuilder.createChatRoomTextMessage(LiveRoomCache.getRoomId(), "分享了直播间！");
                                    Map<String, Object> ext = new HashMap<>();
                                    ext.put("cmd", "share");
                                    ext.put("platform", getPlatformIndex(platform));
                                    shareMsg.setRemoteExtension(ext);
                                    NIMClient.getService(ChatRoomService.class).sendMessage(shareMsg, false).setCallback(new RequestCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void param) {
                                            LogUtil.e("消息发送成功");
                                        }

                                        @Override
                                        public void onFailed(int code) {
                                            if (code == ResponseCode.RES_CHATROOM_MUTED) {
                                                LogUtil.e("用户被禁言！");
                                            } else {
                                                LogUtil.e("消息发送失败： " + code);
                                            }
                                        }

                                        @Override
                                        public void onException(Throwable exception) {
                                            LogUtil.e("消息发送出错: " + exception.toString());
                                        }
                                    });

                                    messageList.add(shareMsg);
                                    if (messageList.size() > 100){
                                        messageList.remove(0);
                                    }
                                    messageAdapter.notifyDataSetChanged();
                                    messageRecyclerView.smoothScrollToPosition(messageList.size());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("Umeng  share success !");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            TSBError("分享失败");
            if(t!=null){
                LogUtil.e("分享失败：" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            TSBSuccess("分享已取消");
        }
    };

    //**********************************************************************************************************************************************

    //初始化ImagePicker
    private void configImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerGLideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);  //显示拍照按钮
        imagePicker.setMultiMode(false);  //多选
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(SystemCache.getScreenWith() - 150);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(SystemCache.getScreenWith() - 150);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
    }

    //主播进入直播间
    private void joinChannel() {
        enterRoom(LiveRoomCache.getRoomId());
        initOperate();
    }

    //主播开播通知
    private void masterEnterRoom() {
        new Thread(){
            @Override
            public void run() {
                int i = 3;
                while (i >= 0){
                    try {
                        Message msg = mHanlder.obtainMessage();
                        msg.obj = i;
                        mHanlder.sendMessage(msg);
                        i--;
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void clearEgret(){
        if (engineInited) gameEngine.game_engine_onStop();
    }

    private void closeLive() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CLOSEPUSH)
                .addParam("cId", LiveRoomCache.getChannelId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                LogUtil.d("关闭推流");
                                livaTime.stop();
                                LogUtil.e("livaTime stop");
                            } else {
                                LogUtil.d("关闭推流出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    /**
     * 清理缓存
     */
    private void clean(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        clearEgret();
        topLl.setVisibility(View.GONE);
        bottomLl.setVisibility(View.GONE);
        messageRecyclerView.setVisibility(View.GONE);
        NIMClient.getService(ChatRoomService.class).exitChatRoom(LiveRoomCache.getChannelId());
        registerObservers(false);
        LiveRoomCache.clearLiveRoomCache();
    }

    private void closeAct(){
        TigerApplication.getInstance().isLiving = false;
        if (mIsPublishing) {
            if(startLiveBefore.getVisibility() == View.VISIBLE) {
                doCompletelyFinish();
            } else {
                closeAvPopWindow();
            }
        } else {
            clean();
            closeAct(this);
        }
    }

    //请求直播权限
    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    private void initView() {
        try {
            if (null == coverUrl){
                Glide.with(TigerApplication.getInstance().getApplicationContext()).load(R.drawable.bg_anchor_setcoverimage)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                coverDraw = resource;
                            }
                        });
            }else {
                Glide.with(TigerApplication.getInstance().getApplicationContext()).load(API.OSS_URL_CHANNELCOVER + coverUrl)
                        .placeholder(R.drawable.bg_anchor_setcoverimage)
                        .error(R.drawable.bg_anchor_setcoverimage)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                coverDraw = resource;
                                coverImage.setImageDrawable(resource);
                            }
                        });
                hasCoverImg = true;
            }
            if (null != titleText){
                title.setText(titleText);
                hasTitle = true;
            }
        }catch (Exception e){
            LogUtil.e(e.toString());
        }

    }

    private void getParentParame() {
        coverUrl = getIntent().getStringExtra("coverUrl");
        titleText = getIntent().getStringExtra("title");
        mRoomID = getIntent().getStringExtra(IntentExtra.ROOM_ID);

        mPublishTitle = UserPreferences.getOpenId() + "_" + UserPreferences.getNickName();
        mPublishStreamID = mRoomID;

        mliveStreamingURL = getIntent().getStringExtra("mediaPath");
        mVideoResolution = getIntent().getStringExtra("videoResolution");
        mUseFilter = getIntent().getBooleanExtra("filter", false);
        coverUrl = getIntent().getStringExtra("coverUrl");
        titleText = getIntent().getStringExtra("title");
        pushBit = getIntent().getLongExtra("pushBit", 600);
        pushFPS = getIntent().getIntExtra("pushFPS", 15);
        pushHard = getIntent().getBooleanExtra("pushBit", false);

        if (getIntent().getStringExtra("markMode").equals("1")){
            markMode = 1;
        }else {
            markMode = 0;
        }

        switch (getIntent().getStringExtra("videoQuality")){
            case "1" :
                videoQuality = ZegoAvConfig.Level.VeryLow;
                break;
            case "2" :
                videoQuality = ZegoAvConfig.Level.Low;
                break;
            case "3" :
                videoQuality = ZegoAvConfig.Level.Generic;
                break;
            case "4" :
                videoQuality = ZegoAvConfig.Level.High;
                break;
            case "5" :
                videoQuality = ZegoAvConfig.Level.VeryHigh;
                break;
            case "6" :
                videoQuality = ZegoAvConfig.Level.SuperHigh;
                break;
            default:
                break;
        }
    }

    protected PhoneStateListener mPhoneStateListener = null;
    protected boolean mHostHasBeenCalled = false;
    /**
     * 电话状态监听.
     */
    protected void initPhoneCallingListener() {
        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mHostHasBeenCalled) {
                            mHostHasBeenCalled = false;
                            startPublish();
                        }

                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        LogUtil.d( ": call state ringing");
                        TSBSuccess("接听电话将停止直播！");
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if(!mHostHasBeenCalled){
                            mHostHasBeenCalled = true;
                            stopAllStream();
                        }
                        break;
                }
            }
        };

        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    //初始化操作界面
    private void initOperate() {
        operateLayout.setVisibility(View.VISIBLE);
        checkResource();
        initEgret(ZegoAnchorActivity.this);
        initOperateView();
        initSofaList();
        initBox2d();
    }

    /**
     * 启动入口.
     *
     * @param activity     源activity
     * @param publishTitle 视频标题
     */
    public static void actionStart(Activity activity,
                                   String publishTitle,
                                   boolean enableFrontCam,
                                   boolean enableTorch,
                                   int selectedBeauty,
                                   int selectedFilter,
                                   int appOrientation,
                                   JSONObject data,
                                   String roomId) {
        try {
            Intent intent = new Intent(activity, ZegoAnchorActivity.class);
            intent.putExtra(IntentExtra.PUBLISH_TITLE, publishTitle);
            intent.putExtra(IntentExtra.ENABLE_FRONT_CAM, enableFrontCam);
            intent.putExtra(IntentExtra.ENABLE_TORCH, enableTorch);
            intent.putExtra(IntentExtra.SELECTED_BEAUTY, selectedBeauty);
            intent.putExtra(IntentExtra.SELECTED_FILTER, selectedFilter);
            intent.putExtra(IntentExtra.APP_ORIENTATION, appOrientation);
            intent.putExtra(IntentExtra.ROOM_ID, roomId);

            String mUrlMedia = data.getString("pushUrl");
            String mVideoResolution = "SD";
            intent.putExtra("mediaPath", mUrlMedia);
            intent.putExtra("videoResolution", mVideoResolution);
            intent.putExtra("filter", true);
            intent.putExtra("coverUrl",     data.getString("coverUrl"));
            intent.putExtra("title",        data.getString("title"));
            intent.putExtra("videoType",    data.getString("pushMode"));
            intent.putExtra("markMode",     data.getString("pushMark"));
            intent.putExtra("videoQuality", data.getString("pushQuality"));
            intent.putExtra("pushBit",      data.getLong("pushBit"));
            intent.putExtra("pushFPS",      data.getInt("pushFPS"));
            intent.putExtra("pushHard",     data.getBoolean("pushHard"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //推流标记, PublishFlag.JoinPublish:连麦, PublishFlag.MixStream:混流, PublishFlag.SingleAnchor:单主播
    protected int mPublishFlag = ZegoConstants.PublishFlag.SingleAnchor;
    private ZegoLiveRoom mZegoLiveRoom;
    private boolean tbEnableFrontCam = true;
    private boolean tbEnableTorch = false;
    private int mSelectedBeauty = 4;//磨皮+皮肤美白
    private int mSelectedFilter = 9;//浪漫
    protected String mRoomID = null;
    private String mPublishTitle = "Tiger_";
//    protected boolean mIsPublishing = false;
    protected String mPublishStreamID = "Tiger_";
    protected boolean mEnableCamera = true;//开启摄像头
    protected boolean mEnableFrontCam = true;//开启前置摄像头
    protected boolean mEnableMic = true;//开启麦克风
    protected boolean mEnableTorch = false;//开启手电筒
    protected boolean mEnableBackgroundMusic = false;//开启伴奏
    protected boolean mEnableLoopback = false;//采集监听
    protected InputStream mIsBackgroundMusic = null;//伴奏文件流
    protected BottomSheetBehavior mBehavior = null;

    private String mliveStreamingURL = null;
    private String mVideoResolution = null;
    private boolean mUseFilter = false;
    private String coverUrl, titleText;
    private int markMode = 1; //0关闭，1静态，2动态
    private long pushBit;
    private int pushFPS = 15;
    private boolean pushHard;
    private int videoQuality;
    private boolean mIsPublishing;
//    private String[] mArrLiveQuality;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        appManager.addActivity(this);
        initZego();
        getParentParame();
        initView();
        requestLivePermission();
        configImagePicker();
        initPhoneCallingListener();
        giftMessageQueue = new GiftMessageQueue(this);
    }

    private void initZego() {

        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
//        mArrLiveQuality = getResources().getStringArray(R.array.live_quality);
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        // 设置app朝向
        int currentOrientation = getWindowManager().getDefaultDisplay().getRotation();
        mZegoLiveRoom.setAppOrientation(currentOrientation);
        // 设置推流配置
        ZegoAvConfig zegoAvConfig = new ZegoAvConfig(videoQuality);
        zegoAvConfig.setVideoFPS(pushFPS);
        int videoWidth = zegoAvConfig.getVideoEncodeResolutionWidth();
        int videoHeight = zegoAvConfig.getVideoEncodeResolutionHeight();
        if (((currentOrientation == Surface.ROTATION_0 || currentOrientation == Surface.ROTATION_180) && videoWidth > videoHeight) ||
                ((currentOrientation == Surface.ROTATION_90 || currentOrientation == Surface.ROTATION_270) && videoHeight > videoWidth)) {
            zegoAvConfig.setVideoEncodeResolution(videoHeight, videoWidth);
            zegoAvConfig.setVideoCaptureResolution(videoHeight, videoWidth);
        }
        if (zegoAvConfig != null) {
            ZegoApiManager.getInstance().setZegoConfig(zegoAvConfig);
        }

        // 设置水印
        mZegoLiveRoom.setWaterMarkImagePath("assets:tiger.png");
        Rect rect = new Rect();
        rect.left = 30;
        rect.top = 10;
        rect.right = 180;
        rect.bottom = 160;
        mZegoLiveRoom.setPreviewWaterMarkRect(rect);
        mZegoLiveRoom.setPublishWaterMarkRect(rect);
        textureView.setVisibility(View.VISIBLE);
        mZegoLiveRoom.setPreviewView(textureView);
        mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
        mZegoLiveRoom.startPreview();

        mZegoLiveRoom.setFrontCam(tbEnableFrontCam);
        mZegoLiveRoom.enableTorch(tbEnableTorch);
        // 设置美颜
//        mZegoLiveRoom.enableBeautifying(ZegoRoomUtil.getZegoBeauty(mSelectedBeauty));
        // 设置滤镜
        mZegoLiveRoom.setFilter(mSelectedFilter);
    }

    /**
     * 登入ZEGO
     */
    private void loginZEGO() {
        //登录
        mZegoLiveRoom.loginRoom(mRoomID, mPublishTitle, ZegoConstants.RoomRole.Anchor, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errorCode, ZegoStreamInfo[] zegoStreamInfos) {
                if(errorCode == 0){
                    handleAnchorLoginRoomSuccess(zegoStreamInfos);
                }else {
                    TSBErrorLongtimeShow("开播失败，请尝试退出应用重新进入！");
                    LogUtil.d("handleAnchorLoginRoomFail  : onLoginRoom fail(" + mRoomID + "), errorCode:" + errorCode);
                }
            }
        });

        //推流状态更新
        mZegoLiveRoom.setZegoLivePublisherCallback(new IZegoLivePublisherCallback() {
            @Override
            public void onPublishStateUpdate(int stateCode, String streamID, HashMap<String, Object> streamInfo) {
                if (stateCode == 0) {
                    handlePublishSucc(streamID, streamInfo);
                } else {
                    TSBErrorLongtimeShow("开播失败，请尝试退重新开播或联系官方人员！");
                    LogUtil.d("onPublishStateUpdate failed : " + stateCode);
                }
            }

            @Override
            public void onJoinLiveRequest(final int seq, String fromUserID, String fromUserName, String roomID) {
            }

            @Override
            public void onPublishQualityUpdate(String streamID, ZegoStreamQuality streamQuality) {
                // 推流质量回调
//                netQualityImg.setText(getResources().getString(R.string.live_quality, mArrLiveQuality[streamQuality.quality]) + " ");
                netQualityImg.append(getResources().getString(R.string.live_quality_fps_and_bitrate, streamQuality.videoFPS, streamQuality.videoBitrate));
            }

            @Override
            public AuxData onAuxCallback(int dataLen) {
                return handleAuxCallback(dataLen);
            }

            @Override
            public void onCaptureVideoSizeChangedTo(int width, int height) {
                LogUtil.d("onCaptureVideoSizeChangedTo    width: " + width + "   height : " + height);
            }

            @Override
            public void onMixStreamConfigUpdate(int errorCode, String streamID, HashMap<String, Object> streamInfo) {

            }
        });

        //用户流状态
        mZegoLiveRoom.setZegoRoomCallback(new IZegoRoomCallback() {
            @Override
            public void onKickOut(int reason, String roomID) {

            }

            @Override
            public void onDisconnect(int errorCode, String roomID) {
                handleDisconnect(errorCode, roomID);
            }

            @Override
            public void onStreamUpdated(final int type, final ZegoStreamInfo[] listStream, final String roomID) {

            }

            @Override
            public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {

            }

            @Override
            public void onRecvCustomCommand(String userID, String userName, String content, String roomID) {

            }
        });

        mZegoLiveRoom.setZegoAudioPrepCallback2(new IZegoAudioPrepCallback2() {
            @Override
            public ZegoAudioFrame onAudioPrep(ZegoAudioFrame zegoAudioFrame) {
                return null;
            }
        });
    }

    //推流成功.
    protected void handlePublishSucc(String streamID, HashMap<String, Object> info) {
        mIsPublishing = true;
        List<String> listUrls = getShareUrlList(info);
        // 将流地址发送给房间观众
        Map<String, String> mapUrls = new HashMap<>();
        mapUrls.put(Constants.FIRST_ANCHOR, String.valueOf(true));
        mapUrls.put(Constants.KEY_HLS, listUrls.get(0));
        mapUrls.put(Constants.KEY_RTMP, listUrls.get(1));
        Gson gson = new Gson();
        String json = gson.toJson(mapUrls);
        mZegoLiveRoom.updateStreamExtraInfo(json);
        LogUtil.d("PUSHUrl : " + json);
    }

    //获取流地址.
    protected List<String> getShareUrlList(HashMap<String, Object> info){
        List<String> listUrls = new ArrayList<>();

        if(info != null){
            String[] hlsList = (String[]) info.get(ZegoConstants.StreamKey.HLS_URL_LST);
            if (hlsList != null && hlsList.length > 0) {
                listUrls.add(hlsList[0]);
            }

            String[] rtmpList = (String[]) info.get(ZegoConstants.StreamKey.RTMP_URL_LIST);
            if (rtmpList != null && rtmpList.length > 0) {
                listUrls.add(rtmpList[0]);
            }
        }
        return listUrls;
    }

    //用户掉线.
    protected void handleDisconnect(int errorCode, String roomID){
        LogUtil.d("handleDisconnect  : onDisconnected, roomID:" + roomID + ", errorCode:" + errorCode);
    }

    //推流
    protected void publishStream() {
        if (TextUtils.isEmpty(mPublishStreamID)) {
            return;
        }
        // 输出发布状态
        LogUtil.d("输出发布状态  : start publishing(" + mPublishStreamID + ")");
        // 设置水印
        mZegoLiveRoom.setWaterMarkImagePath("assets:tiger.png");
        Rect rect = new Rect();
        rect.left = 50;
        rect.top = 20;
        rect.right = 200;
        rect.bottom = 170;
        mZegoLiveRoom.setPreviewWaterMarkRect(rect);
        mZegoLiveRoom.setPublishWaterMarkRect(rect);

        // 开启流量自动控制
        int properties = ZegoConstants.ZegoTrafficControlProperty.ZEGOAPI_TRAFFIC_FPS
                | ZegoConstants.ZegoTrafficControlProperty.ZEGOAPI_TRAFFIC_RESOLUTION;
        mZegoLiveRoom.enableTrafficControl(properties, true);

        // 开始推流
//        mZegoLiveRoom.enableLoopback(true);
        mZegoLiveRoom.setPreviewView(textureView);
        mZegoLiveRoom.startPreview();
        mZegoLiveRoom.enableMic(mEnableMic);
        mZegoLiveRoom.enableCamera(mEnableCamera);
        mZegoLiveRoom.startPublishing(mPublishStreamID, mPublishTitle, mPublishFlag);
        LogUtil.d(mPublishStreamID + "\n" + mPublishTitle + "\n" + mPublishFlag);
//        mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
    }

    /**
     * 开始发布直播.
     */
    protected void startPublish() {
        // 6.0及以上的系统需要在运行时申请CAMERA RECORD_AUDIO权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 101);
            } else {
                publishStream();
            }
        } else {
            publishStream();
        }
    }

    //主播登录房间成功.
    protected void handleAnchorLoginRoomSuccess(ZegoStreamInfo[] zegoStreamInfos) {
        // 开始推流
        startPublish();
        masterEnterRoom();
        // 打印log
        LogUtil.d("handleAnchorLoginRoomSuccess : onLoginRoom success(" + mRoomID + "), streamCounts:" + zegoStreamInfos.length);
    }

    protected AuxData handleAuxCallback(int dataLen) {
        // 开启伴奏后, sdk每20毫秒一次取数据
        if (!mEnableBackgroundMusic || dataLen <= 0) {
            return null;
        }

        AuxData auxData = new AuxData();
        auxData.dataBuf = new byte[dataLen];

        try {
            LogUtil.d("bgm is playing !");
//            mIsBackgroundMusic = new URL(bgmList.get(playIndex).getUrl()).openStream();
            mIsBackgroundMusic = new FileInputStream(bgmList.get(playIndex).getUrl());
            int len = mIsBackgroundMusic.read(auxData.dataBuf);
            if (len <= 0) {
                // 歌曲播放完毕
                mIsBackgroundMusic.close();
                mIsBackgroundMusic = null;
                mEnableBackgroundMusic = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mEnableBackgroundMusic = false;
            LogUtil.e("bgm io error !");
        }

        auxData.channelCount = 2;
        auxData.sampleRate = 44100;

        return auxData;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (backEnabled)
        closeAct();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == PICTURE_REQUEST_CODE) {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            Glide.with(TigerApplication.getInstance().getApplicationContext()).load(images.get(1).path)
                    .placeholder(coverDraw)
                    .error(coverDraw)
                    .into(coverImage);
            hasCoverImg = true;
            isChangeCoverImg = true;
        }
    }

    /**
     * 更新聊天室信息
     */
    private void doUpdateRoomInfo() {
        ChatRoomUpdateInfo chatRoomUpdateInfo = new ChatRoomUpdateInfo();
        Map<String, Object> map = new HashMap<>(1);
        map.put(PushLinkConstant.type, -1);
        chatRoomUpdateInfo.setExtension(map);
        NIMClient.getService(ChatRoomService.class)
                .updateRoomInfo(LiveRoomCache.getRoomId(), chatRoomUpdateInfo, true, map)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LogUtil.i("leave room, update room info success");
                    }

                    @Override
                    public void onFailed(int i) {
                        LogUtil.i("leave room, update room info failed, code:" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    /**
     * LoginOutRoom
     */
    private void doCompletelyFinish() {
        mIsPublishing = false;
//        doUpdateRoomInfo();
        closeLive();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //....
                clean();
            }
        }, 50);
    }

    /**
     * 确认关闭直播PopWindow
     */
    private void closeAvPopWindow() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_closelive, null);

        TextView btnCamera = (TextView) popView.findViewById(R.id.tv_yes);
        TextView btnCancel = (TextView) popView.findViewById(R.id.tv_no);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白消失

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_yes:
                        onTakeSnapshotResult();
                        break;
                    case R.id.tv_no:

                        break;
                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected void stopPublish() {
        if (mIsPublishing) {
//            mZegoLiveRoom.enableLoopback(false);
            mZegoLiveRoom.stopPreview();
            mZegoLiveRoom.stopPublishing();
            mZegoLiveRoom.setPreviewView(null);
        }
    }

    protected void stopAllStream() {
        stopPublish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        LogUtil.d("授权成功");
        isPermissionGrant = true;
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPreview();
            }
        }, 50);
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        TSBError(tip);
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }
        TSBError(sb.toString());
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(View view) {
        if (isAt) isAt = false;
        if (isBarrageOpen){
            isBarrageOpen = false;
            braagerImg.setImageResource(R.drawable.barrage_close);
            isBarrageOpen = false;
        }
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 伴奏操作界面
     */
    private void openBGMOperate(BGMEnum type, String musicName, String musicSingler){//伴奏操作界面
        MusicOperateDialogFragment fragment = new MusicOperateDialogFragment(audio, type, musicName, musicSingler, new MusicOperateDialogFragment.BGMCallback() {
            @Override
            public void onDismiss() {
                bgmImg.setEnabled(true);
            }

            @Override
            public void musicList() {
                openMusicList();
            }

            @Override
            public void audio(int au) {
                audio = au;
            }

            @Override
            public void type(BGMEnum newType) {
                bgmType = newType;
            }
        });
        fragment.show(getSupportFragmentManager(),MusicOperateDialogFragment.class.getSimpleName());
    }

    /**
     * 伴奏选择界面
     */
    private void openMusicList(){
        MusicListDialogFragment.getInstance().show(getSupportFragmentManager(),MusicListDialogFragment.class.getSimpleName());
//        MusicListDialogFragment fragment = new MusicListDialogFragment(playIndex, new MusicListDialogFragment.OperateListener() {
//            @Override
//            public void onChose(List<Mp3Info> mp3Infos , int index) {
//                if (bgmList.size() != 0) bgmList.clear();
//                bgmList.addAll(mp3Infos);
//                playIndex = index;
//                bgmType = BGMEnum.PLAY;
//                musicName = mp3Infos.get(index).getTitle();
//                musicSingler = mp3Infos.get(index).getArtist();
//
//                if (mIsBackgroundMusic != null) {
//                    try {
//                        mIsBackgroundMusic.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    mIsBackgroundMusic = null;
//                }
//                mEnableBackgroundMusic = true;
//                mZegoLiveRoom.enableAux(true);
//
//            }
//
//            @Override
//            public void onDismiss() {
//                bgmImg.setEnabled(false);
//                openBGMOperate(bgmType, musicName, musicSingler);
//            }
//        });
//        fragment.show(getSupportFragmentManager(),MusicListDialogFragment.class.getSimpleName());
    }

    /**
     * 结束直播时截图
     */
    public void onTakeSnapshotResult() {//截图结果回调
        exitChannel();
        stopAllStream();
        LogUtil.d("onTakeSnapshotResult！");
        doCompletelyFinish();
        closeLayout.setVisibility(View.VISIBLE);
        Bitmap myBitmap = EasyBlur.with(getApplicationContext())
                    .bitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_room_change)) //要模糊的图片
                    .radius(8)//模糊半径
                    .blur();
        bg_close.setImageBitmap(myBitmap);
        closeTime = System.currentTimeMillis();
        LogUtil.d("start time >>> " + startTime);
        LogUtil.d("close time >>> " + closeTime);
        watchCount.setText(String.valueOf(watchTimes) + "人");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        livingTime.setText(simpleDateFormat.format(closeTime - startTime - 8*60*60*1000));
    }

    @Override
    public void refreshSofa() {
        getSofaListData();
    }

    /**
     * 举报
     * @param taAccId
     */
    private void reportPopWindow(final String taAccId) {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_report, null);

        TextView report_tv_1 = (TextView) popView.findViewById(R.id.report_tv_1);
        TextView report_tv_2 = (TextView) popView.findViewById(R.id.report_tv_2);
        TextView report_tv_3 = (TextView) popView.findViewById(R.id.report_tv_3);
        TextView report_tv_4 = (TextView) popView.findViewById(R.id.report_tv_4);
        TextView report_tv_5 = (TextView) popView.findViewById(R.id.report_tv_5);
        TextView report_tv_6 = (TextView) popView.findViewById(R.id.report_tv_6);
        TextView report_tv_cancel = (TextView) popView.findViewById(R.id.report_tv_cancel);
        View close = popView.findViewById(R.id.pop_close);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.report_tv_1:
                        report(taAccId, "政治敏感");
                        break;
                    case R.id.report_tv_2:
                        report(taAccId, "色情低俗");
                        break;
                    case R.id.report_tv_3:
                        report(taAccId, "广告骚扰");
                        break;
                    case R.id.report_tv_4:
                        report(taAccId, "人身攻击");
                        break;
                    case R.id.report_tv_5:
                        report(taAccId, "声音违规");
                        break;
                    case R.id.report_tv_6:
                        report(taAccId, "其他");
                        break;
                    case R.id.report_tv_cancel:
                    case R.id.pop_close:
                        break;
                    default:
                        break;
                }
                popWindow.dismiss();
            }
        };
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        report_tv_1.setOnClickListener(listener);
        report_tv_2.setOnClickListener(listener);
        report_tv_3.setOnClickListener(listener);
        report_tv_4.setOnClickListener(listener);
        report_tv_5.setOnClickListener(listener);
        report_tv_6.setOnClickListener(listener);
        report_tv_cancel.setOnClickListener(listener);
        close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 充值PopWindow
     */
    private void rechargePopWindow() {
        View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.channel_recharge, null);

        TextView recharge_pop_wallet = (TextView) popView.findViewById(R.id.recharge_pop_wallet);
        recharge_pop_wallet.setText("余额：" + wallet + "虎币");

        TextView tv_login = (TextView) popView.findViewById(R.id.tv_recharge);
        View login_close = popView.findViewById(R.id.login_close);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白消失
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_recharge:
                        Intent intent = new Intent(ZegoAnchorActivity.this, Recharge.class);
                        intent.putExtra("Channel", "Channel");
                        startActivity(intent);
                        break;
                    case R.id.login_close:

                        break;

                    default:
                        break;
                }
                popWindow.dismiss();
            }
        };
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        tv_login.setOnClickListener(listener);
        login_close.setOnClickListener(listener);

        popWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
