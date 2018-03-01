package com.i5i58.live.home.dialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.i5i58.live.R;
import com.i5i58.live.attachment.SingleChatAttachment;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.common.view.swipeitem.recyclerview.SwipeItemLayout;
import com.i5i58.live.emoji.EmoticonPickerView;
import com.i5i58.live.emoji.IEmoticonSelectedListener;
import com.i5i58.live.emoji.InputTypeEnum;
import com.i5i58.live.home.adapters.SingleInfoMultipleItemQuickAdapter;
import com.i5i58.live.home.entity.SingleInfoMultipleItem;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/27
 */

public class SingleChatInfoDialogFragment extends BaseDialogFragment implements IEmoticonSelectedListener {

    private View root;
    private String name, iconUrl, accId;
    public List<SingleInfoMultipleItem> mChatData = new ArrayList<>();
    public SingleInfoMultipleItemQuickAdapter myAdapter;
    public RecyclerView recyclerView;
    private final int LOAD_MESSAGE_COUNT = 20;
    private List<IMMessage> items = new ArrayList<>();
    private IMMessage anchor;
    private TextView titleTxt;
    private EditText messageEdit;
    private InputMethodManager imm;
    private EmoticonPickerView pickerView;
    private InputTypeEnum inputType = InputTypeEnum.KEYBOARD;
    private ImageView emojiImg;

    public SingleChatInfoDialogFragment() {
    }

    @Event(R.id.single_chat_ib_close)
    private void closeClick(View v){
        NIMClient.getService(MsgService.class).clearUnreadCount(accId, SessionTypeEnum.P2P);
        dismiss();
    }

    @Event(R.id.root_single_info_only)
    private void rootViewClick(View v){
        HideKeyboard(v);
    }

    @Event(R.id.buttonSendMessage)
    private void sendMessage(View v){
        CharSequence msg = messageEdit.getText();
        if (null != msg && !"".equals(msg.toString())){
            SingleChatAttachment attachment = new SingleChatAttachment("channel", "livep2p", msg.toString());
            IMMessage textMessage = MessageBuilder.createCustomMessage(accId, SessionTypeEnum.P2P, "自定义消息", attachment);
            NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
            messageEdit.setText("");
            String content = msg.toString();
            String icon = SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl();;
            SingleInfoMultipleItem multipleItem = new SingleInfoMultipleItem();
            multipleItem.setItemType(SingleInfoMultipleItem.OUT);
            multipleItem.setContent(content);
            multipleItem.setIconUrl(StringUtil.checkIcon(icon));
            mChatData.add(multipleItem);
            myAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(mChatData.size());
        }
    }

    @Event(R.id.emoji_buttonSingle)
    private void onEmojiClick(View view){
        if (inputType == InputTypeEnum.KEYBOARD){
            HideKeyboard(messageEdit);
            pickerView.setVisibility(View.VISIBLE);
            pickerView.show(this);
            emojiImg.setBackgroundResource(R.drawable.icon_input_keyboard);
            inputType = InputTypeEnum.EMOJI;
        }else {
            pickerView.setVisibility(View.GONE);
            messageEdit.setFocusable(true);
            messageEdit.setFocusableInTouchMode(true);
            messageEdit.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            emojiImg.setBackgroundResource(R.drawable.nim_message_input_emotion);
            inputType = InputTypeEnum.KEYBOARD;
        }

    }

    /**
     * 获取历史消息
     */
    private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
        @Override
        public void onResult(int code, final List<IMMessage> messages, Throwable exception) {
            if (messages != null && messages.size() > 0) {
                //历史消息处理
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        mChatData.clear();
                        for (IMMessage msg : messages) {
                            SingleChatAttachment attachment = (SingleChatAttachment) msg.getAttachment();
                            if(null == attachment){
                                continue;
                            }
                            if (attachment.getcId().equals(LiveRoomCache.getChannelId())){
                                String content = attachment.getContent();
                                SingleInfoMultipleItem multipleItem = new SingleInfoMultipleItem();
                                String icon = "";
                                if (msg.getDirect().getValue() == 1) {
                                    multipleItem.setItemType(SingleInfoMultipleItem.IN);
                                    icon = iconUrl;
                                } else{
                                    multipleItem.setItemType(SingleInfoMultipleItem.OUT);
                                    icon = SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl();;
                                }
                                multipleItem.setContent(content);
                                multipleItem.setIconUrl(StringUtil.checkIcon(icon));
                                mChatData.add(multipleItem);
                            }
                        }

                        IMMessage lastMsg = messages.get(messages.size() -1);
                        if (!lastMsg.isRemoteRead()) {
                            NIMClient.getService(MsgService.class).sendMessageReceipt(accId, lastMsg);
                        }

                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        }
    };

    private IMMessage anchor(String sessionId, QueryDirectionEnum direction) {
        if (items.size() == 0) {
            return anchor == null ? MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.P2P, 0) : anchor;
        } else {
            int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
            return items.get(index);
        }
    }

    /**
     * 历史消息
     * @param accId
     * @param direction
     */
    private void getHistory(String accId, QueryDirectionEnum direction) {
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor(accId, direction), direction, LOAD_MESSAGE_COUNT, true)
                .setCallback(callback);
    }

    private void init() {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        titleTxt = (TextView) root.findViewById(R.id.single_chat_tv_title_only);
        titleTxt.setText(name);
        messageEdit = (EditText) root.findViewById(R.id.editTextSingleMessage);
        messageEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    pickerView.setVisibility(View.GONE);
                    imm.showSoftInput(messageEdit, 0);
                    emojiImg.setBackgroundResource(R.drawable.nim_message_input_emotion);
                    inputType = InputTypeEnum.KEYBOARD;
                }
                return false;
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.single_chat_info_only);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        myAdapter = new SingleInfoMultipleItemQuickAdapter(getActivity(), mChatData);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 15, getResources().getColor(R.color.transparent)));
        myAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                HideKeyboard(view);
            }
        });

        pickerView = (EmoticonPickerView) root.findViewById(R.id.emoticon_picker_view);
        emojiImg = (ImageView) root.findViewById(R.id.emoji_buttonSingle);
    }

    public SingleChatInfoDialogFragment(String name, String iconUrl, String accId) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.accId = accId;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void receiveMessageInfo(final List<IMMessage> messages){
        //收到消息
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (IMMessage msg : messages) {
                    if (null == accId || !msg.getFromAccount().equals(accId)){
                        continue;
                    }
                    SingleChatAttachment attachment = (SingleChatAttachment) msg.getAttachment();
                    if(null == attachment){
                        continue;
                    }
                    String content = attachment.getContent();
                    SingleInfoMultipleItem multipleItem = new SingleInfoMultipleItem();
                    String icon = "";
                    if (msg.getDirect().getValue() == 1) {
                        multipleItem.setItemType(SingleInfoMultipleItem.IN);
                        icon = iconUrl;
                    } else{
                        multipleItem.setItemType(SingleInfoMultipleItem.OUT);
                        icon = SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl();
                    }
                    multipleItem.setContent(content);
                    multipleItem.setIconUrl(StringUtil.checkIcon(icon));
                    mChatData.add(multipleItem);
                }

                IMMessage lastMsg = messages.get(messages.size() -1);
                if (!lastMsg.isRemoteRead()) {
                    NIMClient.getService(MsgService.class).sendMessageReceipt(accId, lastMsg);
                }

                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        receiveMessageInfo(messages);
        }
    };

    private void register(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加这句话去掉自带的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        register(true);
        if (null == root){
            root = inflater.inflate(R.layout.dfrg_singlechat_info_only, null);
            init();
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.dimAmount = 0.0f;
        params.windowAnimations = R.style.dialog;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        inputType = InputTypeEnum.KEYBOARD;
        register(false);
    }

    // 关闭键盘
    public void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
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
}
