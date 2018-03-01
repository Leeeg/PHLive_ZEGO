package com.i5i58.live.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.listViewUtil.ListViewUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.login.activity.LoginMainActivity;
import com.i5i58.live.mine.adapter.RnListAdapter;
import com.i5i58.live.model.entity.rnConfig.React;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/19.
 */

@ContentView(R.layout.act_mine_setting)
public class SettingActivity extends BaseActivity {

    private RnListAdapter rnListAdapter;
    private List<React> rnList = new ArrayList<>();

    @ViewInject(R.id.setting_lv_rn)
    private ListView rnListView;

    @Event(R.id.bt_logout)
    private void logOut(Button b){
        UserPreferences.saveLogin(false);
        appManager.SaveLastActivity();
//        SystemCache.clear();
        Intent intent = new Intent(SettingActivity.this, LoginMainActivity.class);
        intentAct(this,intent);
        closeAct(this);
    }

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

    @Event(R.id.ll_prerogative)
    private void onCleanCachClick(View view){
        TSBSuccess("已清理");
    }

    private void init() {
        List<React> list = SystemCache.getRnConfig().getReact();
        for (React react: list){
            if (react.getType().equals("list") && react.getNode().equals("setting") && !react.getId().equals("ScoreIndex")){
                rnList.add(react);
            }
        }
        rnListAdapter = new RnListAdapter(rnList,this);
        rnListView.setAdapter(rnListAdapter);
        ListViewUtil.setListViewHeightBasedOnChildren(rnListView);
        rnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SettingActivity.this, MyReactActivity.class);
                intent.putExtra("accId", UserPreferences.getAccId());
                intent.putExtra("token", UserPreferences.getToken());
                MyReactActivity.mainName = rnList.get(i).getId();
                Log.i("ReactNative", rnList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        init();
    }

}
