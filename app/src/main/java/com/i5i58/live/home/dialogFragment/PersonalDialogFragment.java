package com.i5i58.live.home.dialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.imageVIew.CircleImageView;
import com.i5i58.live.home.fragments.AudienceOperateFragment;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.liveRoom.TaInfo;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

import java.io.InputStream;

/**
 * Created by Lee on 2017/4/27
 */

public class PersonalDialogFragment extends BaseDialogFragment {

    private CircleImageView circleImageView;
    private String accId, name, iconUrl;
    private TextView nameTex, person_tv_star, person_tv_scor, locationTxt, tv_fanscount, open_guard, attentionTxt, openIdTxt;
    private ImageView genderImg, iv_rank, iv_richScore, iv_vip, iv_anchor;
    private LinearLayout ll_ta, ll_registration;
    private FrameLayout fl_fans;
    private boolean isAttention;

    public static final String ACTION1 = "com.fragment_mac.action";
    public static final String ACTION2 = "com.fragment_tiger.action";

    private boolean manager = false;
    private boolean sofa = false;

    private PersonalCallback personalCallback;
    public interface PersonalCallback{

        void report(String taAccId);

        void attention(boolean isAttention);

        void singleChat(String name, String iconUrl, String accId);

        void at(String accId, String atTarget);

    }

    public PersonalDialogFragment() {

    }

    public PersonalDialogFragment(String accId, PersonalCallback personalCallback) {
        this.personalCallback = personalCallback;
        this.accId = accId;
    }

    public PersonalDialogFragment(String accId, boolean manager, boolean sofa, PersonalCallback personalCallback) {
        this.personalCallback = personalCallback;
        this.accId = accId;
        this.manager = manager;
        this.sofa = sofa;
    }

    @Event(R.id.iv_close)
    private void closeReport(View v){//关闭
        dismiss();
    }

    @Event(R.id.fl_fans)
    private void onOpenFansClubClick(View v){//开通粉丝团
        dismiss();
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("cId", LiveRoomCache.getChannelId());
        MyReactActivity.mainName = "FansIndex";
        startActivity(intent);
    }

    @Event(R.id.open_guard)
    private void onOpenGuardClick(View v){//开通骑士
        dismiss();
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("cId", LiveRoomCache.getChannelId());
        MyReactActivity.mainName = "GuardIndex";
        startActivity(intent);
    }

    @Event(R.id.person_home)
    private void onHomePageClick(View v){//主页
        dismiss();
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("ta", accId);
        MyReactActivity.mainName = "HPageIndex";
        startActivity(intent);
    }

    /**
     * 举报
     */
    @Event({R.id.person_img_report, R.id.person_tv_report})
    private void reportPerple(View v){
        dismiss();
        personalCallback.report(accId);
    }

    //私聊
    @Event(R.id.ll_registration)
    private void singleChatClick(View v){
        dismiss();
        personalCallback.singleChat(name, iconUrl, accId);
    }

    //@
    @Event(R.id.ll_ta)
    private void atClick(View v){
        dismiss();
        personalCallback.at(accId, "@" + name);
    }

    /**
     * 关注
     */
    @Event(R.id.tv_cancelfollow)
    private void attentionClick(View v) {
        if (accId.equals(UserPreferences.getAccId())) {
            TSBError("无法关注自己！");
            return;
        }
        if (isAttention) {//取消关注
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.CANNELFOLLOW)
                    .addParam("target", accId)
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                    if (js.getString("code").equals("success")) {
                                        attentionTxt.setText("点击关注");
                                        isAttention = false;
                                        TSBSuccess("已取消");
                                        personalCallback.attention(false);
                                        Intent broad = new Intent(AudienceOperateFragment.action);
                                        broad.putExtra("isAttention", false);
                                        getActivity().sendBroadcast(broad);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {//未关注
            if (manager){
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.FOLLOWMANAGER)
                        .addParam("attAccId", accId)
                        .addParam("cId", LiveRoomCache.getChannelId())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.d("关注主播");
                                            attentionTxt.setText("取消关注");
                                            isAttention = true;
                                            TSBSuccess("已关注！");
                                            personalCallback.attention(true);
                                            Intent broad = new Intent(AudienceOperateFragment.action);
                                            broad.putExtra("isAttention", true);
                                            getActivity().sendBroadcast(broad);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }else {
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.FOLLOW)
                        .addParam("target", accId)
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.d("关注普通用户");
                                            attentionTxt.setText("取消关注");
                                            isAttention = true;
                                            TSBSuccess("已关注！");
                                            personalCallback.attention(true);
                                            Intent broad = new Intent(AudienceOperateFragment.action);
                                            broad.putExtra("isAttention", true);
                                            getActivity().sendBroadcast(broad);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        }
    }

    /**
     * 获取关注状态
     */
    private void isFollow(){
        if (accId.equals(UserPreferences.getAccId())) {
            attentionTxt.setText("点击关注");
            return;
        }
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.ISFOLLOW)
                .addParam("target", accId)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                if (js.getString("code").equals("success")) {
                                    Boolean data = js.getBoolean("data");
                                    if (data) {
                                        attentionTxt.setText("取消关注");
                                        isAttention = true;
                                    } else {
                                        attentionTxt.setText("点击关注");
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

    private void setStarColor(String score) {
        switch (score){
            case "1":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_01));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_1));
                break;
            case "2":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_02));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_2));
                break;
            case "3":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_03));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_3));
                break;
            case "4":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_04));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_4));
                break;
            case "5":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_05));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_5));
                break;
            case "6":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_06));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_6));
                break;
            case "7":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_07));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_7));
                break;
            case "8":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_08));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_8));
                break;
            case "9":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_09));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_9));
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getPersonMsg() {

        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETTAINFO)
                .addParam("ta", accId)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                TaInfo taInfo = GsonInner.getGsonInstance().fromJson(js.getJSONObject("data").toString(), TaInfo.class);
                                Glide.with(getActivity()).load(StringUtil.checkIcon(taInfo.getAccount().getFaceSmallUrl()))
                                        .error(R.drawable.icon_loginbyphone_default)
                                        .into(circleImageView);
                                iconUrl = taInfo.getAccount().getFaceSmallUrl();
                                name = taInfo.getAccount().getName();
                                if (null != taInfo.getAccount().getStageName()){
                                    nameTex.setText(taInfo.getAccount().getStageName() + "(艺名)");
                                }else if (null != taInfo.getAccount().getName()){
                                    nameTex.setText(taInfo.getAccount().getName());
                                }else {
                                    nameTex.setText(taInfo.getAccount().getOpenId());
                                }
                                if (0 != taInfo.getAccount().getGender()){
                                    genderImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_sex_woman));
                                }else {
                                    genderImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_sex_man));
                                }
                                openIdTxt.setText(taInfo.getAccount().getOpenId());
                                String score = String.valueOf(taInfo.getAccount().getScore());
                                long[] scoreArray = MathUtil.checkScore(Long.valueOf(score));
                                person_tv_scor.setText(String.valueOf(scoreArray[1]));
                                person_tv_star.setText(String.valueOf(scoreArray[0]));
                                setStarColor(String.valueOf(scoreArray[0]));
                                long richScore = taInfo.getAccount().getRichScore();
                                InputStream richScoreIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
                                Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                                iv_richScore.setImageBitmap(richScoreBitmap);
                                int vip = taInfo.getAccount().getVip();
                                InputStream vipIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                                Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                                iv_vip.setImageBitmap(vipBitmap);
                                if(taInfo.getAccount().isAnchor()){
                                    iv_anchor.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_anchor_yse));
                                } else {
                                    iv_anchor.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_anchor_no));
                                }
                                if (null != taInfo.getAccount().getLocation()){
                                    locationTxt.setText(StringUtil.getLocation(taInfo.getAccount().getLocation()));
                                }
                                tv_fanscount.setText("粉丝数:" + taInfo.getFansCount());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void init(View view) {
        circleImageView = (CircleImageView) view.findViewById(R.id.ci_persion);
        nameTex = (TextView) view.findViewById(R.id.tv_stagename);
        genderImg = (ImageView) view.findViewById(R.id.iv_gender);
        person_tv_star = (TextView) view.findViewById(R.id.person_tv_star);
        person_tv_scor = (TextView) view.findViewById(R.id.person_tv_scor);
        iv_rank = (ImageView) view.findViewById(R.id.iv_rank);
        iv_richScore = (ImageView) view.findViewById(R.id.iv_richScore);
        iv_vip = (ImageView) view.findViewById(R.id.iv_vip);
        iv_anchor = (ImageView) view.findViewById(R.id.iv_anchor);
        locationTxt = (TextView) view.findViewById(R.id.tv_address);
        tv_fanscount = (TextView) view.findViewById(R.id.tv_fanscount);
        open_guard = (TextView) view.findViewById(R.id.open_guard);
        ll_ta = (LinearLayout) view.findViewById(R.id.ll_ta);
        ll_registration = (LinearLayout) view.findViewById(R.id.ll_registration);
        fl_fans = (FrameLayout) view.findViewById(R.id.fl_fans);
        attentionTxt = (TextView) view.findViewById(R.id.tv_cancelfollow);
        openIdTxt = (TextView) view.findViewById(R.id.owner_openId);
        if (!accId.equals(SystemCache.getPersonalMsg().getAccount().getAccId())){
            ll_registration.setVisibility(View.VISIBLE);
        }
        if (manager) {
            fl_fans.setVisibility(View.VISIBLE);
            open_guard.setVisibility(View.VISIBLE);
            if (!accId.equals(SystemCache.getPersonalMsg().getAccount().getAccId())){
                ll_ta.setVisibility(View.VISIBLE);
            }
        } else if (sofa) {
            if (!accId.equals(SystemCache.getPersonalMsg().getAccount().getAccId())){
                ll_ta.setVisibility(View.VISIBLE);
            }
        }
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
        View view = inflater.inflate(R.layout.dfrg_person, null);
        init(view);
        isFollow();
        getPersonMsg();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.dimAmount = 0.0f;
        params.windowAnimations = R.style.PopupAnimation;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明

    }
}
