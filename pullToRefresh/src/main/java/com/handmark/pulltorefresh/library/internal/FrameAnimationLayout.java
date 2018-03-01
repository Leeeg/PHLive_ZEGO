package com.handmark.pulltorefresh.library.internal;

/**
 * Created by Lee on 2017/2/28.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

public class FrameAnimationLayout extends LoadingLayout {

    private AnimationDrawable mAnimationDrawable;

    public FrameAnimationLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mHeaderImage.setImageResource(R.drawable.drawable_waiting);
        mAnimationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.drawable_waiting;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
    }

    @Override
    protected void pullToRefreshImpl() {
    }

    @Override
    protected void refreshingImpl() {
        mAnimationDrawable.start();
    }

    @Override
    protected void releaseToRefreshImpl() {
    }

    @Override
    protected void resetImpl() {
    }
}
