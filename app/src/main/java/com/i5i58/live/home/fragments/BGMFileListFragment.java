package com.i5i58.live.home.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.common.base.BaseFragment;
import com.i5i58.live.common.db.Mp3;
import com.i5i58.live.common.view.recyclerView.SpacesItemDecoration;
import com.i5i58.live.common.view.swipeitem.recyclerview.SwipeItemLayout;
import com.i5i58.live.home.adapters.BGMMultiItemQuickAdapter;
import com.i5i58.live.home.helper.bgmHelper.FindSongs;
import com.i5i58.live.home.helper.bgmHelper.Mp3Info;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/5/3.
 */

@ContentView(R.layout.dfrg_bgm_filelist)
public class BGMFileListFragment extends BaseFragment {

    private BGMMultiItemQuickAdapter bgmQuickAdapter;
    private boolean isHistory = false;
    private int playIndex = -1;
    private FindSongs findSongs;
    private List<Mp3Info> mp3Infos = new ArrayList<>();
    private List<Mp3Info> cachList = new ArrayList<>();

    @ViewInject(R.id.broad_recycle)
    private RecyclerView recyclerView;

    @ViewInject(R.id.view_music_serach)
    private LinearLayout serachView;

    @ViewInject(R.id.bt_music_serach)
    private Button serachBt;

    @ViewInject(R.id.edit_music_serach)
    private EditText serachEdit;

    @Event(R.id.view_music_serach)
    private void onSerachViewClick(View view){
        isHistory = true;
        serachView.setVisibility(View.GONE);
        serachEdit.setHint("搜索本地音乐、歌手");
        selectAllMp3();
    }

    @Event(R.id.bt_music_serach)
    private void onCancelBtClick(View view){
//        if (isHistory){
//            serachView.setVisibility(View.VISIBLE);
//            serachEdit.setHint("");
//            serachEdit.setText("");
//            isHistory = !isHistory;
//            initRecycle();
//        }else {
//
//        }
    }


    private void initRecycle() {
        mp3Infos.clear();
        cachList.clear();
        mp3Infos = findSongs.getMp3Infos(getActivity().getContentResolver(), mp3Infos);
        if (playIndex > -1){
            mp3Infos.get(playIndex).setItemType(Mp3Info.PLAYING);
        }
        bgmQuickAdapter.notifyDataSetChanged();

        cachList.addAll(mp3Infos);

    }

    /**
     * 查询全部历史数据
     */
    private void selectAllMp3(){
        try {
            mp3Infos.clear();
            List<Mp3> mp3List = TigerApplication.getInstance().getDbManager().findAll(Mp3.class);
            if (null != mp3List && mp3List.size() > 0){
                for (Mp3 mp3: mp3List) {
                    Mp3Info mp3Info = new Mp3Info();
                    mp3Info.setItemType(Mp3Info.SERACH);
                    mp3Info.setTitle(mp3.getTitle());
                    mp3Infos.add(mp3Info);
                }
            }
            bgmQuickAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("查询全部历史数据出错");
        }
    }

    /**
     * 删除
     */
    private void deleteMp3(String title) {
        try {
            Mp3 mp3 = TigerApplication.getInstance().getDbManager().findById(Mp3.class, title);
            TigerApplication.getInstance().getDbManager().delete(mp3);
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("删除用户登录信息失败");
        }
    }

    /**
     * 保存查询记录
     */
    private void saveMp3(String title) {
        try {
            Mp3 mp3 = TigerApplication.getInstance().getDbManager().findById(Mp3.class, title);
            if(null == mp3){
                Mp3 mp3New = new Mp3();
                mp3New.setTitle(title);
                TigerApplication.getInstance().getDbManager().save(mp3New);
            }else{
                mp3.setTitle(title);
                TigerApplication.getInstance().getDbManager().update(mp3);
            }
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("保存失败");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serachEdit.setCursorVisible(false);
        findSongs = new FindSongs();

        serachEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN ) {
                    if (!"".equals(serachEdit.getText()))
                        saveMp3(serachEdit.getText().toString());
                }
                return false;
            }
        });
        serachEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null == editable || "".equals(editable.toString())){
                    selectAllMp3();
                }
                if (isHistory && cachList.size() > 0){
                    mp3Infos.clear();
                    for (int i = 0; i < cachList.size(); i++){
                        Mp3Info mp3Info = cachList.get(i);
                        String title = mp3Info.getTitle();
                        String art = mp3Info.getArtist();
                        if (title.indexOf(editable.toString()) != -1 || art.indexOf(editable.toString()) != -1){
                            mp3Infos.add(mp3Info);
                        }
                    }
                    bgmQuickAdapter.notifyDataSetChanged();
                }
            }
        });

        bgmQuickAdapter = new BGMMultiItemQuickAdapter(getActivity(), mp3Infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        recyclerView.setAdapter(bgmQuickAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, getResources().getColor(R.color.transparent)));
        bgmQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (isHistory){
                    serachEdit.setText(mp3Infos.get(i).getTitle());
                }else {

                }
            }
        });
        bgmQuickAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()){
                    case R.id.music_root:
                        InputMethodManager manager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (manager != null) {
                            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                    case R.id.music_history_remove:
                        deleteMp3(mp3Infos.get(i).getTitle());
                        baseQuickAdapter.remove(i);
                        break;
                    case R.id.music_playIbt:
                        if (playIndex > -1){
                            mp3Infos.get(playIndex).setItemType(Mp3Info.PLAY);
                            bgmQuickAdapter.notifyItemChanged(playIndex);
                        }
                        mp3Infos.get(i).setItemType(Mp3Info.PLAYING);
                        bgmQuickAdapter.notifyItemChanged(i);
                        break;
                }
            }
        });

        initRecycle();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serachEdit.setText("");
    }

}
