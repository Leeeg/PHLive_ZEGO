package com.i5i58.live.home.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.home.fragments.MicFragments;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 麦序Activity
 */
@ContentView(R.layout.act_macactivity)
public class MicActivity extends BaseActivity{

	private List<String> titleList = new ArrayList<>();

	@ViewInject(R.id.vp)
	private ViewPager vp;

	@ViewInject(R.id.liveroom_table_tab)
	private TabLayout liveroomTab;

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyStatusBarUtil.StatusBarLightMode(this);

		initView();
	}

	private void initView() {
		titleList.add("麦序");
		titleList.add("老虎席");
		titleList.add("观众");

		List<Fragment> fragments = new ArrayList<>();
		for (int i = 0; i < 3; i++){
			liveroomTab.addTab(liveroomTab.newTab().setText(titleList.get(i)));
			fragments.add(MicFragments.newInstance(i, true));
		}
		BaseFragmentAdapter pagerAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments, titleList);
		vp.setOffscreenPageLimit(fragments.size());
		vp.setAdapter(pagerAdapter);
		liveroomTab.setupWithViewPager(vp);
		liveroomTab.setTabsFromPagerAdapter(pagerAdapter);
	}

}
