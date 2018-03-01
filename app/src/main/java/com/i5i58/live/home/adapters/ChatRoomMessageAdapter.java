package com.i5i58.live.home.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.imgUtil.BitmapUtil;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.emoji.MoonUtil;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.home.helper.MessageCallBack;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/5/4.
 */

public class ChatRoomMessageAdapter extends RecyclerView.Adapter<ChatRoomMessageAdapter.MyHolder>{

    private final String GIVEHEART = "giveHeart";
    private final String FOLLWOANCHOR = "followAnchor";
    private final String OPENCLUB = "openClub";
    private final String GIFT = "gift";
    private final String OPENGUARD = "openGuard";
    private final String ENTER = "enter";
    private final String CONNMIC = "connMic";
    private final String DRIFTCOMMENT = "driftComment";
    private final String SHARE = "share";
    private final String KICK = "kick";
    private final String GUARDKICK = "guardKick";
    private final String SETMUTE = "setMute";
    private final String LIGHTEN = "lighten";

    private RecyclerView mRecyclerView;
    private List<ChatRoomMessage> roomMessageList;
    private Context mContext;
    private MessageCallBack messageCallBack;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;

    public ChatRoomMessageAdapter(Context mContext, List<ChatRoomMessage> data, MessageCallBack messageCallBack) {
        this.roomMessageList = data;
        this.mContext = mContext;
        this.messageCallBack = messageCallBack;
    }

    @Override
    public ChatRoomMessageAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new MyHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new MyHolder(VIEW_HEADER);
        } else {
            return new MyHolder(getLayout(R.layout.item_chatroommessage_recycler));
        }
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //强制关闭复用
        try {
            holder.itemView.setTag(position);
            holder.setIsRecyclable(false);
            if (!isHeaderView(position) && !isFooterView(position)) {
                if (haveHeaderView()) position--;
                ChatRoomMessage roomMessage = roomMessageList.get(position);
                if (roomMessage.getMsgType() == MsgTypeEnum.custom){//服务器自定义消息
                    Map<?, ?> customMsg = roomMessage.getRemoteExtension();
                    String cmd = customMsg.get("cmd").toString();
                    Map<?, ?> data = (Map<?, ?>) customMsg.get("data");
                    showMessage(cmd, data, holder, roomMessage);
                }else if (roomMessage.getMsgType() == MsgTypeEnum.text){//云信文本消息
                    String nickName = "";
                    String content = "";
                    Map data;
                    ForegroundColorSpan span2 = null;
                    if (roomMessage.getChatRoomMessageExtension() != null) {
                        nickName = roomMessage.getChatRoomMessageExtension().getSenderNick();
                        data = roomMessage.getChatRoomMessageExtension().getSenderExtension();
                    } else {
                        nickName = SystemCache.getPersonalMsg().getAccount().getName() + " ";
                        data = roomMessage.getLocalExtension();
                    }
                    if (null != data){
                        Object iconSuper = data.get("superUser");
                        Object guard = data.get("guard");
                        Object vip = data.get("vip");
                        Object richScore = data.get("richScore");
                        Object clubLevel = data.get("clubLevel");
                        Object fansClub = data.get("fansClub");
                        if (null != iconSuper && Boolean.valueOf(iconSuper.toString())){
                            holder.superIcon.setImageResource(R.drawable.icon_super);
                            holder.superIcon.setVisibility(View.VISIBLE);
                        }
                        if (null != richScore && 0 != Integer.valueOf(richScore.toString())){
                            InputStream richScoreIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(richScore.toString()))));
                            Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                            holder.richIcon.setImageBitmap(richScoreBitmap);
                            holder.richIcon.setVisibility(View.VISIBLE);
                        }else {
                            holder.richIcon.setImageResource(R.drawable.lvl_1);
                            holder.richIcon.setVisibility(View.VISIBLE);

                        }
                        if (null != richScore && 0 != Integer.valueOf(guard.toString())){
                            InputStream guardIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard));
                            Bitmap vipBitmap = BitmapFactory.decodeStream(guardIs);
                            holder.guardIcon.setImageBitmap(vipBitmap);
                            holder.guardIcon.setVisibility(View.VISIBLE);
                        }
                        if (null != richScore && 0 != Integer.valueOf(vip.toString())){
                            InputStream vipIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                            Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                            holder.vipIcon.setImageBitmap(vipBitmap);
                            holder.vipIcon.setVisibility(View.VISIBLE);
                        }
                        if (null != fansClub && 0 != Integer.valueOf(clubLevel.toString())){
                            InputStream fansClubs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "clublevel_" + clubLevel));
                            Bitmap fansClubBitmap = BitmapFactory.decodeStream(fansClubs);
                            String clubName = "粉丝团";
                            if (null != data.get("clubName")) {
                                clubName = data.get("clubName").toString();
                            }
                            holder.fansIcon.setImageBitmap(BitmapUtil.drawNewBitmap(fansClubBitmap, clubName));
                            holder.fansIcon.setVisibility(View.VISIBLE);
                        }
                    }
                    if (null != roomMessage.getRemoteExtension() && null != roomMessage.getRemoteExtension().get("cmd")) {//普通@
                        if (roomMessage.getRemoteExtension().get("cmd").equals("share")){
                            content = "分享了直播间 ";
                            Map shareData = roomMessage.getRemoteExtension();
                            if (null != shareData && null != shareData.get("platform")){
                                InputStream shareIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "share_" + shareData.get("platform")));
                                Bitmap shareBitmap = BitmapFactory.decodeStream(shareIs);
                                holder.shareIcon.setImageBitmap(shareBitmap);
                                holder.shareIcon.setVisibility(View.VISIBLE);
                            }
                        }else if (roomMessage.getRemoteExtension().get("cmd").equals("lighten")){
                            content = "点亮了";
                            holder.shareIcon.setImageResource(R.drawable.point_heart);
                            holder.shareIcon.setVisibility(View.VISIBLE);
                        }else {
                            Map data1 = roomMessage.getRemoteExtension();
                            String taAccId = data1.get("target").toString();
                            String myAccId = SystemCache.getPersonalMsg().getAccount().getAccId();
                            String fromAccId = roomMessage.getFromAccount();
                            if (data1.get("cmd").toString().equals("at") && taAccId.equals(myAccId)) {//自己被@
                                content = data1.get("content").toString();
                                holder.contextTxt.setTextColor(mContext.getResources().getColor(R.color.pink));
                            }else if (data1.get("cmd").toString().equals("at")){//普通观众
                                content = data1.get("content").toString();
                                holder.contextTxt.setTextColor(mContext.getResources().getColor(R.color.white_word));
                            }else {//普通消息
                                content = roomMessage.getContent();
                                holder.contextTxt.setTextColor(mContext.getResources().getColor(R.color.white_word));
                            }
                        }
                    }else {//普通消息
                        content = roomMessage.getContent();
                        holder.contextTxt.setTextColor(mContext.getResources().getColor(R.color.white_word));
                    }
                    ForegroundColorSpan span1 = new ForegroundColorSpan(Color.rgb(255, 218, 129));
                    SpannableStringBuilder builder = new SpannableStringBuilder(nickName); //创建SpannableStringBuilder，并添加前面文案
                    builder.setSpan(span1, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.nickNameTxt.setText(builder);
                    holder.nickNameTxt.setVisibility(View.VISIBLE);

                    SpannableString mSpannableString = MoonUtil.makeSpannableStringTags(mContext, content, 0.45f, ImageSpan.ALIGN_BOTTOM, false);
                    holder.contextTxt.setText(mSpannableString);
                    holder.contextTxt.setVisibility(View.VISIBLE);
                }
            }
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }catch (Resources.NotFoundException e){
            LogUtil.e(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        int count = (roomMessageList == null ? 0 : roomMessageList.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getLayout(int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            return;
//            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    public boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView guardIcon, vipIcon, richIcon, fansIcon, mountIcon, superIcon, shareIcon;
        private TextView nickNameTxt, mountNameTxt, welcomeTxt, welcomeMsgTxt, contextTxt, driveTxt, mountCountTxt, conTextNick;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            guardIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_guard);
            vipIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_vip);
            richIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_richscore);
            fansIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_fansclub);
            mountIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_mountimg);
            superIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_super);
            shareIcon = (ImageView) itemView.findViewById(R.id.item_chatroommessage_share);

            nickNameTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_nickname);
            mountNameTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_mountname);
            welcomeTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_welcome);
            welcomeMsgTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_welcomemsg);
            contextTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_context);
            driveTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_drive);
            mountCountTxt = (TextView) itemView.findViewById(R.id.item_chatroommessage_mountcount);
            conTextNick = (TextView) itemView.findViewById(R.id.item_chatroommessage_context_nick);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private void showMessage(String cmd, Map<?, ?> data, MyHolder holder, ChatRoomMessage roomMessage) {
        try {
            LogUtil.d("ChatRoomMessage CMD: " + cmd);
            Object iconSuper = null;
            Object guard = null;
            Object vip = null;
            Object richScore = null;
            Object clubLevel = null;
            Object fansClub = null;
            if (null != data){
                iconSuper    = data.get("superUser");
                guard        = data.get("guard");
                vip          = data.get("vip");
                richScore    = data.get("richScore");
                clubLevel    = data.get("clubLevel");
                fansClub     = data.get("fansClub");
            }
            if (null != iconSuper){
                holder.superIcon.setImageResource(R.drawable.icon_super);
                holder.superIcon.setVisibility(View.VISIBLE);
            }
            if (null != richScore){
                InputStream richScoreIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(richScore.toString()))));
                Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                holder.richIcon.setImageBitmap(richScoreBitmap);
                holder.richIcon.setVisibility(View.VISIBLE);
            }else {
                holder.richIcon.setImageResource(R.drawable.lvl_1);
                holder.richIcon.setVisibility(View.VISIBLE);

            }
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
            if (null != fansClub && null != clubLevel && 0 != Integer.valueOf(clubLevel.toString())){
                InputStream fansClubs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "clublevel_" + clubLevel));
                Bitmap fansClubBitmap = BitmapFactory.decodeStream(fansClubs);
                String clubName = "粉丝团";
                if (null != data.get("clubName")) {
                    clubName = data.get("clubName").toString();
                }
                holder.fansIcon.setImageBitmap(BitmapUtil.drawNewBitmap(fansClubBitmap, clubName));
                holder.fansIcon.setVisibility(View.VISIBLE);
            }
            switch (cmd){
                case ENTER:
                    holder.welcomeTxt.setVisibility(View.VISIBLE);
                    String name = data.get("name") == null? data.get("accId").toString() : data.get("name").toString();
                    holder.nickNameTxt.setText(name.toString());
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
                    //刷新沙发列表
                    if (null != data.get("indexByViewer")) {
                        if (Integer.valueOf(data.get("indexByViewer").toString()) < 20) {
                            messageCallBack.refreshSofa();
                        }
                    }
                    break;
                case GIFT:
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
                case GIVEHEART:
                case DRIFTCOMMENT:
                    String nickName = "";
                    ForegroundColorSpan span2 = null;
                    if (roomMessage.getChatRoomMessageExtension() != null) {
                        nickName = roomMessage.getChatRoomMessageExtension().getSenderNick();
                    } else {
                        nickName = SystemCache.getPersonalMsg().getAccount().getName() + " ";
                    }
                    String content = " 送了一颗星星";
                    if (cmd.equals(DRIFTCOMMENT)){//漂屏文字
                        try {
                            JSONObject jsonObject = new JSONObject(data.get("content").toString());
                            String type = jsonObject.getString("type");
                            content = " " + jsonObject.getString("contents");
                            String myAccId = SystemCache.getPersonalMsg().getAccount().getAccId();
                            String fromAccId = roomMessage.getFromAccount();
                            if (type.equals("at")){
                                String taAccId = jsonObject.getString("target");
                                if (myAccId.equals(taAccId) || myAccId.equals(fromAccId)){
                                    span2 = new ForegroundColorSpan(Color.rgb(243, 127, 243));
                                }
                            }else {
                                span2 = new ForegroundColorSpan(Color.WHITE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {//送星文字
                        span2 = new ForegroundColorSpan(Color.rgb(163, 160, 248));
                    }
                    ForegroundColorSpan span1 = new ForegroundColorSpan(Color.rgb(241, 177, 104));
                    SpannableStringBuilder builder = new SpannableStringBuilder(nickName); //创建SpannableStringBuilder，并添加前面文案
                    builder.setSpan(span1, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    builder.append(content);
                    builder.setSpan(span2, nickName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.contextTxt.setText(builder);
                    holder.contextTxt.setVisibility(View.VISIBLE);
                    break;
                case OPENCLUB:
                case OPENGUARD:
                case FOLLWOANCHOR:
                case LIGHTEN:
                    holder.nickNameTxt.setText(data.get("name").toString());
                    holder.nickNameTxt.setVisibility(View.VISIBLE);
                    if (cmd.equals(LIGHTEN)){
                        holder.driveTxt.setText(" 点亮了心");
                        holder.driveTxt.setVisibility(View.VISIBLE);
                    }else {
                        holder.driveTxt.setText(data.get("content").toString());
                        holder.driveTxt.setVisibility(View.VISIBLE);
                    }
                    break;
                case KICK://房管踢人
                    String name1 = data.get("name") == null? data.get("accId").toString() : data.get("name").toString();
                    if (data.get("accId").equals(UserPreferences.getAccId())){
                        holder.nickNameTxt.setText(name1);
                        holder.nickNameTxt.setVisibility(View.VISIBLE);
                        holder.driveTxt.setText("被踢出直播间！");
                        holder.driveTxt.setVisibility(View.VISIBLE);
                    }else {
                        holder.nickNameTxt.setText(name1);
                        holder.nickNameTxt.setVisibility(View.VISIBLE);
                        holder.driveTxt.setText("被踢出直播间！");
                        holder.driveTxt.setVisibility(View.VISIBLE);
                    }
                    break;
                case GUARDKICK://骑士踢人
                    Map kickMap = (Map) data.get("kicked");
                    Map guardMap = (Map) data.get("guard");

                    Object iconSuper4 = data.get("superUser");
                    Object guard4 = kickMap.get("guard");
                    Object vip4 = kickMap.get("vip");
                    Object richScore4 = kickMap.get("richScore");
                    Object clubLevel4 = data.get("clubLevel");
                    Object fansClub4 = data.get("fansClub");
                    if (null != iconSuper4){
                        holder.superIcon.setImageResource(R.drawable.icon_super);
                        holder.superIcon.setVisibility(View.VISIBLE);
                    }
                    if (null != guard4){
                        InputStream guardIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard4));
                        Bitmap vipBitmap = BitmapFactory.decodeStream(guardIs);
                        holder.guardIcon.setImageBitmap(vipBitmap);
                        holder.guardIcon.setVisibility(View.VISIBLE);
                    }
                    if (null != vip4){
                        InputStream vipIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip4));
                        Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                        holder.vipIcon.setImageBitmap(vipBitmap);
                        holder.vipIcon.setVisibility(View.VISIBLE);
                    }
                    if (null != richScore4){
                        InputStream richScoreIs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(richScore4.toString()))));
                        Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                        holder.richIcon.setImageBitmap(richScoreBitmap);
                        holder.richIcon.setVisibility(View.VISIBLE);
                    }else {
                        holder.richIcon.setImageResource(R.drawable.lvl_1);
                        holder.richIcon.setVisibility(View.VISIBLE);
                    }
                    if (null != fansClub4 && null != clubLevel4 && 0 != Integer.valueOf(clubLevel4.toString())){
                        InputStream fansClubs = mContext.getResources().openRawResource(FileUtils.getResouceID("drawable", "clublevel_" + clubLevel4));
                        Bitmap fansClubBitmap = BitmapFactory.decodeStream(fansClubs);
                        String clubName = "粉丝团";
                        if (null != data.get("clubName")) {
                            clubName = data.get("clubName").toString();
                        }
                        holder.fansIcon.setImageBitmap(BitmapUtil.drawNewBitmap(fansClubBitmap, clubName));
                        holder.fansIcon.setVisibility(View.VISIBLE);
                    }
                    String gAccId = guardMap.get("accId").toString();
                    String gName = guardMap.get("name").toString();
                    String kAccId = kickMap.get("accId").toString();
                    String kName = kickMap.get("name").toString();
                    String guardName = gName == null? gAccId:gName;
                    String kickEdName = kName == null? kAccId:kName;

                    holder.nickNameTxt.setText(kickEdName);
                    holder.nickNameTxt.setVisibility(View.VISIBLE);
                    holder.driveTxt.setText("被" + guardName + "踢出直播间！");
                    holder.driveTxt.setVisibility(View.VISIBLE);
                    break;
                case SETMUTE:
                    if (data.get("accId").equals(UserPreferences.getAccId())){
                        holder.contextTxt.setText(data.get("content").toString());
                        holder.contextTxt.setTextColor(Color.RED);
                        holder.contextTxt.setVisibility(View.VISIBLE);
                    }else {
                        holder.contextTxt.setText(data.get("content").toString());
                        holder.contextTxt.setTextColor(Color.WHITE);
                        holder.contextTxt.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }catch (Resources.NotFoundException e){
            LogUtil.e(e.toString());
        }
    }

}
