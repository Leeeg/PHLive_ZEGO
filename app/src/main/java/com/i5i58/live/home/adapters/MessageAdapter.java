package com.i5i58.live.home.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.imgUtil.BitmapUtil;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.MessageCallBack;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import org.xutils.common.util.LogUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/5/5.
 */

public class MessageAdapter extends BaseAdapter {

    private final String GIVEHEART = "giveHeart";
    private final String FOLLWOANCHOR = "followAnchor";
    private final String OPENCLUB = "openClub";
    private final String GIFT = "gift";
    private final String ENTER = "enter";
    private final String CONNMIC = "connMic";
    private final String FRIFTCOMMENT = "driftComment";

    private Context mContext;
    private List<ChatRoomMessage> roomMessageList;
    private LayoutInflater inflater;
    Map<Integer, View> tMap = new HashMap<>();
    private MessageCallBack messageCallBack;

    public MessageAdapter(Context context, List<ChatRoomMessage> list, MessageCallBack messageCallBack) {
        super();
        this.mContext = context;
        this.roomMessageList = list;
        this.messageCallBack = messageCallBack;
    }

    @Override
    public int getCount() {

        return roomMessageList.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (tMap.get(position) == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_chatroommessage_recycler, null);
            holder = new ViewHolder();

            holder.guardIcon            = (ImageView) convertView.findViewById(R.id.item_chatroommessage_guard);
            holder.vipIcon              = (ImageView) convertView.findViewById(R.id.item_chatroommessage_vip);
            holder.richIcon             = (ImageView) convertView.findViewById(R.id.item_chatroommessage_richscore);
            holder.fansIcon             = (ImageView) convertView.findViewById(R.id.item_chatroommessage_fansclub);
            holder.mountIcon            = (ImageView) convertView.findViewById(R.id.item_chatroommessage_mountimg);
            holder.nickNameTxt          = (TextView) convertView.findViewById(R.id.item_chatroommessage_nickname);
            holder.mountNameTxt         = (TextView) convertView.findViewById(R.id.item_chatroommessage_mountname);
            holder.welcomeTxt           = (TextView) convertView.findViewById(R.id.item_chatroommessage_welcome);
            holder.welcomeMsgTxt        = (TextView) convertView.findViewById(R.id.item_chatroommessage_welcomemsg);
            holder.contextTxt           = (TextView) convertView.findViewById(R.id.item_chatroommessage_context);
            holder.driveTxt             = (TextView) convertView.findViewById(R.id.item_chatroommessage_drive);
            holder.mountCountTxt        = (TextView) convertView.findViewById(R.id.item_chatroommessage_mountcount);

            tMap.put(position, null);
            convertView.setTag(holder);
        } else {
            convertView = tMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        ChatRoomMessage roomMessage = roomMessageList.get(position);
        if (roomMessage.getMsgType() == MsgTypeEnum.custom){//服务器自定义消息
            Map<?, ?> customMsg = roomMessage.getRemoteExtension();
            String cmd = customMsg.get("cmd").toString();
            Map<?, ?> data = (Map<?, ?>) customMsg.get("data");
            showMessage(cmd, data, holder);
        }else if (roomMessage.getMsgType() == MsgTypeEnum.text){//云信文本消息
            String nickName = "";
            if (roomMessage.getChatRoomMessageExtension() != null) {
                nickName = roomMessage.getChatRoomMessageExtension().getSenderNick();
            } else {
                nickName = SystemCache.getPersonalMsg().getAccount().getName() + " ";
            }
            String content = roomMessage.getContent();
            ForegroundColorSpan span1 = new ForegroundColorSpan(Color.rgb(241, 177, 104));
            ForegroundColorSpan span2 = new ForegroundColorSpan(Color.WHITE);
            SpannableStringBuilder builder = new SpannableStringBuilder(nickName); //创建SpannableStringBuilder，并添加前面文案
            builder.setSpan(span1, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.append(content);
            builder.setSpan(span2, nickName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.contextTxt.setText(builder);
            holder.contextTxt.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView guardIcon, vipIcon, richIcon, fansIcon, mountIcon;
        private TextView nickNameTxt, mountNameTxt, welcomeTxt, welcomeMsgTxt, contextTxt, driveTxt, mountCountTxt;

    }

    private void showMessage(String cmd, Map<?, ?> data, ViewHolder holder) {
        LogUtil.d("ChatRoomMessage CMD: " + cmd);
        Object guard = data.get("guard");
        Object vip = data.get("vip");
        Object richScore = data.get("richScore");
        switch (cmd){
            case ENTER:
                holder.welcomeTxt.setVisibility(View.VISIBLE);
                if (null != guard){
                    InputStream guardIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard));
                    Bitmap vipBitmap = BitmapFactory.decodeStream(guardIs);
                    holder.guardIcon.setImageBitmap(vipBitmap);
                    holder.guardIcon.setVisibility(View.VISIBLE);
                }
                if (null != vip){
                    InputStream vipIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                    Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                    holder.vipIcon.setImageBitmap(vipBitmap);
                    holder.vipIcon.setVisibility(View.VISIBLE);
                }
                if (null != richScore){
                    InputStream richScoreIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(richScore.toString()))));
                    Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                    holder.richIcon.setImageBitmap(richScoreBitmap);
                    holder.richIcon.setVisibility(View.VISIBLE);
                }
                if (null != data.get("fansClub") && 0 < Integer.valueOf(data.get("fansClub").toString())) {
                    InputStream fansClub = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "fans_bg2x"));
                    Bitmap fansClubBitmap = BitmapFactory.decodeStream(fansClub);
                    String clubName = "粉丝团";
                    if (null != data.get("clubName")) {
                        clubName = data.get("clubName").toString();
                    }
                    holder.fansIcon.setImageBitmap(BitmapUtil.drawNewBitmap(fansClubBitmap, clubName));
                    holder.fansIcon.setVisibility(View.VISIBLE);
                }
                holder.nickNameTxt.setText(data.get("name").toString());
                holder.nickNameTxt.setVisibility(View.VISIBLE);
                if (null != data.get("mtsId")) {
                    holder.driveTxt.setVisibility(View.VISIBLE);
                    holder.driveTxt.setVisibility(View.VISIBLE);
                    int mountId = 0;
                    if (null != data.get("gMtsId")) {
                        mountId = Integer.valueOf(data.get("gMtsId").toString());
                    }else if (null != data.get("mtsId")) {
                        mountId = Integer.valueOf(data.get("mtsId").toString());
                    }
                    MountConfig mountConfig = LiveRoomCache.getMountConfigMap().get(mountId);
                    holder.mountNameTxt.setText(mountConfig.getName());
                    holder.mountNameTxt.setVisibility(View.VISIBLE);
                    holder.mountIcon.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(API.OSS_URL_MOUNTS + mountConfig.getPath() + ".png").into(holder.mountIcon);
                }
                holder.welcomeMsgTxt.setVisibility(View.VISIBLE);
                break;
            case GIFT:
                if (null != guard){
                    InputStream guardIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard));
                    Bitmap vipBitmap = BitmapFactory.decodeStream(guardIs);
                    holder.guardIcon.setImageBitmap(vipBitmap);
                    holder.guardIcon.setVisibility(View.VISIBLE);
                }
                if (null != vip){
                    InputStream vipIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                    Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                    holder.vipIcon.setImageBitmap(vipBitmap);
                    holder.vipIcon.setVisibility(View.VISIBLE);
                }
                if (null != richScore){
                    InputStream richScoreIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(richScore.toString()))));
                    Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                    holder.richIcon.setImageBitmap(richScoreBitmap);
                    holder.richIcon.setVisibility(View.VISIBLE);
                }
                if (null != data.get("fansClub") && 0 < Integer.valueOf(data.get("fansClub").toString())) {
                    InputStream fansClub = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "fans_bg2x"));
                    Bitmap fansClubBitmap = BitmapFactory.decodeStream(fansClub);
                    String clubName = "粉丝团";
                    if (null != data.get("clubName")) {
                        clubName = data.get("clubName").toString();
                    }
                    holder.fansIcon.setImageBitmap(BitmapUtil.drawNewBitmap(fansClubBitmap, clubName));
                    holder.fansIcon.setVisibility(View.VISIBLE);
                }
                holder.nickNameTxt.setText(data.get("name").toString());
                holder.nickNameTxt.setVisibility(View.VISIBLE);
                GiftConfig giftConfig = LiveRoomCache.getGiftConfigMap().get(data.get("id"));
                holder.mountCountTxt.setText("送" + data.get("ct") + giftConfig.getUnit());
                holder.mountCountTxt.setVisibility(View.VISIBLE);
                holder.mountNameTxt.setText(giftConfig.getName());
                holder.mountNameTxt.setVisibility(View.VISIBLE);
                holder.mountIcon.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(API.OSS_URL_GIFT + giftConfig.getPath() + ".png").into(holder.mountIcon);
                break;
            default:
                break;
        }
    }

}
