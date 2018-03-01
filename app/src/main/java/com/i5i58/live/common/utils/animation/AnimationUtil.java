package com.i5i58.live.common.utils.animation;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Lee on 2017/4/13.
 */

public class AnimationUtil {

    public static TranslateAnimation moveToViewBottom() {//底部隐藏
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation moveFromViewBootom() {//底部显示
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation moveToViewTop() {//顶部隐藏
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation moveFromViewTop() {//顶部显示
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF,-1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    public static TranslateAnimation moveFromViewLeft() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation moveToViewLeft() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static TranslateAnimation moveFromViewRight() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(100);
        return mHiddenAction;
    }

    public static TranslateAnimation moveToViewRight() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(100);
        return mHiddenAction;
    }

}
