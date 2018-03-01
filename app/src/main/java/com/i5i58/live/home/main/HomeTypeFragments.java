package com.i5i58.live.home.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.imageLoader.BannerGlideImageLoader;
import com.i5i58.live.common.utils.json.GsonUtil;
import com.i5i58.live.common.view.fragment.LazyFragment;
import com.i5i58.live.common.view.gridView.MyGrivdview;
import com.i5i58.live.home.activity.MainAudienceActivity;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.BannerData;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/17.
 */

public class HomeTypeFragments extends LazyFragment{

    private int tabIndex;
    private static final String INTENT_INT_INDEX = "index";
    private List<String> bannerList = new ArrayList<>();
    private List<BannerData> banners;
    private List<ChannelData> channels = new ArrayList<>();
    private Banner banner;
    private PullToRefreshScrollView pushScro;
    private MyGrivdview gridView;
    private TypeFrgGridAdapter moreFrgGridAdapter;
    private int pageUnm = 0;
    private int pageSize = 20;
    private int count;
    public static final String enterAction = "com.HomeTypeFragments.action";

    public static HomeTypeFragments newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        HomeTypeFragments fragment = new HomeTypeFragments();
        fragment.setArguments(args);
        return fragment;
    }

    private void registBroadcast(boolean register){
        if (register){
            IntentFilter enter = new IntentFilter(HomeTypeFragments.enterAction);
            getActivity().registerReceiver(broadcastReceiver, enter);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent enter) {
            //侧边栏跳转进入直播间
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);

                        Intent intent = new Intent(getActivity(), MainAudienceActivity.class);
                        intent.putExtra("position", 0);
                        Bundle bundle = new Bundle();
                        ChannelData channel = new ChannelData();
                        channel.setCId(enter.getStringExtra("cId"));
                        channel.setHttpPullUrl(enter.getStringExtra("pullUrl"));
                        channel.setCoverUrl(enter.getStringExtra("coverImage"));
                        channel.setYunXinRId(enter.getStringExtra("roomId"));
                        channel.setChannelId(enter.getStringExtra("channelId"));
                        LiveRoomCache.setChannelData(channel);
                        List<ChannelData> channels = new ArrayList<>();
                        channels.add(channel);
                        bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    private void initBanner() {
        banner.setVisibility(View.VISIBLE);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerGlideImageLoader());
        //设置图片集合
        banner.setImages(bannerList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /**
     * 获取首页轮播图
     */
    public void getCarousel() {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETCAROUSEL)
                .addParam("device", "3")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                banners = GsonUtil.getBannerList(js.getJSONArray("data").toString());
                                if(banners.size() > 0){
                                    bannerList.clear();
                                    for (BannerData banner: banners){
                                        bannerList.add(API.OSS_URL_CAROUSEL + banner.getImgUrl());
                                    }
                                    initBanner();
                                }else {
                                    banner.setVisibility(View.GONE);
                                }
                            } else {
                                LogUtil.e("获取轮播图数据出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    /**
     * 获取直播间列表
     */
    public void getChannelRooms(final boolean clear) {
        try {
            HomeMainFragment liveFrg = (HomeMainFragment) getActivity().getSupportFragmentManager().getFragments().get(0);
            String channelType = liveFrg.homeTypeList.get(tabIndex).getValue();
            new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.GETHOTCHANNEL)
                    .addParam("type", channelType)
                    .addParam("pageSize", String.valueOf(pageSize))
                    .addParam("pageNum", String.valueOf(pageUnm))
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals("success")) {
                                    if (clear) channels.clear();
                                    channels = GsonUtil.getChannelList(js.getJSONObject("data").getJSONArray("content").toString(),channels);
                                    moreFrgGridAdapter.notifyDataSetChanged();
                                    count = Integer.valueOf(js.getJSONObject("data").getString("count"));
                                    LogUtil.d("channels count = " + count);
                                } else {
                                    LogUtil.e("获取直播间列表数据出错");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                            }
                        }
                    });
        }catch (NullPointerException e){
            LogUtil.e(e.toString());
        }
    }

    private void fromWeb() {
        if (TigerApplication.getInstance().isFromWeb){
            Intent intent = new Intent(getActivity(), MainAudienceActivity.class);
            intent.putExtra("position", 0);
            Bundle bundle = new Bundle();
            List<ChannelData> channels = new ArrayList<>();
            channels.add(LiveRoomCache.getChannelData());
            bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void init() {
        pageUnm = 0;
        pushScro = (PullToRefreshScrollView) findViewById(R.id.type_pull_refresh_scrollview);
        pushScro.setMode(PullToRefreshBase.Mode.BOTH);
        pushScro.setPullToRefreshOverScrollEnabled(true);
        pushScro.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if(tabIndex == 0){
                    getCarousel();
                }
                pageUnm = 0;
                getChannelRooms(true);
                pushScro.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        pushScro.onRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (channels.size() < count){
                    pageUnm++;
                    getChannelRooms(false);
                }else {
                    LogUtil.d("no  more channels !");
                }
                pushScro.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        pushScro.onRefreshComplete();
                    }
                }, 1000);
            }
        });

        gridView = (MyGrivdview) findViewById(R.id.morefrg_grid);
        moreFrgGridAdapter = new TypeFrgGridAdapter(channels,getActivity());
        gridView.setAdapter(moreFrgGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MainAudienceActivity.class);
                intent.putExtra("position", i);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        banner = (Banner) findViewById(R.id.morefrg_banner);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerData bannerData = banners.get(position);
                if (null == bannerData || "0".equals(bannerData.getAction())) return;
                Intent intent = new Intent(getActivity(), MainAudienceActivity.class);
                intent.putExtra("position", 0);
                Bundle bundle = new Bundle();
                ChannelData channel = new ChannelData();
                channel.setCId(bannerData.getcId());
                channel.setHttpPullUrl(bannerData.getHttpPullUrl());
                channel.setCoverUrl(bannerData.getCoverUrl());
                channel.setYunXinRId(bannerData.getYunXinRId());
                LiveRoomCache.setChannelData(channel);
                List<ChannelData> channels = new ArrayList<>();
                channels.add(channel);
                bundle.putParcelableArrayList("channels", (ArrayList<? extends Parcelable>) channels);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if (tabIndex == 0){
            getCarousel();
        }else {
            banner.setVisibility(View.GONE);
        }

        getChannelRooms(true);

        fromWeb();
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        if (null != banner){
            banner.startAutoPlay();
        }
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        if (null != banner){
            banner.stopAutoPlay();
        }
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.frg_home_main_more);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        init();
//        registBroadcast(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        registBroadcast(false);
    }
}
