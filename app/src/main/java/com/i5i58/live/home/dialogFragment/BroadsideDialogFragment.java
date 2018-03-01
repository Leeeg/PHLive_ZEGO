package com.i5i58.live.home.dialogFragment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.system.BrightnessTools;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.common.view.swipeitem.recyclerview.SwipeItemLayout;
import com.i5i58.live.home.adapters.BroadSideQuickAdapter;
import com.i5i58.live.home.entity.BroadSideQuickItem;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.liveRoom.BroadSideAction;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.reactNative.MyReactActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/27.
 */

public class BroadsideDialogFragment extends BaseDialogFragment {

//    private static BroadsideDialogFragment broadDialogFragment;

    private View colse_broad;
    private static BroadCallback broadCallback;
    private LinearLayout broad_root;
    private TextView notifyCountTxt;
    private RecyclerView recyclerView;
    private BroadSideQuickAdapter broadSideQuickAdapter;
    private List<BroadSideQuickItem> mChatData = new ArrayList<>();
    private SeekBar seekBar;
    private int mProgess = 10;
    private final int REQUEST_CODE = 100;
    private List<BroadSideAction> broadSideActions = new ArrayList<>();
    private ImageView pushImg;
    private boolean pushAble;
    private LinearLayout pushLayout;
    private int pushCount;

    public interface BroadCallback{
        void onDissmise();
        void onChencins();
        void onSingleChat();
        void onClean();
        void enterChannel(String cId, String pullUrl, String coverImage, String roomId, String channelId);
    }

//    public static BroadsideDialogFragment newInstance(BroadCallback broadCallback) {
//        if (null == broadDialogFragment) {
//            this.broadCallback = broadCallback;
//            broadDialogFragment = new BroadsideDialogFragment(broadCallback);
//        }
//        return broadDialogFragment;
//    }


    public BroadsideDialogFragment() {
    }

    public BroadsideDialogFragment(BroadCallback broadCallback) {
        this.broadCallback = broadCallback;
    }

    private void setNoticeLive(final boolean notifyAble){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.NOTIFYLIVE)
                .addParam("cId", LiveRoomCache.getChannelId())
                .addParam("isNotify",String.valueOf(notifyAble))
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                pushAble = notifyAble;
                                if (notifyAble){
                                    pushImg.setImageResource(R.drawable.icon_live_push_open);
                                    pushCount++;
                                    notifyCountTxt.setText(String.valueOf(pushCount));
                                }else {
                                    pushImg.setImageResource(R.drawable.icon_live_push_colse);
                                    pushCount--;
                                    notifyCountTxt.setText(String.valueOf(pushCount));
                                }
                                pushImg.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    private void getNoticeLiveStatus(){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETNOTIFYABLE)
                .addParam("cId", LiveRoomCache.getChannelId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                LogUtil.d(js.toString());
                                pushAble = js.getJSONObject("data").getBoolean("isNotice");
                                if (pushAble){
                                    pushImg.setImageResource(R.drawable.icon_live_push_open);
                                }else {
                                    pushImg.setImageResource(R.drawable.icon_live_push_colse);
                                }
                                pushImg.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    private void getNotifyCount() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETNOTIFYCOUNT)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (null != js.getString("data")){
                                    pushCount = Integer.valueOf(js.getString("data"));
                                    notifyCountTxt.setText(js.getString("data"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            LogUtil.e(e.toString());
                        }
                    }
                });

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
        View view = inflater.inflate(R.layout.dfrg_broadside, null);
        colse_broad = view.findViewById(R.id.colse_broad);
        colse_broad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return true;
            }
        });

        broad_root = (LinearLayout) view.findViewById(R.id.broad_root);
        broad_root.getBackground().setAlpha(204);

        notifyCountTxt = (TextView) view.findViewById(R.id.broad_txt_notifycount);
        pushImg = (ImageView) view.findViewById(R.id.push_live);
        pushLayout = (LinearLayout) view.findViewById(R.id.push_live_layout);
        pushImg.setEnabled(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.broad_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        broadSideQuickAdapter = new BroadSideQuickAdapter(getActivity(), mChatData);
        recyclerView.setAdapter(broadSideQuickAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, getResources().getColor(R.color.transparent)));
        broadSideQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int i) {
                LogUtil.d("broadCase item click!" + i);
                new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.GETCHANNELINFO)
                        .addParam("cId", LiveRoomCache.getChannelId())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                        if (broadSideActions.get(i).getTarget().equals("enter_channel")){
                                            ChannelData channelData = GsonInner.getGsonInstance().fromJson(js.toString(), ChannelData.class);
                                            broadCallback.enterChannel(channelData.getCId(), channelData.getHttpPullUrl(), channelData.getCoverUrl()
                                            ,channelData.getYunXinRId(), channelData.getChannelId());
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

        seekBar = (SeekBar) view.findViewById(R.id.light_seekBar);
        seekBar.setProgress(BrightnessTools.getScreenBrightness(getActivity()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                BrightnessTools.setBrightness(getActivity(), i);
                mProgess = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (BrightnessTools.isAutoBrightness(getActivity().getContentResolver())){
                    TSBError("请先关闭自动亮度调节!");
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }else {
                    BrightnessTools.saveBrightness(getActivity().getContentResolver(), mProgess);
                }
            }
        });

        getNotifyCount();
        getNoticeLiveStatus();

        return view;
    }

    //6.0以上才能调用
    @TargetApi(23)
    void checkPermission(){
        if(!Settings.System.canWrite(getActivity())){
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        }else {
            BrightnessTools.saveBrightness(getActivity().getContentResolver(), mProgess);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            checkPermission();
        }
    }

    @Event(R.id.ll_checkins)
    private void onChencinsClick(View v){
        dismiss();
        broadCallback.onChencins();
    }

    @Event(R.id.ll_singlechat)
    private void onSingleChatClick(View v){
        dismiss();
        broadCallback.onSingleChat();
    }

    @Event(R.id.ll_clear)
    private void onCleanClick(View v){
        dismiss();
        broadCallback.onClean();
    }

    @Event(R.id.push_live)
    private void onPushAbleClick(View v){
        pushImg.setEnabled(false);
        setNoticeLive(!pushAble);
    }

//    @Event(R.id.push_live_layout)
//    private void onPushAbleLayoutClick(View v){
//        Intent intent = new Intent(getActivity(), MyReactActivity.class);
//        intent.putExtra("accId", UserPreferences.getAccId());
//        intent.putExtra("token", UserPreferences.getToken());
//        intent.putExtra("cId", LiveRoomCache.getChannelId());
//        MyReactActivity.mainName = "LiveNoticeIndex";
//        startActivity(intent);
//    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("BroadsideDialogFragment  start");
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.RIGHT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.dimAmount = 0.0f;
        params.windowAnimations = R.style.PopRight;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        broadCallback.onDissmise();
        LogUtil.d("BroadsideDialogFragment  onDismiss");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("BroadsideDialogFragment  onDestroy");
    }
}
