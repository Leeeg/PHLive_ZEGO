package com.i5i58.live.home.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.attachment.SingleChatAttachment;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.animation.AnimationUtil;
import com.i5i58.live.common.utils.countDown.CountDownCallback;
import com.i5i58.live.common.utils.countDown.LCountDownTimer;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoad;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoadCallback;
import com.i5i58.live.common.utils.imgUtil.BitmapUtil;
import com.i5i58.live.common.utils.json.GsonUtil;
import com.i5i58.live.common.utils.layoutManager.ScrollSpeedLinearLayoutManger;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.barrage.BarrageView;
import com.i5i58.live.common.view.button.MyButton;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.common.view.recyclerView.MyRecyclerView;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.emoji.EmoticonPickerView;
import com.i5i58.live.emoji.IEmoticonSelectedListener;
import com.i5i58.live.emoji.InputTypeEnum;
import com.i5i58.live.home.activity.ContributeActivity;
import com.i5i58.live.home.activity.MainAudienceActivity;
import com.i5i58.live.home.activity.MicActivity;
import com.i5i58.live.home.adapters.ChatRoomMessageAdapter;
import com.i5i58.live.home.adapters.SofaListAdapter;
import com.i5i58.live.home.dialogFragment.BroadsideDialogFragment;
import com.i5i58.live.home.dialogFragment.GiftDialogFragment;
import com.i5i58.live.home.dialogFragment.HeartDialogFragment;
import com.i5i58.live.home.dialogFragment.PersonalDialogFragment;
import com.i5i58.live.home.dialogFragment.SingleChatDialogFragment;
import com.i5i58.live.home.dialogFragment.SingleChatInfoDialogFragment;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.MessageCallBack;
import com.i5i58.live.home.helper.channelGift.GiftHelper;
import com.i5i58.live.home.helper.channelGift.GiftMsg;
import com.i5i58.live.home.helper.channelGift.MessageThread;
import com.i5i58.live.home.helper.periscope.PeriscopeLayout;
import com.i5i58.live.main.activity.WelcomeActivity;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.account.SofaAccount;
import com.i5i58.live.model.entity.barrage.BarrageEntity;
import com.i5i58.live.model.entity.barrage.BarrageParams;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;
import com.i5i58.live.recharge.Recharge;
import com.i5i58.live.webSocket.receive.Channel;
import com.i5i58.live.webSocket.receive.Owner;
import com.i5i58.live.webSocket.receive.Self;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMemberUpdate;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
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

import org.dync.giftlibrary.widget.GiftControl;
import org.dync.giftlibrary.widget.GiftFrameLayout;
import org.dync.giftlibrary.widget.GiftModel;
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
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lee on 2017/4/17.
 */

@ContentView(R.layout.audience_operate)
public class AudienceOperateFragment extends BaseFragment implements MessageCallBack, GiftDialogFragment.GiftCallback,
        CountDownCallback, GestureDetector.OnGestureListener, View.OnTouchListener, IEmoticonSelectedListener, ViewTreeObserver.OnGlobalLayoutListener {

//    private WebSocket webSocket;
    private StringBuilder giftMsg;
    private String gCount, gId;
    private volatile int gCtis = 1;
    private List<SofaAccount> sofaAccountList = new ArrayList<>();
    private SofaListAdapter sofaListAdapter;
    private List<ChatRoomMessage> messageList = new ArrayList();
    private ChatRoomMessageAdapter messageAdapter;
    private InputMethodManager imm;
    private long onlinePeople;
    private Timer timer;
    private Map<String, Object> updateMap = new HashMap();

    private String egretRoot;
    private final String EGRET_ROOT = "egret";
    private String gameId;
    private EgretGameEngine gameEngine;
    private boolean engineInited = false;
    //若bUsingPlugin为true，开启插件
    private static boolean bUsingPlugin = false;
    private String loaderUrl;
    private String updateUrl;
    private String wallet = "0";

    private final String GIVEHEART = "giveHeart";
    private final String FOLLWOANCHOR = "followAnchor";
    private final String OPENCLUB = "openClub";
    private final String GIFT = "gift";
    private final String OPENGUARD = "openGuard";
    private final String ENTER = "enter";
    private final String CONNMIC = "connMic";
    private final String DRIFTCOMMENT = "driftComment";

    private ChannelData channel;
    private boolean isAttention;
    private String atTarget;
    private String atAccId;
    private boolean isAt = false;
    private boolean isBarrageOpen = false;
    private InputTypeEnum inputType = InputTypeEnum.KEYBOARD;
    private LinearLayout.LayoutParams lp;
    public static final String action = "com.audienceoperatefragment.attention";
    private boolean webSocketLost = false;
    private boolean isGift = false;
    private boolean firstComing = true;
    private int navigationBar;
    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    public boolean isShowOperate = true;
    private CleanOperateCallback cleanCallback;
    private MainAudienceActivity parent;

    BroadcastReceiver attentionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("attentionBroadcastReceive  >>>  " + intent.getBooleanExtra("isAttention", false));
            if (intent.getBooleanExtra("isAttention", false)){
                attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_yes));
                isAttention = true;
            }else {
                attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_no));
                isAttention = false;
            }
        }
    };

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

    @Override
    public void onGlobalLayout() {
    }


    public interface CleanOperateCallback{
        void openPlay();
        void closeVideo();
        void cleanOperate();
        void stopLode();
    }

    // 定义手势检测器实例
    private GestureDetector detector;
    private int FLING_MIN_DISTANCE = 180;
    private boolean isBroadside = false;

    private GiftModel giftModel;
    private GiftControl giftControl;

    private LCountDownTimer mLCountDownTimer = null;

    //分享
    private ShareAction mShareAction;
    private ShareBoardConfig config;

    private FragmentManager fragmentManager;

    public AudienceOperateFragment() {
    }

    public AudienceOperateFragment(CleanOperateCallback cleanCallback, ChannelData channel) {
        this.channel = channel;
        this.cleanCallback = cleanCallback;
    }

    @ViewInject(R.id.input)
    private LinearLayout input;

    @ViewInject(R.id.root)
    private RelativeLayout root;

    @ViewInject(R.id.emoji_buttonSingle)
    private ImageView emojiImg;

    @ViewInject(R.id.emoticon_picker_view)
    private EmoticonPickerView pickerView;

    @ViewInject(R.id.operate_gift_button)
    private MyButton giftButton;

    @ViewInject(R.id.gift_layout1)
    private GiftFrameLayout giftFrameLayout1;

    @ViewInject(R.id.gift_layout2)
    private GiftFrameLayout giftFrameLayout2;

    @ViewInject(R.id.periscope)
    private PeriscopeLayout periscope;

    @ViewInject(R.id.view_heart)
    private View view_heart;

    @ViewInject(R.id.containerView)
    private BarrageView containerView;

    @ViewInject(R.id.editTextSingleMessage)
    private EditText messageEdit;

    @ViewInject(R.id.layout1)
    private RelativeLayout layout1;

    @ViewInject(R.id.textroomleMessageLayout)
    public LinearLayout messageLayout;

    @ViewInject(R.id.operate_root)
    public LinearLayout rootOperateLayout;

    @ViewInject(R.id.audience_operate_top_person)
    private LinearLayout personLayout;

    @ViewInject(R.id.operate_listview_message)
    private RecyclerView messageRecyclerView;

    @ViewInject(R.id.frg_operate_fl_bottom)
    public RelativeLayout bottomLl;

    @ViewInject(R.id.frg_operate_fl_top)
    private LinearLayout topLl;

    @ViewInject(R.id.audience_operate_top_contribution)
    private LinearLayout contributionLayout;

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

    @ViewInject(R.id.message_recycle_scrotobottom)
    private ImageView scrollToBottom;

    @ViewInject(R.id.operate_im_attention)
    private ImageView attentionImg;

    @ViewInject(R.id.liveroom_bottom_iv_keyboard)
    private ImageView talkImg;

    @ViewInject(R.id.iv_sliveroom_bottom_iv_share)
    private ImageView shareImg;

    @ViewInject(R.id.power_braager)
    private ImageView braagerImg;

    @ViewInject(R.id.iv_sliveroom_bottom_iv_single)
    private ImageView singleImg;

    @ViewInject(R.id.dfrg_gift_gift)
    private ImageView gift;

    @ViewInject(R.id.audience_operate_top_sofa)
    private MyRecyclerView sofaRecyclerView ;

    @Event(R.id.emoji_buttonSingle)
    private void onEmojiClick(View view){
        if (inputType == InputTypeEnum.KEYBOARD){
            inputType = InputTypeEnum.EMOJI;
            imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            pickerView.setVisibility(View.VISIBLE);
            pickerView.show(this);
            emojiImg.setImageResource(R.drawable.icon_input_keyboard);
        }else {
            inputType = InputTypeEnum.KEYBOARD;
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            pickerView.setVisibility(View.GONE);
            messageEdit.setFocusable(true);
            messageEdit.setFocusableInTouchMode(true);
            messageEdit.requestFocus();
            emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
        }

    }

    @Event(R.id.power_braager)
    private void onBraagerImgClick(View view){
        if (isBarrageOpen){
            braagerImg.setImageResource(R.drawable.barrage_close);
                messageEdit.setHint("说点什么吧");
//            }
            isBarrageOpen = false;
        }else {
            braagerImg.setImageResource(R.drawable.barrage_open);
                messageEdit.setHint("发送弹幕,0.1虎币/条");
//            }
            isBarrageOpen = true;
        }
    }

    @Event(R.id.audience_operate_top_exit)
    private void exitClick(View v){
        clean();
        closeAct(parent);
    }

    @Event(R.id.iv_sliveroom_bottom_iv_mic)
    private void micClick(View v){
        TSBSuccess("暂无权限！");
    }

    @Event(R.id.dfrg_gift_gift)
    private void giftClick(View v){
        GiftDialogFragment fragment = new GiftDialogFragment(this);
        fragment.show(fragmentManager,GiftDialogFragment.class.getSimpleName());
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
                if (isFollow) {
                    attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_yes));
                    isAttention = true;
                }else {
                    attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_no));
                    isAttention = false;
                }
            }

            @Override
            public void singleChat(String name, String iconUrl, String accId) {
                SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                fragment.show(fragmentManager,SingleChatInfoDialogFragment.class.getSimpleName());
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
                    ImageSpan imageSpan = new ImageSpan(parent, atBitmap);
                    SpannableString spannableString = new SpannableString(at);
                    spannableString.setSpan(imageSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    messageEdit.setText(spannableString + " ");
                    messageEdit.setSelection(spannableString.length() + 1);
                }
            }
        });
        fragment.show(fragmentManager,PersonalDialogFragment.class.getSimpleName());
    }

    @Event(R.id.liveroom_bottom_iv_keyboard)
    private void talkClick(View view){
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        messageEdit.setFocusable(true);
        messageEdit.setFocusableInTouchMode(true);
        messageEdit.requestFocus();
    }

    @Event(R.id.iv_sliveroom_bottom_iv_share)
    private void shareClick(View v){
        mShareAction.open(config);
    }

    @Event(R.id.iv_sliveroom_bottom_iv_single)
    private void singleClick(View v){
        SingleChatDialogFragment fragment = new SingleChatDialogFragment();
        fragment.show(fragmentManager,SingleChatDialogFragment.class.getSimpleName());
        singleImg.setImageResource(R.drawable.bt_operate_bottom_singlechat);
    }

    @Event(R.id.audience_operate_top_contribution)
    private void intentToContribute(View v){
        Intent intent = new Intent(parent, ContributeActivity.class);
        intentAct(parent, intent);
    }

    @Event(R.id.icon_mac)
    private void intentToMic(View v){
        Intent intent = new Intent(parent, MicActivity.class);
        intentAct(parent, intent);
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
                .addParam("cId",        channel.getcId())
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
            if (msg.length() > 50){
                msg = msg.subSequence(0, 50);
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
                String content = "{\"source\":\"" + UserPreferences.getAccId() + "\",\"type\":\"text\",\"contents\":\"" + msg.toString() + "\"}";
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
                String content = "{\"source\":\"" + UserPreferences.getAccId() + "\",\"type\":\"at\",\"contents\":\"" + msg.toString() + "\",\"target\":\"" + atAccId + "\"}";
                sendBarrageMsg(content);
//                BarrageParams params = new BarrageParams();
//                params.setContent(content);
//                BarrageEntity be = new BarrageEntity();
//                be.setCmd("driftComment");
//                be.setParams(params);
//                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
//                        .addRouteUrl(API.DRIFTCOMMENT)
//                        .addParam("device",     SystemCache.DEVICE)
//                        .addParam("cId",        channel.getcId())
//                        .addParam("content",    content)
//                        .getResult(new HttpCallback() {
//                            @Override
//                            @HttpResultCut
//                            public void success(JSONObject js, boolean success) {
//                                try {
//                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
//                                        LogUtil.d("sendGift success !");
//                                    }else {
//                                        LogUtil.d("sendGift error : " + js);
//                                        String msg = js.getString("msg");
//                                        String code = js.getString("code");
//                                        if (code.equals("igold_not_enough")){
//                                            imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                            rechargePopWindow();
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
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
            Toast.makeText(parent, "请输入文字", Toast.LENGTH_SHORT).show();
        }
    }

    @Event(R.id.operate_im_attention)
    private void attentionClick(View v){
        if (isAttention){//已关注   粉丝团
            Intent intent = new Intent(parent, MyReactActivity.class);
            intent.putExtra("accId", UserPreferences.getAccId());
            intent.putExtra("token", UserPreferences.getToken());
            intent.putExtra("cId", channel.getCId());
            MyReactActivity.mainName = "FansIndex";
            startActivity(intent);
        }else {//未关注
            if (channel.getOwnerId().equals(UserPreferences.getAccId())) {
                TSBError("无法关注自己！");
                return;
            }
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.FOLLOWMANAGER)
                    .addParam("attAccId", channel.getOwnerId())
                    .addParam("cId", channel.getCId())
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                    if (js.getString("code").equals("success")) {
                                        attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_yes));
                                        isAttention = true;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Event(value = {R.id.view_close, R.id.view_close2}, type = View.OnTouchListener.class)
    private boolean closeInput(View view, MotionEvent motionEvent){
        bottomLl.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
        messageEdit.setText("");
        pickerView.setVisibility(View.GONE);
        if (inputType == InputTypeEnum.EMOJI){
            inputType = InputTypeEnum.KEYBOARD;
            emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
        }
        imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return false;
    }

    @Event(R.id.operate_gift_button)
    private void giftSendClick(View v){
        startTimer();
        gCtis++;
        GiftMsg giftMsg = new GiftMsg();
        giftMsg.setcId(channel.getcId());
        giftMsg.setGiftId(gId);
        giftMsg.setGiftCount(gCount);
        giftMsg.setContinuous(gCtis);

        parent.getGiftMessageQueue().addGift(giftMsg);
//        GiftHelper.postTask(new MessageThread(giftMsg, new MessageThread.MessageThreadCallback() {
//            @Override
//            public void recharge(String msg) {
//                rechargePopWindow();
//            }
//
//            @Override
//            public void guard(String msg) {
//                Intent intent = new Intent(parent, MyReactActivity.class);
//                intent.putExtra("accId", UserPreferences.getAccId());
//                intent.putExtra("token", UserPreferences.getToken());
//                intent.putExtra("cId", LiveRoomCache.getChannelId());
//                MyReactActivity.mainName = "GuardIndex";
//                startActivity(intent);
//            }
//        }));
        
//        messageThread.addGift(giftMsg);
//        if (null != webSocket){
//            gCtis++;
//            giftMsg = new StringBuilder("{\"cmd\":\"gift\",\"params\":{\"id\":\"" + gId + "\",\"ct\":\"" + gCount + "\",\"ctis\":\"" + gCtis + "\"}}");
//            webSocket.send(giftMsg.toString());
//            LogUtil.d(giftMsg.toString());
//        }

    }

    public void onVideoSizeChanged(boolean isHoriz){
        if (null == messageRecyclerView) return;
        if (isHoriz){
            ViewGroup.LayoutParams lp = messageRecyclerView.getLayoutParams();
            lp.height = messageRecyclerView.getHeight() * 2;
            messageRecyclerView.setLayoutParams(lp);
        }
    }

    private void startTimer(){
        try {
            mLCountDownTimer.startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        imm.showSoftInput(editTextMessage, 0);
    }

    /**
     * 清理缓存
     */
    public void clean(){
        try {
            exitChannel();
            cleanCallback.closeVideo();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                root.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
            } else {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
            firstComing = true;
            clearEgret();
            topLl.setVisibility(View.GONE);
            bottomLl.setVisibility(View.GONE);
            messageRecyclerView.setVisibility(View.GONE);
//            webSocket.close();
            NIMClient.getService(ChatRoomService.class).exitChatRoom(channel.getCId());
            registerObservers(false);
            LiveRoomCache.clearLiveRoomCache();
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }
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
//                loaderUrl = "http://gg78live.oss-cn-hangzhou.aliyuncs.com/Animation/egrettest/game_code_egrettest.zip";
//                updateUrl = "http://gg78live.oss-cn-hangzhou.aliyuncs.com/Animation/egrettest";
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

    /**
     * 右下角飄愛心效果=====================================================================================
     */
//    private void initHeart() {
//        pList = new ArrayList<>();
//        pList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_praise_sm1, null))
//                .getBitmap());
//        pList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_praise_sm2, null))
//                .getBitmap());
//        pList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_praise_sm3, null))
//                .getBitmap());
//        pList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_praise_sm4, null))
//                .getBitmap());
//
//        divergeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIndex == 3) {
//                    mIndex = 0;
//                }
//                divergeView.startDiverges(mIndex);
//                mIndex++;
//            }
//        });
//        divergeView.post(new Runnable() {
//            @Override
//            public void run() {
//                divergeView.setEndPoint(new PointF(divergeView.getMeasuredWidth() / 2, 0));
//                divergeView.setDivergeViewProvider(new Provider());
//            }
//        });
//    }
//
//    class Provider implements DivergeView.DivergeViewProvider {
//
//        @Override
//        public Bitmap getBitmap(Object obj) {
//            return pList == null ? null : pList.get((int) obj);
//        }
//    }

    /**
     * 举报
     * @param reason
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

    private void reportPopWindow(final String taAccId) {
        View parentView = ((ViewGroup) parent.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(parent, R.layout.pop_report, null);

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

        WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
        lp.alpha = 0.4f;
        parent.getWindow().setAttributes(lp);

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
                WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
                lp.alpha = 1f;
                parent.getWindow().setAttributes(lp);
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

        popWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 沙发列表数据刷新
     */
    public void getSofaListData() {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.SOFALIST)
                .addParam("cId", channel.getCId())
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
     * 实例化沙发列表
     */
    private void initSofaList() {
        sofaListAdapter = new SofaListAdapter(parent, sofaAccountList);
        sofaRecyclerView.setAdapter(sofaListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(parent);
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
                        fragment.show(fragmentManager,SingleChatInfoDialogFragment.class.getSimpleName());
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
                            ImageSpan imageSpan = new ImageSpan(parent, atBitmap);
                            SpannableString spannableString = new SpannableString(at);
                            spannableString.setSpan(imageSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            messageEdit.setText(spannableString);
                            messageEdit.setSelection(spannableString.length());
                        }
                    }
                });
                fragment.show(fragmentManager,PersonalDialogFragment.class.getSimpleName());
            }
        });
        getSofaListData();
    }

    /**
     * 获取关注状态
     * @param ownerId
     */
    private void getFollowStatus(String ownerId){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETFOLLOWSTATUS)
                .addParam("target", ownerId)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.getString("code").equals("success")) {
                                    if (js.getString("data").equals("1") || js.getString("data").equals("3")) {
                                        attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_yes));
                                        isAttention = true;
                                    }else {
                                        attentionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_operate_attention_no));
                                        isAttention = false;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.ChatRoom
                && message.getSessionId() != null
                && message.getSessionId().equals(channel.getYunXinRId());
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

    private void showMessage(String cmd, Map<?, ?> data) {
        try {
            LogUtil.d("ChatRoomMessage CMD: " + cmd);
            switch (cmd){
                case ENTER:
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
                    //在线人数
                    onlinePeople++;
                    onlineTxt.setText(onlinePeople + "人次");
                    break;
                case GIFT:
                    //显示礼物动画
                    String giftId = data.get("id").toString();
                    GiftConfig giftConfig = LiveRoomCache.getGiftConfigMap().get(Integer.valueOf(giftId));
                    if (LiveRoomCache.isGiftOpen()){
                        String function = giftConfig.getFunction();
                        if (null != function && !"".equals(function) && null != data.get("condition") && data.get("condition").toString().equals("true")) {
                            LogUtil.d("giftId:" + giftId);
                            gameEngine.callEgretInterface("playGift", giftId + "|" + SystemCache.getScreenWith() + "|" + SystemCache.getScreenHeight());
                        }
                    }
                    String giftUrl = API.OSS_URL_GIFT + giftConfig.getPath() + ".png";
                    String nickName = data.get("name").toString();
                    String fromPic = "";
                    if (null != data.get("face")){
                        fromPic = StringUtil.checkIcon(data.get("face").toString());
                    }
                    int giftCount = Integer.valueOf(data.get("ct").toString());
                    int giftCtis = Integer.valueOf(data.get("ctis").toString());
                    Log.d("giftCtis", giftCtis + "");
                    Log.d("giftCount", giftCount + "");
                    String gId = giftId + giftCount + nickName;
                    giftModel = new GiftModel(gId, giftConfig.getName(), 1, giftUrl, "accId",
                            nickName, fromPic, System.currentTimeMillis(), getGiftType(giftCount), giftCount, giftCtis);
                    giftControl.loadGift(giftModel);
                    //刷新贡献榜
                    if (null != data.get("weekOffer") && null != contributionTxt){
                        contributionTxt.setText(data.get("weekOffer").toString());
                    }
                    break;
                case GIVEHEART:
                    ((MainAudienceActivity)parent).addStar(new Random().nextBoolean());
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
            try {
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
                                if ("openPush".equals(cmd)){
                                    TSBSuccess("主播已开播！");
                                    cleanCallback.openPlay();
                                    return;
                                }else if ("closePush".equals(cmd)){
                                    TSBSuccess("直播已关闭！");
                                    cleanCallback.closeVideo();
                                    return;
                                }else if ("kick".equals(cmd)){
                                    Map kickMap = (Map) customMsg.get("data");
                                    if (null != kickMap && null != kickMap.get("accId") && kickMap.get("accId").equals(UserPreferences.getAccId())){
                                        clean();
                                        closeAct(parent);
                                        Toast.makeText(parent, "您已被房管踢出！", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }else if ("guardKick".equals(cmd)){
                                    Map kickMap = (Map) customMsg.get("data");
                                    String gAccId = ((Map)kickMap.get("guard")).get("accId").toString();
                                    String gName = ((Map)kickMap.get("guard")).get("name").toString();
                                    String kAccId = ((Map)kickMap.get("kicked")).get("accId").toString();
                                    String kName = ((Map)kickMap.get("kicked")).get("name").toString();
                                    if (kAccId.equals(UserPreferences.getAccId())){
                                        clean();
                                        closeAct(parent);
                                        return;
                                    }
                                }else if ("setMute".equals(cmd)){
                                    Map muteMap = (Map) customMsg.get("data");
                                    if (muteMap.get("accId").equals(UserPreferences.getAccId()))
                                    Toast.makeText(parent, muteMap.get("content").toString(), Toast.LENGTH_SHORT).show();
                                }
                                messageList.add(message);
                                if (messageList.size() > 100){
                                    messageList.remove(0);
                                }
                                messageAdapter.notifyDataSetChanged();
                                if (!messageRecyclerView.canScrollVertically(1)){//表示是否能向上滚动，false表示已经滚动到底部
                                    messageRecyclerView.smoothScrollToPosition(messageList.size());
                                    scrollToBottom.setVisibility(View.GONE);
                                }else {
                                    scrollToBottom.setVisibility(View.VISIBLE);
                                }
                                Map<?, ?> data = (Map<?, ?>) customMsg.get("data");
                                showMessage(cmd, data);
                            }
                        }
                    }else if (message.getMsgType() == MsgTypeEnum.text){//普通文本消息
                        if (isMyMessage(message)){
//                            if (null == message.getContent() || "".equals(message.getContent())) return;
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
            }catch (NullPointerException e){
                LogUtil.e(e.toString());
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

    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            // 提示被踢出的原因（聊天室已解散、被管理员踢出、被其他端踢出等）
            // 清空缓存数据
            clean();
            closeAct(parent);
            Toast.makeText(parent, "多端被踢，退出直播间！", Toast.LENGTH_SHORT).show();
            return;
        }
    };

    /**
     * 注册接收消息
     * @param register
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
    }

    private Paint mPaint;
    private int layerId;

    private LinearGradient linearGradient;

    //recyclerView顶部半透明遮罩
    public void doTopGradualEffect(){
        mPaint = new Paint();
        final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint.setXfermode(xfermode);
        linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 100.0f, new int[]{0, Color.BLACK}, null, Shader.TileMode.CLAMP);

        messageRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(canvas, parent, state);
                mPaint.setXfermode(xfermode);
                mPaint.setShader(linearGradient);
                canvas.drawRect(0.0f, 0.0f, parent.getRight(), 200.0f, mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(layerId);
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                layerId = c.saveLayer(0.0f, 0.0f, (float) parent.getWidth(), (float) parent.getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
    }

    private void init() {
        imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        personLayout.getBackground().setAlpha(100);
        contributionLayout.getBackground().setAlpha(100);
        adTxt.getBackground().setAlpha(100);

        //创建手势检测器
        detector = new GestureDetector(getContext(), this);
        rootOperateLayout.setOnTouchListener(this);

        final LinearLayoutManager manager = new ScrollSpeedLinearLayoutManger(getContext());
        manager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(manager);
//        messageRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));

        messageAdapter = new ChatRoomMessageAdapter(getContext(), messageList, this);
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
                        fragment.show(fragmentManager,SingleChatInfoDialogFragment.class.getSimpleName());
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
                            ImageSpan imageSpan = new ImageSpan(parent, atBitmap);
                            SpannableString spannableString = new SpannableString(at);
                            spannableString.setSpan(imageSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            messageEdit.setText(spannableString + " ");
                            messageEdit.setSelection(spannableString.length() + 1);
                        }
                    }
                });
                fragment.show(fragmentManager,PersonalDialogFragment.class.getSimpleName());
            }
        });

        giftControl = new GiftControl(giftFrameLayout1, giftFrameLayout2);
        view_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periscope.addHeart();
                if (firstComing){
                    firstComing = false;
                    ChatRoomMessage lightenMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(LiveRoomCache.getRoomId(),"点亮了心！");
                    lightenMessage.setLocalExtension(updateMap);
                    Map<String, Object> ext = new HashMap<>();
                    ext.put("cmd", "lighten");
                    lightenMessage.setRemoteExtension(ext);
                    NIMClient.getService(ChatRoomService.class).sendMessage(lightenMessage, false);
                    messageList.add(lightenMessage);
                    if (messageList.size() > 100){
                        messageList.remove(0);
                    }
                    messageAdapter.notifyDataSetChanged();
                    messageRecyclerView.smoothScrollToPosition(messageList.size());
                }
            }
        });

        mLCountDownTimer = new LCountDownTimer(this);
        mLCountDownTimer.setTimerCoutingMm(3500);
        mLCountDownTimer.setCountDownIntervalMm(100);
        try {
            Bitmap bitmapBg = BitmapFactory.decodeStream(getContext().getAssets().open("giftbutton.png"));
            Bitmap bitmapNum = BitmapFactory.decodeStream(getContext().getAssets().open("number.png"));
            giftButton.setBitmap(bitmapBg, bitmapNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    Toast.makeText(parent, "最多只能输入50个字哦！", Toast.LENGTH_SHORT).show();
                    String text = s.toString().trim().substring(0, 50);
                    messageEdit.setText(text);
                    messageEdit.setSelection(text.length());
                }
            }
        });

        onlinePeople = channel.getPlayerTimes();
    }

    /**
     * 进入云信聊天室
     */
    public void enterRoom(String roomId){
        EnterChatRoomData enterData = new EnterChatRoomData(roomId);
        enterData.setAvatar(StringUtil.checkIcon(UserPreferences.getFace()));
        if (null != SystemCache.getPersonalMsg().getAccount().getName())
        enterData.setNick(SystemCache.getPersonalMsg().getAccount().getName());

        NIMClient.getService(ChatRoomService.class).enterChatRoom(enterData)
                .setCallback(new RequestCallback() {
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
    }

    private void showTop(){
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Owner owner = LiveRoomCache.getOwner();
                    if (owner == null) {
                        if (topLl.getVisibility() == View.GONE){
                            topLl.setVisibility(View.VISIBLE);
                            topLl.setAnimation(AnimationUtil.moveFromViewTop());
                        }
                        return;
                    }
                    Glide.with(parent).load(StringUtil.checkIcon(owner.getFaceSmallUrl()))
                            .error(R.drawable.icon_mine_personal_default)
                            .transform(new GlideCircleTransform(parent))
                            .into(anchorIcon);
                    if (null != owner.getStageName()){
                        anchorName.setText(owner.getStageName());
                    }else if (null != owner.getName()){
                        anchorName.setText(owner.getName());
                    }else {
                        anchorName.setText(owner.getOpenId());
                    }
                    onlineTxt.setText(onlinePeople + "人");
                    getFollowStatus(channel.getOwnerId());
                    contributionTxt.setText(String.valueOf(channel.getWeekOffer()));
                    if (topLl.getVisibility() == View.GONE){
                        topLl.setVisibility(View.VISIBLE);
                        topLl.setAnimation(AnimationUtil.moveFromViewTop());
                    }
                }catch (NullPointerException e){
                    LogUtil.e(e.toString());
                }
            }
        });
    }

    private void showBottom(){
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bottomLl.getVisibility() == View.GONE){
                    bottomLl.setVisibility(View.VISIBLE);
                    bottomLl.setAnimation(AnimationUtil.moveFromViewBootom());
                }
            }
        });
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
                                LogUtil.d("iGold : " + iGold);
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
                                parent.getGiftMessageQueue().setWallet(wallet);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 充值PopWindow
     */
    private void rechargePopWindow() {
        View parentView = ((ViewGroup) parent.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(parent, R.layout.channel_recharge, null);

        TextView recharge_pop_wallet = (TextView) popView.findViewById(R.id.recharge_pop_wallet);
        recharge_pop_wallet.setText("余额：" + wallet + "虎币");

        TextView tv_login = (TextView) popView.findViewById(R.id.tv_recharge);
        View login_close = popView.findViewById(R.id.login_close);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
        lp.alpha = 0.4f;
        parent.getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白消失
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_recharge:
                        Intent intent = new Intent(parent, Recharge.class);
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
                WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
                lp.alpha = 1f;
                parent.getWindow().setAttributes(lp);
            }
        });

        tv_login.setOnClickListener(listener);
        login_close.setOnClickListener(listener);

        popWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void enterChannel(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.ENTERCHANNEL)
                .addParam("device", SystemCache.DEVICE)
                .addParam("cId", channel.getCId())
                .addParam("sessionId", "0")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.get("code").equals(ResultCode.SUCCESS.getCode())) {
                                JSONObject data = js.getJSONObject("data");

                                String welcomeMsg = data.getString("welcome");
                                View heardView = LayoutInflater.from(parent).inflate(R.layout.chatroommessage_item_header_layout,null);
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
                                LogUtil.d("webSocket enter channel failed !");
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
                .addParam("cId", channel.getCId())
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
//        enterChannel();
//        if (true) return;
//        final String channelId = channel.getCId();
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
//            }
//
//            @Override
//            public void success() {//成功收到进场消息
//                showTop();
//                showBottom();
//                //更新本人在聊天室内的信息
//                updateChannelMemberMeg();
//            }
//
//            @Override
//            public void play(final boolean play) {//直播状态为：1（直播中）
//                parent.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (play){//直播中
////                            cleanCallback.openPlay();
//                        }else {//未直播
////                            cleanCallback.closeVideo();
//                        }
//                    }
//                });
//
//            }
//
//            @Override
//            public void failed(String failedMsg) {
//                LogUtil.e(failedMsg);
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
////                doTopGradualEffect();//顶部遮罩
//                try {
//                    parent.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            View heardView = LayoutInflater.from(parent).inflate(R.layout.chatroommessage_item_header_layout,null);
//                            TextView heardMsg = (TextView) heardView.findViewById(R.id.chatroommessage_heardview_heardmsg);
//                            heardMsg.setText(welcomeMsg);
//                            messageAdapter.addHeaderView(heardView);
//                        }
//                    });
//                }catch (Exception e){
//                    LogUtil.e("webSocket  onWelcome  NullPointException!");
//                    MobclickAgent.reportError(parent, "CATCH >>> " + e.toString());
//                }
//            }
//
//            @Override
//            public void recharge(String msg) {
//                parent.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        rechargePopWindow();
//                    }
//                });
//            }
//
//            @Override
//            public void guard(String msg) {
//                Intent intent = new Intent(parent, MyReactActivity.class);
//                intent.putExtra("accId", UserPreferences.getAccId());
//                intent.putExtra("token", UserPreferences.getToken());
//                intent.putExtra("cId", LiveRoomCache.getChannelId());
//                MyReactActivity.mainName = "GuardIndex";
//                startActivity(intent);
//            }
//        });
//        webSocket.connect();
//    }

//    private boolean isNavigationBarShow(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Display display = parent.getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            Point realSize = new Point();
//            display.getSize(size);
//            display.getRealSize(realSize);
//            return realSize.y!=size.y;
//        }else {
//            boolean menu = ViewConfiguration.get(parent).hasPermanentMenuKey();
//            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//            if(menu || back) {
//                return false;
//            }else {
//                return true;
//            }
//        }
//    }
//
//    private int getNavigationBarHeight() {
//        if (!isNavigationBarShow()){
//            return 0;
//        }
//        Resources resources = getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height",
//                "dimen", "android");
//        //获取NavigationBar的高度
//        int height = resources.getDimensionPixelSize(resourceId);
//        return height;
//    }
//
//
//    private int getSceenHeight() {
//        int screenHeight =  parent.getWindow().getDecorView().getRootView().getHeight();
//        int i = parent.getWindowManager().getDefaultDisplay().getHeight()+getNavigationBarHeight();
//        if (screenHeight == i){
//            return 0;
//        }
//        return 1;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getFragmentManager();
        parent = (MainAudienceActivity)getActivity();

        if (isHUAWEI() && checkDeviceHasNavigationBar(getContext())){

            navigationBar = getNavigationBarHehght();
            LogUtil.d("navigationBar height is :" + navigationBar);
            parent.getContentResolver().registerContentObserver(Settings.System.getUriFor
                    ("navigationbar_is_min"), true, mNavigationStatusObserver);
        }

        root.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        lp = (LinearLayout.LayoutParams) input.getLayoutParams();
        initEgret(parent);
        init();
        initSofaList();
        initUmeng();
        getWallet();
        IntentFilter cleanFilter = new IntentFilter(WelcomeActivity.action);
        parent.registerReceiver(broadcastReceiver, cleanFilter);
        IntentFilter attentionAilter = new IntentFilter(action);
        parent.registerReceiver(attentionBroadcastReceiver, attentionAilter);
        fetchOnlineCount();

        final GestureDetector mGestureDetector = new GestureDetector(parent, this);
        MainAudienceActivity.MyOnTouchListener myOnTouchListener = new MainAudienceActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };
        ((MainAudienceActivity) parent).registerMyOnTouchListener(myOnTouchListener);
//        messageThread = ((MainAudienceActivity)parent).getMessageThread();
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
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }, 800, 800);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        parent.unregisterReceiver(broadcastReceiver);
        parent.unregisterReceiver(attentionBroadcastReceiver);
        if (null != messageList){
            messageList.clear();
            messageList = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LogUtil.d("Timer" + msg.obj.toString());
                    giftButton.changeTime(Integer.parseInt(msg.obj.toString()));
                    break;
                case 2:
                    startTimer();
                    if (giftButton.getVisibility() == View.GONE) giftButton.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    periscope.addHeart();
                    break;
                case 4:
                    if (giftButton.getVisibility() == View.VISIBLE) giftButton.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    public void clearEgret(){
        if (engineInited){
            gameEngine.game_engine_onStop();
        }
    }

    @Override
    public void refreshSofa() {//进入房间刷新沙发列表
        getSofaListData();
    }

    @Override
    public void giftSend(String giftId, String giftCount, boolean continueAble) {
        gCtis = 1;
        gId = giftId;
        gCount = giftCount;
        GiftMsg giftMsg = new GiftMsg();
        giftMsg.setcId(channel.getcId());
        giftMsg.setGiftId(gId);
        giftMsg.setGiftCount(gCount);
        giftMsg.setContinuous(gCtis);

        parent.getGiftMessageQueue().addGift(giftMsg);
        if (continueAble){
            Message msg = handler.obtainMessage();
            msg.what = 2;
            handler.sendMessage(msg);
        }else {
            Message msg = handler.obtainMessage();
            msg.what = 4;
            handler.sendMessage(msg);
        }
//        GiftHelper.postTask(new MessageThread(giftMsg, new MessageThread.MessageThreadCallback() {
//            @Override
//            public void recharge(String msg) {
//                rechargePopWindow();
//            }
//
//            @Override
//            public void guard(String msg) {
//                Intent intent = new Intent(parent, MyReactActivity.class);
//                intent.putExtra("accId", UserPreferences.getAccId());
//                intent.putExtra("token", UserPreferences.getToken());
//                intent.putExtra("cId", LiveRoomCache.getChannelId());
//                MyReactActivity.mainName = "GuardIndex";
//                startActivity(intent);
//            }
//        }));
//
        if (continueAble){
            Message msg = handler.obtainMessage();
            msg.what = 2;
            handler.sendMessage(msg);
        }else {
            Message msg = handler.obtainMessage();
            msg.what = 4;
            handler.sendMessage(msg);
        }

//        messageThread.addGift(giftMsg);
//        giftMsg = new StringBuilder("{\"cmd\":\"gift\",\"params\":{\"id\":\"" + gId + "\",\"ct\":\"" + gCount + "\",\"ctis\":\"" + gCtis + "\"}}");
//        if (null != webSocket && webSocket.isOpen()){
//            LogUtil.d("webSocket send gift" + "\n" + giftMsg);
//            webSocket.send(giftMsg.toString());
//            if (continueAble){
//                Message msg = handler.obtainMessage();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }else {
//                Message msg = handler.obtainMessage();
//                msg.what = 4;
//                handler.sendMessage(msg);
//            }
//        }else {
//            TSBError("webSocket is lost");
//            LogUtil.d("webSocket lose to connect");
//            webSocketLost = true;
//            isGift = true;
//            initWebSocket();
//        }
    }

    private void initUmeng() {
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(parent).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        new ShareAction(parent)
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
        UMImage image = new UMImage(parent, API.OSS_URL_CHANNELCOVER + iconUrl);//网络图片
        UMWeb web = new UMWeb(SystemCache.getShareUrl() + LiveRoomCache.getChannelId());
        web.setTitle(name + "正在直播");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("再不看直播你就掉队啦，主播" + name + "正在直播，快让主播带你飞。");//描述
        return web;
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

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(final SHARE_MEDIA platform) {
            LogUtil.d("Umeng  share success !");
            //分享开始的回调
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.SHARE)
                    .addParam("cId", channel.getCId())
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

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void countDowning(final long count) {
//        if (count!=0 && (((count/1000+2)%4)==0)){
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = count/100;
        handler.sendMessage(msg);
//        }
    }

    @Override
    public void end() {

    }

    //将该Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (rootView.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_HIDE_NAVIGATION){
//            LogUtil.d("activity  onTouch !");
//            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
        return detector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent arg0)
    {
        //当触碰事件按下时触发该方法
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2
            , float velocityX, float velocityY)
    {

        if (isShowOperate && e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            //当用户手指在屏幕上“滚动”时触发该方法
            if (!isBroadside) {
                isBroadside = true;
                BroadsideDialogFragment fragment = new BroadsideDialogFragment(new BroadsideDialogFragment.BroadCallback() {
                    @Override
                    public void onDissmise() {
                        isBroadside = false;
                    }

                    @Override
                    public void onChencins() {
                        HeartDialogFragment fragment = HeartDialogFragment.newInstance();
                        fragment.show(fragmentManager,HeartDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void onSingleChat() {
                        SingleChatDialogFragment fragment = new SingleChatDialogFragment();
                        fragment.show(fragmentManager,SingleChatDialogFragment.class.getSimpleName());
                        singleImg.setImageResource(R.drawable.bt_operate_bottom_singlechat);
                    }

                    @Override
                    public void onClean() {
                        cleanCallback.cleanOperate();
                    }

                    @Override
                    public void enterChannel(String cId, String pullUrl, String coverImage, String roomId, String channelId) {
                    }
                });
                fragment.show(fragmentManager,BroadsideDialogFragment.class.getSimpleName());
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        //当用户手指在屏幕上长按时触发该方法
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2
            , float distanceX, float distanceY)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
        //当用户手指在触摸屏上按下，且未移动、松开时触发该方法
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        //当用户手指在触摸屏上轻击事件将触发该方法
        return false;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            parent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clean();
                    closeAct(parent);
                }
            });
            LogUtil.d("BroadcastReceiver========");
        }
    };

    /**
     * 是否是华为
     */
    private boolean isHUAWEI() {
        LogUtil.d("the phone is HUAWEI");
        return Build.MANUFACTURER.equals("HUAWEI");
    }

    //获取是否存在NavigationBar
    private boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
                LogUtil.d("not has NavigationBar ");
            } else if ("0".equals(navBarOverride)) {
                LogUtil.d("has NavigationBar ");
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            try {
                int navigationBarIsMin = Settings.System.getInt(parent.getContentResolver(),
                        "navigationbar_is_min", 0);
                if (navigationBarIsMin == 1) {
                    //导航键隐藏了
                    LogUtil.d("导航键隐藏了");
                    lp.bottomMargin = 0;
                    input.setLayoutParams(lp);
                } else {
                    //导航键显示了
                    LogUtil.d("导航键显示了");
                    lp.bottomMargin = navigationBar;
                    input.setLayoutParams(lp);
                }
            }catch (Exception e){
                LogUtil.e(e.toString());
                MobclickAgent.reportError(parent, "CATCH >>> " + e.toString());
            }
        }
    };

    private int getNavigationBarHehght(){
        int rid = getContext().getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0){
            int resule = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return getContext().getResources().getDimensionPixelSize(resule);
        }
        return 0;
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            try {
                LogUtil.d("globalLayoutListener-------------");
                // 应用可以显示的区域。此处包括应用占用的区域，
                // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
                Rect r = new Rect();
                root.getWindowVisibleDisplayFrame(r);

                // 屏幕高度。这个高度不含虚拟按键的高度
                int screenHeight = root.getRootView().getHeight();

                int heightDiff = screenHeight - (r.bottom - r.top);
                LogUtil.d("heightDiff : " + heightDiff);
                LogUtil.d("statusBarHeight : " + SystemCache.getStatusBarHeight());
                // 在不显示软键盘时，heightDiff等于状态栏的高度
                // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
                // 所以heightDiff大于状态栏高度时表示软键盘出现了，
                // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
                if(keyboardHeight == 0 && heightDiff > SystemCache.getStatusBarHeight()){
                    keyboardHeight = heightDiff - SystemCache.getStatusBarHeight();
                    LogUtil.d("keyboardHeight : " + keyboardHeight);
                }

                if (isShowKeyboard) {
                    // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                    // 说明这时软键盘已经收起
                    if (heightDiff <= SystemCache.getStatusBarHeight()) {
                        isShowKeyboard = false;
                        onHideKeyboard();
                    }
                } else {
                    // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                    // 说明这时软键盘已经弹出
                    if (heightDiff > SystemCache.getStatusBarHeight()) {
                        isShowKeyboard = true;
                        onShowKeyboard();
                    }
                }
            }catch (NullPointerException e){
                LogUtil.e(e.toString());
            }
        }
    };

    private void onShowKeyboard() {
        // 在这里处理软键盘弹出的回调
        LogUtil.d("软键盘弹出： " + keyboardHeight);
        bottomLl.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
//        messageEdit.setFocusable(true);
//        messageEdit.setFocusableInTouchMode(true);
//        messageEdit.requestFocus();
    }

    private void onHideKeyboard() {
        // 在这里处理软键盘收回的回调
        LogUtil.d("软键盘收起");
        if (inputType == InputTypeEnum.EMOJI){
            lp.bottomMargin = 0;
            input.setLayoutParams(lp);
        }else {
            if (isAt) isAt = false;
            if (isBarrageOpen){
                isBarrageOpen = false;
                braagerImg.setImageResource(R.drawable.barrage_close);
                isBarrageOpen = false;
            }
            bottomLl.setVisibility(View.VISIBLE);
            messageLayout.setVisibility(View.GONE);
            messageEdit.setText("");
            pickerView.setVisibility(View.GONE);
            if (inputType == InputTypeEnum.EMOJI){
                inputType = InputTypeEnum.KEYBOARD;
                emojiImg.setImageResource(R.drawable.nim_message_input_emotion);
            }
        }
    }
}
