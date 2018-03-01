package com.i5i58.live.common.view.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lee on 2017/5/19.
 */

public class NoInterceptViewPager extends ViewPager {
    public NoInterceptViewPager(Context context) {
        super(context);
    }

    public NoInterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        getParent().requestDisallowInterceptTouchEvent(true);//不允许截获手势
        return super.onTouchEvent(arg0);
    }
}
