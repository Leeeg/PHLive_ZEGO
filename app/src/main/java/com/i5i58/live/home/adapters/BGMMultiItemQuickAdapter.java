package com.i5i58.live.home.adapters;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.i5i58.live.R;
import com.i5i58.live.home.helper.bgmHelper.Mp3Info;

import java.util.List;

/**
 * Created by Lee on 2017/5/6.
 */

public class BGMMultiItemQuickAdapter extends BaseMultiItemQuickAdapter<Mp3Info> {

    private Context context;

    public BGMMultiItemQuickAdapter(Context context, List data) {
        super(data);
        this.context = context;
        addItemType(Mp3Info.PLAY, R.layout.item_anchor_bgm);
        addItemType(Mp3Info.PLAYING, R.layout.item_anchor_bgm_playing);
        addItemType(Mp3Info.SERACH, R.layout.item_anchor_bgm_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, Mp3Info item) {
        switch (helper.getItemViewType()){
            case Mp3Info.PLAY:
            case Mp3Info.PLAYING:
                helper.setText(R.id.music_title, item.getTitle());      //显示歌名
                helper.setText(R.id.music_artist, item.getArtist());    //显示作者
                helper.setText(R.id.music_duration, String.valueOf(formatTime(item.getDuration())));    //显示时长
                helper.setOnClickListener(R.id.music_playIbt, new OnItemChildClickListener());
                break;
            case Mp3Info.SERACH:
                helper.setText(R.id.music_title, item.getTitle())
                .setOnClickListener(R.id.music_history_remove, new OnItemChildClickListener());      //显示歌名
                break;
        }
    }

    private String formatTime(Long time){                     //将歌曲的时间转换为分秒的制度
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if(min.length() < 2)
            min = "0" + min;
        switch (sec.length()){
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0,2);
    }
}
