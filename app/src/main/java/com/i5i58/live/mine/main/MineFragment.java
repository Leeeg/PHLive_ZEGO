package com.i5i58.live.mine.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoad;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoadCallback;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.listViewUtil.ListViewUtil;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.common.view.spaceImage.SpaceImageDetailActivity;
import com.i5i58.live.mine.activity.EditPersonalActivity;
import com.i5i58.live.mine.activity.MyQrCodeActivity;
import com.i5i58.live.mine.activity.SettingActivity;
import com.i5i58.live.mine.adapter.RnListAdapter;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.account.PersonalMsg;
import com.i5i58.live.model.entity.rnConfig.RNConfig;
import com.i5i58.live.model.entity.rnConfig.React;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;
import com.i5i58.live.recharge.Recharge;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/14.
 */

@ContentView(R.layout.frg_mine)
public class MineFragment extends BaseFragment {

    private RnListAdapter rnListAdapter1;
    private RnListAdapter rnListAdapter2;
    private List<React> rnList1 = new ArrayList<>();
    private List<React> rnList2 = new ArrayList<>();
    private String name, iconurl;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.tv_id)
    private TextView openIdTxt;

    @ViewInject(R.id.tv_score)
    private TextView scoreTxt;

    @ViewInject(R.id.followCount)
    private TextView followCount;

    @ViewInject(R.id.fansCount)
    private TextView fansCount;

    @ViewInject(R.id.person_tv_star)
    private TextView person_tv_star;

    @ViewInject(R.id.person_tv_score)
    private TextView person_tv_score;

    @ViewInject(R.id.table_top_img_menu)
    private ImageView editImg;

    @ViewInject(R.id.tv_not_anchor)
    private TextView anchorTxt;

    @ViewInject(R.id.tv_persion_stagename)
    private TextView nameTxt;

    @ViewInject(R.id.iv_not_anchor)
    private ImageView anchorImg;

    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;

    @ViewInject(R.id.iv_anchor)
    private ImageView anchorIcon;

    @ViewInject(R.id.iv_persion_image)
    private ImageView personIcon;

    @ViewInject(R.id.iv_guard)
    private ImageView guardIcon;

    @ViewInject(R.id.iv_fans_club)
    private ImageView fansClubIcon;

    @ViewInject(R.id.iv_back)
    private ImageView tabBackIbt;

    @ViewInject(R.id.iv_richScore)
    private ImageView richScoreiIcon;

    @ViewInject(R.id.iv_vip)
    private ImageView vipImg;

    @ViewInject(R.id.lv_rn)
    private ListView rnListView1;

    @ViewInject(R.id.lv_rn2)
    private ListView rnListView2;

    @Event(R.id.iv_persion_image)
    private void showImage(ImageView v){
        if (null == SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl()){
            return;
        }
        Intent intent = new Intent(getActivity(), SpaceImageDetailActivity.class);
        String url = SystemCache.getPersonalMsg().getAccount().getFaceOrgUrl();
        boolean isOrg = true;
        if (null == url){
            url = StringUtil.checkIcon(SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl());
        }else {
            url = StringUtil.checkIcon(url);
            isOrg = false;
        }
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        intent.putExtra("images", url);
        intent.putExtra("isOrg", isOrg);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);
        intent.putExtra("width", v.getWidth());
        intent.putExtra("height", v.getHeight());
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Event(R.id.ll_setting)
    private void intentToSttingAct(View v){
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intentAct(getActivity(),intent);
    }

    @Event(R.id.table_top_fl_menu)
    private void intnetToMenu(View v){
        Intent intent = new Intent(getActivity(), EditPersonalActivity.class);
        intentAct(getActivity(),intent);
    }

    @Event(R.id.mine_iv_qrcode)
    private void intnetToMyQrCode(View v){
        Intent intent = new Intent(getActivity(), MyQrCodeActivity.class);
        intentAct(getActivity(),intent);
    }

    @Event({R.id.followCount, R.id.tv_attention})
    private void onAtteneionClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "FollowIndex";
        startActivity(intent);
    }

    @Event({R.id.fansCount, R.id.tv_fans})
    private void onFansClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "MyFansIndex";
        startActivity(intent);
    }

    @Event(R.id.iv_game)
    private void onGameClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "GameIndex";
        startActivity(intent);
    }

    /**
     * 获取我的个人信息
     */
    private void getMyInfo(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETMYINFO)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                PersonalMsg personalMsg = GsonInner.getGsonInstance().fromJson(js.getJSONObject("data").toString(),PersonalMsg.class);
                                SystemCache.setPersonalMsg(personalMsg);
                                if(personalMsg.getAccount().isAnchor()){
                                    anchorTxt.setVisibility(View.GONE);
                                    anchorImg.setVisibility(View.GONE);
                                    anchorIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_anchor_yse));
                                } else {
                                    anchorIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_anchor_no));
                                }
                                iconurl = StringUtil.checkIcon(personalMsg.getAccount().getFaceSmallUrl());
                                Glide.with(getActivity()).load(iconurl)
                                        .transform(new GlideCircleTransform(getActivity()))
                                        .placeholder(R.drawable.icon_mine_personal_default)
                                        .error(R.drawable.icon_mine_personal_default)
                                        .into(personIcon);
                                name = personalMsg.getAccount().getName();
                                nameTxt.setText(name);
                                openIdTxt.setText(personalMsg.getAccount().getOpenId());
                                String score = String.valueOf(personalMsg.getAccount().getScore());
                                scoreTxt.setText(score);
                                long[] scoreArray = MathUtil.checkScore(Long.valueOf(score));
                                person_tv_score.setText(String.valueOf(scoreArray[1]));
                                person_tv_star.setText(String.valueOf(scoreArray[0]));
                                setStarColor(String.valueOf(scoreArray[0]));
                                long richScore = personalMsg.getAccount().getRichScore();
                                InputStream richScoreIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
                                Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                                richScoreiIcon.setImageBitmap(richScoreBitmap);
                                int vip = personalMsg.getAccount().getVip();
                                InputStream vipIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                                Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                                vipImg.setImageBitmap(vipBitmap);
                                if(null != personalMsg.getGuard()){
                                    guardIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_person_guard_yes));
                                } else {
                                    guardIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_person_guard_no));
                                }
                                if(null != personalMsg.getClubLevel()){
                                    fansClubIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_fansclub_yes));
                                } else {
                                    fansClubIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_personal_fansclub_no));
                                }
                                followCount.setText(personalMsg.getFollowCount());
                                fansCount.setText(personalMsg.getFansCount());
                            } else {
                                LogUtil.e("获取我的个人信息数据出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        } catch (NullPointerException e){
                            LogUtil.e(e.toString());
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

    private void initRnList() {
        try {
            List<React> list = SystemCache.getRnConfig().getReact();
            for (React react: list){
                if (react.getType().equals("list") && react.getNode().equals("my")){
                    if (null != react.getSection() && react.getSection().equals("1")){
                        rnList1.add(react);
                    }else if (null != react.getSection() && react.getSection().equals("2")){
                        rnList2.add(react);
                    }
                }
            }
            rnListAdapter1 = new RnListAdapter(rnList1,getActivity(),true);
            rnListAdapter2 = new RnListAdapter(rnList2,getActivity(),true);
            rnListView1.setAdapter(rnListAdapter1);
            rnListView2.setAdapter(rnListAdapter2);
            ListViewUtil.setListViewHeightBasedOnChildren(rnListView1);
            ListViewUtil.setListViewHeightBasedOnChildren(rnListView2);
            rnListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), MyReactActivity.class);
                    intent.putExtra("accId", UserPreferences.getAccId());
                    intent.putExtra("token", UserPreferences.getToken());
                    MyReactActivity.mainName = rnList1.get(i).getId();
                    Log.i("ReactNative", rnList1.get(i).getId());
                    startActivity(intent);

                }
            });
            rnListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent;
                    if (rnList2.get(i).getId().equals("PayIndex")){
                        intent = new Intent(getActivity(), Recharge.class);
                    }else {
                        intent = new Intent(getActivity(), MyReactActivity.class);
                        intent.putExtra("accId", UserPreferences.getAccId());
                        intent.putExtra("token", UserPreferences.getToken());
                        MyReactActivity.mainName = rnList2.get(i).getId();
                        Log.i("ReactNative", rnList2.get(i).getId());
                    }
                    startActivity(intent);
                }
            });
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }
    }


    /**
     * 检查Rn配置
     */
    private void checkRn() {
        final String filePath = SystemCache.BASE_PATH + SystemCache.RNFOLDER;
        String rnVersion = SysPreferences.getRnVersion();
        if (SysPreferences.getRnConfiger().equals("-1")){
            rnVersion = "rn";
        }
        if (!FileUtils.isExist(filePath + "/index.android.bundle")){
            rnVersion = "rn";
        }
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CHECKRN)
                .addParam("version", rnVersion)
                .addParam("device" , SystemCache.DEVICE)
                .addParam("main"   , SystemCache.MAIN)
                .addParam("sub"    , SystemCache.SUB )
                .addParam("func"   , SystemCache.FUNC)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject jsonObject, boolean success) {
                        try {
                            if (jsonObject.getString("code").equals("success")) {
                                if(jsonObject.has("data")){     //RN版本有更新
                                    final JSONObject data = jsonObject.getJSONObject("data");
                                    final RNConfig rnConfig = GsonInner.getGsonInstance().fromJson(data.toString(), RNConfig.class);
                                    final String rnConfigVersion = rnConfig.getRnConfigVersion();
                                    if (rnConfig.getRnZip() != null) {
                                        String url = API.OSS_URL_RN + rnConfig.getRnZip() + ".zip";
                                        XUtilsDownLoad.downloadOneFile(url, filePath + ".zip", new XUtilsDownLoadCallback() {
                                            @Override
                                            public void success() {
                                                SysPreferences.saveRnVersion(rnConfigVersion);
                                                SysPreferences.saveRnConfiger(data.toString());
                                                SystemCache.setRnConfig(rnConfig);
                                                Toast.makeText(SystemCache.getContext(), "下载成功", Toast.LENGTH_SHORT).show();
                                                try {
                                                    File file = new File(filePath + ".zip");
                                                    FileUtils.extractZipFile(file, filePath);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    if(!"-1".equals(SysPreferences.getRnConfiger())){
                                        RNConfig rnConfig = GsonInner.getGsonInstance().fromJson(SysPreferences.getRnConfiger(), RNConfig.class);
                                        SystemCache.setRnConfig(rnConfig);
                                    }
                                }
                                initRnList();
                            } else {
                                TSBError(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.getMessage());
                        }
                    }
                });
    }

    private void init() {
        tabBackIbt.setVisibility(View.GONE);
        titleTxt.setText(R.string.mine);
        editImg.setImageDrawable(getResources().getDrawable(R.drawable.bt_tab_top_menu_write));
        if (null == SystemCache.getRnConfig()){
            checkRn();
        }else {
            initRnList();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMyInfo();
    }
}
