package com.i5i58.live.home.dialogFragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.base.BaseFragmentAdapter;
import com.i5i58.live.common.view.viewPager.NoHorizontalScrollViewpager;
import com.i5i58.live.home.fragments.BGMFileListFragment;
import com.i5i58.live.home.fragments.BGMMusicListFragment;
import com.i5i58.live.home.helper.bgmHelper.Mp3Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/4/27.
 */

public class MusicListDialogFragment extends BaseDialogFragment {

    private static MusicListDialogFragment musicDialogFragment;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    private TabLayout tabLayout;
    private NoHorizontalScrollViewpager viewpager;
    private BaseFragmentAdapter baseAdapter;
    private FrameLayout backLayout;
    private TextView titleTxt;

    private OperateListener operateListener;

    public void setOperateListener(OperateListener operateListener) {
        this.operateListener = operateListener;
    }

    public interface OperateListener {

    }

    public MusicListDialogFragment() {
    }

    public static MusicListDialogFragment getInstance() {
        if (null == musicDialogFragment)musicDialogFragment = new MusicListDialogFragment();
        return musicDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleList.add("我的歌单");
        titleList.add("我的目录");
        fragmentList.add(new BGMMusicListFragment());
        fragmentList.add(new BGMFileListFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dfrg_bgm, null);
        backLayout = (FrameLayout) view.findViewById(R.id.table_top_fl_back);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        titleTxt = (TextView) view.findViewById(R.id.table_top_tv_title);
        titleTxt.setText("音乐");
        TextPaint textPaint = titleTxt.getPaint();
        textPaint.setFakeBoldText(true);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_bgm);
        tabLayout.addTab(tabLayout.newTab().setText("我的歌单"));
        tabLayout.addTab(tabLayout.newTab().setText("我的目录"));
        viewpager = (NoHorizontalScrollViewpager) view.findViewById(R.id.viewpager_bgm);
        baseAdapter = new BaseFragmentAdapter(getChildFragmentManager(), fragmentList, titleList);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabsFromPagerAdapter(baseAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(baseAdapter);
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
        params.windowAnimations = R.style.PopRight;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));//设置背景
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
