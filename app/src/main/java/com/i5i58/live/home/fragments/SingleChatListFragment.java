package com.i5i58.live.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.attachment.SingleChatAttachment;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.httpUtil.HttpUtils;
import com.i5i58.live.common.utils.httpUtil.Net;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.TimeUtil;
import com.i5i58.live.common.view.imageVIew.MyCircleAngleImageView;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.common.view.swipeitem.recyclerview.SwipeItemLayout;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.SingleChatCallBack;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.liveRoom.ChatEntity;
import com.mabeijianxi.stickydotslib.view.StickyViewHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/5/3.
 */

public class SingleChatListFragment extends BaseFragment {

    private View root;
    private RecyclerView recyclerView;
    private List<ChatEntity> mChatData = new ArrayList<>();
    private MyAdapter myAdapter;
    private SingleChatCallBack singleChatCallBack;
    private Map<String, ChatEntity> entityMap = new HashMap<>();
    private List<RecentContact> myRecents = new ArrayList<>();

    @Event({R.id.single_chat_ib_close, R.id.closeSingleChat})
    private void close(View view){
        singleChatCallBack.finish();
    }

    public SingleChatListFragment() {
    }

    public SingleChatListFragment(SingleChatCallBack singleChatCallBack) {
        this.singleChatCallBack = singleChatCallBack;
    }

    private void getTaPerson(String taAccId, ChatEntity chat) {
        try {
            Net net = new Net();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ta", taAccId);
            net.setMap(map);
            net.setAccid(UserPreferences.getAccId());
            net.setToken(UserPreferences.getToken());
            net.setUrl(API.REST_URL + API.GETTAINFO);
            JSONObject result = new JSONObject(HttpUtils.doPost(net));
            if (result.getString("code").equals("success")) {
                JSONObject account = result.getJSONObject("data").getJSONObject("account");
                chat.setName(account.getString("name"));
                chat.setIconUrl(account.getString("faceSmallUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMsgData(List<RecentContact> recents) {
        for (int i = 0; i < recents.size(); i++) {
            RecentContact msg = recents.get(i);
            SingleChatAttachment attachment = (SingleChatAttachment) msg.getAttachment();
            if (null != attachment){
                myRecents.add(msg);
                ChatEntity chat = new ChatEntity();
                chat.setIndex(i);
                getTaPerson(msg.getContactId(), chat);
                String time = TimeUtil.getTimeShowString(msg.getTime(), true);
                if (null != attachment) {
                    chat.setContent(attachment.getContent());
                }
                chat.setTime(time);
                chat.setAccId(msg.getContactId());
                chat.setPoint(msg.getUnreadCount());
                entityMap.put(msg.getContactId(), chat);
                mChatData.add(chat);
            }
        }
    }

    public void refreshItem(String accId){
        if (entityMap.keySet().contains(accId)) {
            entityMap.get(accId).clranPoint();
            myAdapter.notifyItemChanged(entityMap.get(accId).getIndex());
        }
    }

    public void inComingMsg(final List<IMMessage> messages){
        new Thread() {
            public void run() {
                super.run();
                for (IMMessage msg : messages) {
                    SingleChatAttachment attachment = (SingleChatAttachment) msg.getAttachment();
                    if (null != attachment) {
                        if (attachment.getcId().equals(LiveRoomCache.getChannelId())) {
                            String accId = msg.getFromAccount();
                            if (entityMap.keySet().contains(accId)) {
                                entityMap.get(accId).addPoint();
                                entityMap.get(accId).replaceContent(attachment.getContent());

                                Message message = handler.obtainMessage();
                                message.what = 2;
                                message.obj = entityMap.get(accId).getIndex();
                                handler.sendMessage(message);
                            } else {
                                ChatEntity chat = new ChatEntity();
                                String fromAccount = msg.getFromAccount();
                                getTaPerson(fromAccount, chat);
                                String time = TimeUtil.getTimeShowString(msg.getTime(), true);
                                chat.setContent(attachment.getContent());
                                chat.setTime(time);
                                chat.setAccId(fromAccount);
                                chat.setPoint(1);
                                chat.setIndex(mChatData.size());
                                entityMap.put(fromAccount, chat);
                                mChatData.add(chat);

                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public void getLocalPerple(){
        // 查询最近联系人列表数据
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, final List<RecentContact> recents, Throwable exception) {
                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                    return;
                }
                new Thread() {
                    public void run() {
                        super.run();
                        mChatData.clear();
                        myRecents.clear();
                        if (0 < recents.size()) {
                            getMsgData(recents);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void init() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        myAdapter = new MyAdapter(getContext(), mChatData);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, getResources().getColor(R.color.transparent)));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    myAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    myAdapter.notifyItemChanged((Integer) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == root){
            root = inflater.inflate(R.layout.dfrg_singlechat_list, null);
            init();
            getLocalPerple();
        }
        return root;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
        private Context mContext;
        private List<ChatEntity> mChatData;

        public MyAdapter(Context context, List<ChatEntity> mChatData) {
            mContext = context;
            this.mChatData = mChatData;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.item_singlechat_recycler, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.nameText.setText(mChatData.get(position).getName());
            holder.contentText.setText(mChatData.get(position).getContent());
            holder.timeText.setText(mChatData.get(position).getTime());
            int unReadCount = mChatData.get(position).getPoint();
            if (unReadCount > 0) {
                holder.mDragView.setVisibility(View.VISIBLE);
                holder.mDragView.setText(unReadCount + "");
            }else {
                holder.mDragView.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(StringUtil.checkIcon(mChatData.get(position)
                    .getIconUrl()))
                    .asBitmap()
                    .placeholder(R.drawable.icon_loginbyphone_default)
                    .error(R.drawable.icon_loginbyphone_default)
                    .into(holder.iconImage);

            StickyViewHelper stickyViewHelper = new StickyViewHelper(mContext, holder.mDragView,R.layout.includeview);
//            setViewOut2InRangeUp(stickyViewHelper);
            setViewOutRangeUp(position, stickyViewHelper);
//            setViewInRangeUp(stickyViewHelper, position);
//            setViewInRangeMove(stickyViewHelper);
//            setViewOutRangeMove(stickyViewHelper);

        }

        @Override
        public int getItemCount() {
            return mChatData.size();
        }

        class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private MyCircleAngleImageView iconImage;
            private TextView nameText, contentText, timeText, mDragView;

            Holder(View itemView) {
                super(itemView);

                iconImage = (MyCircleAngleImageView) itemView.findViewById(R.id.chat_list_iv_icon);
                nameText = (TextView) itemView.findViewById(R.id.chat_list_tv_name);
                contentText = (TextView) itemView.findViewById(R.id.chat_list_tv_content);
                timeText = (TextView) itemView.findViewById(R.id.chat_list_tv_time);
                mDragView = (TextView) itemView.findViewById(R.id.mDragView);

                View item = itemView.findViewById(R.id.item_single_recycle);
                View delete = itemView.findViewById(R.id.delete);
                item.setOnClickListener(this);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_single_recycle:
                        ChatEntity chatEntity = mChatData.get(getAdapterPosition());
                        singleChatCallBack.itemClick(chatEntity.getName(), chatEntity.getIconUrl(), chatEntity.getAccId());
                        break;
//                    case R.id.read:
//                        //已读
//                        NIMClient.getService(MsgService.class).clearUnreadCount(mChatData.get(getAdapterPosition()).getAccId(), SessionTypeEnum.P2P);
//                        notifyItemChanged(getAdapterPosition());
//                        break;
                    case R.id.delete:
                        int pos = getAdapterPosition();
                        mChatData.remove(pos);
                        notifyItemRemoved(pos);
                        //删除会话
                        NIMClient.getService(MsgService.class).deleteRecentContact(myRecents.get(pos));
                        break;
                }
            }

        }

        /**
         * view在范围外移动执行此Runnable
         * @param stickyViewHelper
         */
        private void setViewOutRangeMove(StickyViewHelper stickyViewHelper) {
            stickyViewHelper.setViewOutRangeMoveRun(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        /**
         * view在范围内移动指此此Runnable
         * @param stickyViewHelper
         */
        private void setViewInRangeMove(StickyViewHelper stickyViewHelper) {
            stickyViewHelper.setViewInRangeMoveRun(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        /**
         * view没有移出过范围，在范围内松手
         * @param stickyViewHelper
         */
        private void setViewInRangeUp(StickyViewHelper stickyViewHelper, final int position) {
            stickyViewHelper.setViewInRangeUpRun(new Runnable() {
                @Override
                public void run() {
                    String accId = myRecents.get(position).getFromAccount();
                    NIMClient.getService(MsgService.class).clearUnreadCount(accId, SessionTypeEnum.P2P);
                }
            });
        }

        /**
         * view移出范围，最后在范围外松手
         * @param position
         * @param stickyViewHelper
         */
        private void setViewOutRangeUp(final int position, StickyViewHelper stickyViewHelper) {
            stickyViewHelper.setViewOutRangeUpRun(new Runnable() {
                @Override
                public void run() {
                    NIMClient.getService(MsgService.class).clearUnreadCount(mChatData.get(position).getAccId(), SessionTypeEnum.P2P);
                }
            });
        }

        /**
         * view移出过范围，最后在范围内松手执行次Runnable
         * @param stickyViewHelper
         */
        private void setViewOut2InRangeUp(StickyViewHelper stickyViewHelper) {
            stickyViewHelper.setViewOut2InRangeUpRun(new Runnable() {
                @Override
                public void run() {
                    myAdapter.notifyDataSetChanged();
                }
            });
        }

    }

}
