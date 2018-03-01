package com.i5i58.live.home.dialogFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.i5i58.live.BuildConfig;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.listViewUtil.ListViewUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.fragment.GridFragment;
import com.i5i58.live.home.adapters.GiftCountAdapter;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.GiftModel.GiftModel;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.reactNative.MyReactActivity;
import com.i5i58.live.recharge.Recharge;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/4/27.
 */

public class GiftDialogFragment extends BaseDialogFragment implements GridFragment.GridCallback{

//    private static GiftDialogFragment dialogFragment;
    private BaseFragmentAdapter pageAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<GiftModel> modelList = new ArrayList<>();
    private int pageCount;
    private int pageNum = 0;
    private List<ImageView> dots;//底部小圆点的集合
    private LinearLayout ll_point;
    private LinearLayout bgLl1;
    private boolean isOtherNumber = false;
    private boolean isNumberValid = false;
    private LinearLayout number_other;
    private TextView numberChoseTxt;
    private EditText numberEdit;
    private FrameLayout bgLl2;
    private ViewPager pageGridView;
    private GiftCallback giftCallback;
    private String gCount = "1";
    private String gId;
    private LinearLayout ll_box, ll_gift;
    private TextView tv_gift, tv_parcel, tv_recharge_wallet, gift_tv_recharge, countTxt, sendTxt;
    private ImageView iv_parcel, gift_iv_recharge, iv_gift;
    private LinearLayout ll_send, ll_wallet, rl_box;
    private FrameLayout myView;
    private ListView countListview;
    private GiftCountAdapter giftCountAdapter;
    private List<Map> list = new ArrayList<>();
    private Map map = new HashMap();
    private Map<Integer, GiftConfig> giftConfigMap;
    private ImageView iv_bg_car;
    private InputMethodManager manager;
    private boolean continueAble;
    //礼物券
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;


    @Event(R.id.tv_yes)
    private void choseNumberClick(View view){
        if (isNumberValid){
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            number_other.setVisibility(View.GONE);
            bgLl1.setVisibility(View.VISIBLE);
            bgLl2.setVisibility(View.VISIBLE);
            isOtherNumber = false;
        }
    }

    @Event(R.id.tv_gift_send)
    private void sendGift(View view){
        if (sendTxt.isEnabled()){
            giftCallback.giftSend(gId, gCount, continueAble);
            dismiss();
        }else {
            TSBError("请选择礼物！");
        }
    }

    @Event(R.id.close_dfrg_gift)
    private void closeClick(View view){
        if (isOtherNumber){
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            number_other.setVisibility(View.GONE);
            bgLl1.setVisibility(View.VISIBLE);
            bgLl2.setVisibility(View.VISIBLE);
            isOtherNumber = false;
        }else {
            dismiss();
        }
    }

    @Event(R.id.gift_num)
    private void countClick(View view){
        if (countListview.getVisibility() == View.VISIBLE){
            countListview.setVisibility(View.GONE);
        }else {
            countListview.setVisibility(View.VISIBLE);
        }
    }

    @Event(R.id.gift_tv_recharge)
    private void rechargeClick(View view){
        Intent intent = new Intent(getActivity(), Recharge.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Event(R.id.ll_box)
    private void boxClick(View view){
        ll_gift.setEnabled(true);
        ll_box.setEnabled(false);
        ll_box.setBackground(getResources().getDrawable(R.drawable.line_l_r));
        ll_gift.setBackground(getResources().getDrawable(R.drawable.line_top));
        tv_gift.setTextColor(getResources().getColor(R.color.white));
        tv_parcel.setTextColor(getResources().getColor(R.color.green_words));
        iv_gift.setBackground(getResources().getDrawable(R.drawable.bg_dfrg_gift_unchosed));
        iv_parcel.setBackground(getResources().getDrawable(R.drawable.bg_dfrg_gift_pakge_chosed));
        rl_box.setVisibility(View.VISIBLE);
        myView.setVisibility(View.GONE);
        ll_send.setVisibility(View.GONE);
        ll_wallet.setVisibility(View.VISIBLE);
        gift_tv_recharge.setVisibility(View.GONE);
        tv_recharge_wallet.setVisibility(View.VISIBLE);
        gift_iv_recharge.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Event(R.id.ll_gift)
    private void giftClick(View view){
        ll_gift.setEnabled(false);
        ll_box.setEnabled(true);
        ll_box.setBackground(getResources().getDrawable(R.drawable.line_top));
        ll_gift.setBackground(getResources().getDrawable(R.drawable.line_right));
        tv_parcel.setTextColor(getResources().getColor(R.color.white));
        tv_gift.setTextColor(getResources().getColor(R.color.green_words));
        iv_gift.setBackground(getResources().getDrawable(R.drawable.bg_dfrg_gift_gift_chosed));
        iv_parcel.setBackground(getResources().getDrawable(R.drawable.bg_dfrg_gift_pakge_unchosed));
        rl_box.setVisibility(View.GONE);
        ll_send.setVisibility(View.VISIBLE);
        myView.setVisibility(View.VISIBLE);
        gift_tv_recharge.setVisibility(View.VISIBLE);
        tv_recharge_wallet.setVisibility(View.GONE);
        gift_iv_recharge.setVisibility(View.GONE);
    }

    @Event(R.id.iv_bg_car)
    private void onCarClick(View view){
        if(LiveRoomCache.isMountOpen()){
            iv_bg_car.setImageResource(R.drawable.bt_gift_car_close);
            LiveRoomCache.setIsMountOpen(false);
        }else {
            iv_bg_car.setImageResource(R.drawable.bt_gift_car_open);
            LiveRoomCache.setIsMountOpen(true);
        }
    }

    private void changeCarBt(){
        if(LiveRoomCache.isMountOpen()){
            iv_bg_car.setImageResource(R.drawable.bt_gift_car_open);
        }else {
            iv_bg_car.setImageResource(R.drawable.bt_gift_car_close);
        }
    }

    @Event(R.id.iv_bg_mount)
    private void onMountClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("cId", LiveRoomCache.getChannelId());
        MyReactActivity.mainName = "MountLiveIndex";
        startActivity(intent);
    }

    @Event(R.id.iv_bg_vip)
    private void onVipClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("cId", LiveRoomCache.getChannelId());
        MyReactActivity.mainName = "VipIndex";
        startActivity(intent);
    }

    private void getWallet() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETWALLET)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                String iGold = js.getJSONObject("data").getString("iGold");
                                if (null == iGold) return;
                                double iGoldF = Double.valueOf(iGold) / 100;
                                iGold = String.valueOf(iGoldF);
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith(".")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
//                                }
                                tv_recharge_wallet.setText(iGold);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Event(R.id.iv_bg_guard)
    private void onGuardClick(View view){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        intent.putExtra("cId", LiveRoomCache.getChannelId());
        MyReactActivity.mainName = "GuardIndex";
        startActivity(intent);
    }

    public interface GiftCallback{
        void giftSend(String gId, String gCount, boolean continueAble);
    }

    public GiftDialogFragment() {

    }

    public GiftDialogFragment(GiftCallback giftCallback) {
        this.giftCallback = giftCallback;
    }

    private void notifyListview(int maxCount) {
        list.clear();
        map = new HashMap();
        map.put("words", "其他数量");
        list.add(map);
        switch (maxCount) {
            case 1314:
                map = new HashMap();
                map.put("count", "1314");
                map.put("words", "一生一世");
                list.add(map);
            case 520:
                map = new HashMap();
                map.put("count", "520");
                map.put("words", "我爱你");
                list.add(map);
            case 188:
                map = new HashMap();
                map.put("count", "188");
                map.put("words", "要抱抱");
                list.add(map);
            case 66:
                map = new HashMap();
                map.put("count", "66");
                map.put("words", "一切顺利");
                list.add(map);
            case 30:
                map = new HashMap();
                map.put("count", "30");
                map.put("words", "想你...");
                list.add(map);
            case 10:
                map = new HashMap();
                map.put("count", "10");
                map.put("words", "十全十美");
                list.add(map);
            case 1:
                map = new HashMap();
                map.put("count", "1");
                map.put("words", "一心一意");
                list.add(map);
            default:
                break;
        }
        giftCountAdapter.notifyDataSetChanged();
        ListViewUtil.setListViewHeightBasedOnChildren2(countListview, 50);
    }

    /**
     * 实例化礼物列表下面的点
     */
    private void initDots() {
        dots = new ArrayList<>();//底部圆点集合的初始化
        for (int i = 0; i < pageCount; i++) {//根据界面数量动态添加圆点
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));//设置ImageView的宽度和高度
            imageView.setPadding(3, 3, 3, 3);//设置圆点的Padding，与周围的距离
            imageView.setImageResource(R.drawable.dot_normal);//设置图片
            dots.add(imageView);//将该图片添加到圆点集合中
            ll_point.addView(imageView);//将图片添加到LinearLayout中
        }
        dots.get(pageNum).setImageResource(R.drawable.dot_focused);
    }

    private void initPageGridView() {
        fragmentList.clear();
        modelList.clear();
        giftConfigMap = LiveRoomCache.getGiftConfigMap();
        int i = 0;
        for (Map.Entry<Integer, GiftConfig> giftConfigEntry : giftConfigMap.entrySet()) {
            GiftConfig gf = giftConfigEntry.getValue();
            if (null != gf) {
                i++;
                String guard = "0";
                if (gf.isForGuard()) {
                    guard = "1";
                }
                GiftModel model = new GiftModel(i + "", gf.getName(), String.valueOf(gf.getPrice()),
                        gf.getPath(), String.valueOf(gf.getId()), guard);
                modelList.add(model);
            }
        }
        int size = modelList.size();
        if (size % 8 == 0) {
            pageCount = size / 8;
        } else {
            pageCount = size / 8 + 1;
        }
        for (int j = 0; j < pageCount; j++) {
            GridFragment gf = GridFragment.newInstance(j, modelList, this);
            fragmentList.add(gf);
        }
        pageAdapter = new BaseFragmentAdapter(getChildFragmentManager(), fragmentList);
        pageGridView.setAdapter(pageAdapter);
        pageGridView.setCurrentItem(pageNum);
        pageGridView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < pageCount; i++) {
                    //将所有的圆点设置为为选中时候的图片
                    dots.get(i).setImageResource(R.drawable.dot_normal);
                }
                //将被选中的图片中的圆点设置为被选中的时候的图片
                dots.get(arg0).setImageResource(R.drawable.dot_focused);
                pageNum = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initDots();
    }

    private void init(View view) {

        bgLl1               = (LinearLayout) view.findViewById(R.id.dfrg_ll_bg);
        bgLl2               = (FrameLayout) view.findViewById(R.id.dfrg_fl_bg);
        pageGridView        = (ViewPager) view.findViewById(R.id.dfrg_gift_gift_viewpager);
        number_other            = (LinearLayout) view.findViewById(R.id.number_other);
        ll_point            = (LinearLayout) view.findViewById(R.id.ll_point);
        ll_box              = (LinearLayout) view.findViewById(R.id.ll_box);
        ll_gift             = (LinearLayout) view.findViewById(R.id.ll_gift);
        ll_gift.setEnabled(false);
        iv_parcel           = (ImageView) view.findViewById(R.id.iv_parcel);
        tv_gift             = (TextView) view.findViewById(R.id.tv_gift);
        tv_parcel           = (TextView) view.findViewById(R.id.tv_parcel);
        ll_gift             = (LinearLayout) view.findViewById(R.id.ll_gift);
        rl_box              = (LinearLayout) view.findViewById(R.id.rl_box);
        myView              = (FrameLayout) view.findViewById(R.id.myView);
        ll_send             = (LinearLayout) view.findViewById(R.id.ll_send);
        ll_send.getBackground().setAlpha(230);
        gift_iv_recharge    = (ImageView) view.findViewById(R.id.gift_iv_recharge);
        gift_tv_recharge    = (TextView) view.findViewById(R.id.gift_tv_recharge);
        tv_recharge_wallet  = (TextView) view.findViewById(R.id.tv_recharge_wallet);
        iv_gift             = (ImageView) view.findViewById(R.id.iv_gift);
        countListview       = (ListView) view.findViewById(R.id.lv_gift_conut);
        ll_wallet           = (LinearLayout) view.findViewById(R.id.ll_wallet);
        countTxt            = (TextView) view.findViewById(R.id.gift_num);
        sendTxt             = (TextView) view.findViewById(R.id.tv_gift_send);
        iv_bg_car           = (ImageView) view.findViewById(R.id.iv_bg_car);
        numberChoseTxt      = (TextView) view.findViewById(R.id.tv_yes);
        numberEdit          = (EditText) view.findViewById(R.id.et_number);
        countTxt.setEnabled(false);
        sendTxt.setEnabled(false);

        manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        giftCountAdapter    = new GiftCountAdapter(getActivity(), list);
        countListview.setAdapter(giftCountAdapter);
        countListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){//其他数量
                    bgLl1.setVisibility(View.GONE);
                    bgLl2.setVisibility(View.GONE);
                    countListview.setVisibility(View.GONE);
                    number_other.setVisibility(View.VISIBLE);
                    manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    isOtherNumber = true;
                }else {
                    gCount = list.get(i).get("count").toString();
                    countTxt.setText(list.get(i).get("count").toString());
                    countListview.setVisibility(View.GONE);
                }
            }
        });

        numberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != editable && !"".equals(editable.toString())){
                    if (StringUtil.isNumeric(editable.toString())) {
                        long number = Long.valueOf(editable.toString());
                        if (number > 1314){
                            number = 1314;
                            numberEdit.setText("1314");
                        }
                        if (number > 0){
                            isNumberValid = true;
                            gCount = number + "";
                            countTxt.setText(number + "");
                        }
                    }else {
                        TSBError("输入有误！");
                    }
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
        View view = inflater.inflate(R.layout.dfrg_gift, null);
        init(view);
        changeCarBt();
        initPageGridView();
        getWallet();
        //礼物券
        String jsBundleFile = Environment.getExternalStorageDirectory() + "/PHLive/React/index.android.bundle";
        mReactRootView = new ReactRootView(TigerApplication.getInstance());
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(TigerApplication.getInstance())
                .setJSBundleFile(jsBundleFile)
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        Bundle bundle = new Bundle();
        bundle.putString("accId",UserPreferences.getAccId());
        bundle.putString("token",UserPreferences.getToken());
        mReactRootView.startReactApplication(mReactInstanceManager, "PackageIndex", bundle);
        rl_box.addView(mReactRootView);
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

        bgLl1.getBackground().setAlpha(210);
        bgLl2.getBackground().setAlpha(210);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void sendGift(String gId) {
        this.gId = gId;
        GiftConfig giftConfig = giftConfigMap.get(Integer.valueOf(gId));
        if (null != giftConfig && 1 < giftConfig.getMaxCount()){
            countTxt.setBackground(getResources().getDrawable(R.drawable.board_nomal));
            countTxt.setEnabled(true);
            notifyListview(giftConfig.getMaxCount());
            continueAble = true;
        }else {
            countTxt.setBackground(getResources().getDrawable(R.drawable.bg_dfrg_gift_board_innomal));
            countTxt.setEnabled(false);
            continueAble = false;
        }
        sendTxt.setEnabled(true);
    }
}
