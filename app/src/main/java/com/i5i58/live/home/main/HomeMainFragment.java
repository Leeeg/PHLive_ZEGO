package com.i5i58.live.home.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.json.GsonUtil;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.HomeTypeData;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/14.
 */

@ContentView(R.layout.frg_liveroom)
public class HomeMainFragment extends BaseFragment{

    public List<HomeTypeData> homeTypeList;
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private BaseFragmentAdapter baseAdapter;

    @ViewInject(R.id.liveroom_table_tab)
    private TabLayout liveroomTab;

    @ViewInject(R.id.liveroom_pager)
    private ViewPager liveroomPager;

    @Event(R.id.liveroom_table_top_ibt_serach)
    private void onSearchClick(ImageButton v){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "SerIndex";
        startActivity(intent);
    }

    @Event(R.id.liveroom_table_top_ibt_game)
    private void onGameClick(ImageButton v){
        Intent intent = new Intent(getActivity(), MyReactActivity.class);
        intent.putExtra("accId", UserPreferences.getAccId());
        intent.putExtra("token", UserPreferences.getToken());
        MyReactActivity.mainName = "GameIndex";
        startActivity(intent);
    }

    private void getPlatformConfig(){
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETPLATFROMCONFIG)
                .addParam("device" , SystemCache.DEVICE)
                .addParam("main"   , SystemCache.MAIN)
                .addParam("sub"    , SystemCache.SUB )
                .addParam("func"   , SystemCache.FUNC)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                String shareUrl = js.getJSONObject("data").getString("shareUrl");
                                SystemCache.setShareUrl(shareUrl);
                                homeTypeList = GsonUtil.getHomeTypeList(js.getJSONObject("data").getJSONArray("homeType").toString());
                                initViewPgaer();
                            } else {
                                LogUtil.e("获取HomeType出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    private void initViewPgaer() {
        for (int i = 0;i < homeTypeList.size();i++){
            titleList.add(homeTypeList.get(i).getName());
            fragmentList.add(HomeTypeFragments.newInstance(i, true));
            liveroomTab.addTab(liveroomTab.newTab().setText(homeTypeList.get(i).getName()));
        }
        baseAdapter = new BaseFragmentAdapter(getChildFragmentManager(), fragmentList, titleList);
        liveroomPager.setOffscreenPageLimit(homeTypeList.size());
        liveroomPager.setAdapter(baseAdapter);
        liveroomTab.setupWithViewPager(liveroomPager);
        liveroomTab.setTabsFromPagerAdapter(baseAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPlatformConfig();
    }

}
