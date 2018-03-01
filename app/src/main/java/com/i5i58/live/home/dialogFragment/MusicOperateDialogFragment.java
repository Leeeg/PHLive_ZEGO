package com.i5i58.live.home.dialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseDialogFragment;
import com.i5i58.live.common.enums.BGMEnum;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

/**
 * Created by Lee on 2017/4/27.
 */

public class MusicOperateDialogFragment extends BaseDialogFragment {

    private ImageView musicList;
    private LinearLayout rootLayout;
    private SeekBar bgm_seekBar;
    private String musicName;//伴奏名字
    private String musicSingler;//伴奏演唱者
    private TextView bgmTitleTxt;
    private BGMEnum bgmType = BGMEnum.STOP;
    private ImageView playImb;
    private int audio;

    @Event(R.id.bgm_operate_musiclist)
    private void onMusicListClick(View view){
        bgmCallback.musicList();
        dismiss();
    }

    @Event(R.id.bgm_operate_musicPause)
    private void onPasueClick(View view){
        if (bgmType == BGMEnum.PLAY){
//            AVChatManager.getInstance().stopAudioMixing();
            playImb.setImageResource(R.drawable.icon_bgm_operate_musicpause);
            bgmType = BGMEnum.PAUSE;
        }else if (bgmType == BGMEnum.PAUSE){
            playImb.setImageResource(R.drawable.icon_bgm_operate_musicplay);
            bgmType = BGMEnum.PLAY;
        }
        bgmCallback.type(bgmType);
    }

    private BGMCallback bgmCallback;

    public interface BGMCallback{
        void onDismiss();
        void musicList();
        void audio(int audio);
        void type(BGMEnum type);
    }

    public MusicOperateDialogFragment(int audio, BGMEnum bgmType, String musicName, String musicSingler, BGMCallback bgmCallback) {
        this.audio = audio;
        this.bgmType = bgmType;
        this.musicName = musicName;
        this.musicSingler = musicSingler;
        this.bgmCallback = bgmCallback;
    }

    public MusicOperateDialogFragment() {

    }

    private void init(View view) {
        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        rootLayout = (LinearLayout) view.findViewById(R.id.ll_bgm);
        rootLayout.getBackground().setAlpha(200);

        bgmTitleTxt = (TextView) view.findViewById(R.id.bgm_operate_musicName);
        playImb = (ImageView) view.findViewById(R.id.bgm_operate_musicPause);
        bgmTitleTxt.setText(musicName + "   " + musicSingler);
        bgm_seekBar = (SeekBar) view.findViewById(R.id.bgm_seekBar);

        if (bgmType == BGMEnum.PLAY){
            playImb.setImageResource(R.drawable.icon_bgm_operate_musicplay);
        }else {
            playImb.setImageResource(R.drawable.icon_bgm_operate_musicpause);
        }

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        bgm_seekBar.setProgress(audio);
        LogUtil.d("Audio max:" + max + ">>>>" + "Audio max:" + current);

        bgm_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bgmCallback.audio(seekBar.getProgress());
                LogUtil.d("Audio seekBar:" + seekBar.getProgress());
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
        View view = inflater.inflate(R.layout.dfrg_bgm_operate, null);
        init(view);
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

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        bgmCallback.onDismiss();
    }
}
