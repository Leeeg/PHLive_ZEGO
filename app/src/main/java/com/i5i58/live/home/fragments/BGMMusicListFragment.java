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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/5/3.
 */

@ContentView(R.layout.dfrg_bgm_musiclist)
public class BGMMusicListFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void init() {

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
                    case R.id.delete:
                        int pos = getAdapterPosition();
                        mChatData.remove(pos);
                        notifyItemRemoved(pos);
                        break;
                }
            }

        }
    }

}
