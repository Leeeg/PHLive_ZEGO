package com.i5i58.live.home.dialogFragment;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.imageVIew.CircleImageView;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;

/**
 * Created by Lee on 2017/4/27.
 */

public class HeartDialogFragment extends BaseDialogFragment {

    private static HeartDialogFragment dialogFragment;

    private final String CANGET = "今日签到可领取:星x";
    private final String GETTED = "已签到，已领取当日星星 ";
    private int maxHeart = 0;

    private ProgressBar pb_heart;
    private TextView tv_day;
    private TextView tv_onlinepeoplenum;
    private TextView tv_brightness;
    private TextView tv_stars;
    private Button bt_getheart;
    private CircleImageView iconImg;
    private TextView nickNameTxt;

    public static HeartDialogFragment newInstance() {
        dialogFragment = new HeartDialogFragment();
        return dialogFragment;
    }

    /**
     * 赠送爱心
     */
    @Event(R.id.iv_bigstar)
    private void giveHeart(View v) {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GIVEHEART)
                .addParam("cId", LiveRoomCache.getChannelId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.getString("code").equals("success") && js.has("data")) {
                                    if (js.get("code").toString().equals("success")) {
                                        JSONObject data = js.getJSONObject("data");
                                        int mHeart = 0;
                                        if(data.has("heart")){
                                            mHeart = Integer.parseInt(data.getString("heart"));
                                        }
                                        tv_stars.setText(String.valueOf(mHeart));
                                        pb_heart.setProgress(maxHeart - mHeart);
                                        getHeart();
                                    } else {
                                        TSBError(js.getString("data"));
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
     * 领取爱心
     */
    @Event(R.id.bt_getheart)
    private void takeHeart(View v) {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.TAKEHEART)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.getString("code").equals("success") && js.has("data")) {
                                    tv_stars.setText(String.valueOf(maxHeart));
                                    tv_day.setText(String.valueOf(Integer.parseInt(tv_day.getText().toString())+1));
                                    bt_getheart.setBackgroundDrawable(getResources().getDrawable(R.drawable.dfrg_heart_bluebg));
                                    bt_getheart.setText(GETTED);
                                }else {
                                    TSBError(js.getString("msg"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取爱心信息
     */
    private void getHeart() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETHEART)
                .addParam("cId", LiveRoomCache.getChannelId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.getString("code").equals("success") && js.has("data")) {
                                    JSONObject data = js.getJSONObject("data");
                                    maxHeart = Integer.parseInt(data.getString("heartMax"));
                                    pb_heart.setMax(maxHeart);
                                    tv_brightness.setText(data.getString("bright"));
                                    tv_onlinepeoplenum.setText(data.getString("HeartUserCount"));
                                    tv_day.setText(data.getString("continu"));
                                    if (data.has("dailyHeart")) {
                                        int st = 0;
                                        if(data.getJSONObject("dailyHeart").has("heart")){
                                            st = Integer.parseInt(data.getJSONObject("dailyHeart").getString("heart"));
                                        }
                                        pb_heart.setProgress(maxHeart - st);
                                        bt_getheart.setText(GETTED);
                                        tv_stars.setText(String.valueOf(st));
                                        bt_getheart.setBackgroundDrawable(getResources().getDrawable(R.drawable.dfrg_heart_bluebg));
                                    }else{
                                        bt_getheart.setText(CANGET + maxHeart);
                                        tv_stars.setText("0");
                                        pb_heart.setProgress(0);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void init(View view) {
        pb_heart = (ProgressBar) view.findViewById(R.id.pb_heart);
        tv_day = (TextView) view.findViewById(R.id.tv_day);
        tv_onlinepeoplenum = (TextView) view.findViewById(R.id.tv_onlinepeoplenum);
        tv_brightness = (TextView) view.findViewById(R.id.tv_heartnum);
        tv_stars = (TextView) view.findViewById(R.id.tv_stars);
        bt_getheart = (Button) view.findViewById(R.id.bt_getheart);
        iconImg = (CircleImageView) view.findViewById(R.id.civ_persion);
        nickNameTxt = (TextView) view.findViewById(R.id.tv_owner_name);

        Glide.with(getActivity()).load(StringUtil.checkIcon(LiveRoomCache.getOwner().getFaceSmallUrl()))
                .error(R.drawable.icon_loginbyphone_default)
                .into(iconImg);
        nickNameTxt.setText(LiveRoomCache.getOwner().getName());
        getHeart();
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
        View view = inflater.inflate(R.layout.dfrg_heart, null);
        init(view);
        return view;
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
}
