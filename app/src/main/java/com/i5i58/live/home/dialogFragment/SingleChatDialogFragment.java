package com.i5i58.live.home.dialogFragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

import com.i5i58.live.R;
import com.i5i58.live.attachment.SingleChatAttachment;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.viewPager.NoHorizontalScrollViewpager;
import com.i5i58.live.home.entity.SingleInfoMultipleItem;
import com.i5i58.live.home.fragments.SingleChatInfoFragment;
import com.i5i58.live.home.fragments.SingleChatListFragment;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.SingleChatCallBack;
import com.i5i58.live.home.helper.ViewPagerScroller;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/27
 */

public class SingleChatDialogFragment extends BaseDialogFragment implements SingleChatCallBack{

    private View root;
    private NoHorizontalScrollViewpager viewpager;
    private String iconUrl, accId;

    private SingleChatListFragment singleChatListFragment;
    private SingleChatInfoFragment singleChatInfoFragment;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    singleChatInfoFragment.myAdapter.notifyDataSetChanged();
                    singleChatInfoFragment.recyclerView.smoothScrollToPosition(singleChatInfoFragment.mChatData.size());
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
                        icon = LiveRoomCache.getOwner().getFaceSmallUrl();
                    }
                    multipleItem.setContent(content);
                    multipleItem.setIconUrl(StringUtil.checkIcon(icon));
                    singleChatInfoFragment.mChatData.add(multipleItem);

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
            if (viewpager.getCurrentItem() == 0){
                singleChatListFragment.inComingMsg(messages);
                LogUtil.d("receiveMessageList===========");
            }else {
                receiveMessageInfo(messages);
                LogUtil.d("receiveMessageInfo===========");
            }
        }
    };

    private void register(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
    }

    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(viewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        singleChatListFragment = new SingleChatListFragment(this);
        singleChatInfoFragment = new SingleChatInfoFragment(this);

        viewpager = (NoHorizontalScrollViewpager) root.findViewById(R.id.single_viewpager);
        setViewPagerScrollSpeed(viewpager, 300);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(singleChatListFragment);
        fragments.add(singleChatInfoFragment);
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getChildFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
//        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0){
//                    receiveMessageList();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

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
            root = inflater.inflate(R.layout.dfrg_singlechat_main, null);
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
        params.windowAnimations = R.style.PopBottom;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明

    }

    @Override
    public void itemClick(String name, String iconUrl, String accId) {
        this.iconUrl = iconUrl;
        this.accId = accId;
        singleChatListFragment.refreshItem(accId);
        singleChatInfoFragment.setTitle(name, iconUrl, accId);
        viewpager.setCurrentItem(1);
    }

    @Override
    public void finish() {
        dismiss();
    }

    @Override
    public void back() {
        viewpager.setCurrentItem(0);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        register(false);
    }

}
