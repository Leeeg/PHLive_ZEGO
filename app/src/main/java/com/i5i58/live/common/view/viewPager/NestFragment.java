package com.i5i58.live.common.view.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lee on 2017/4/24.
 */

public class NestFragment extends ViewPager {

    public NestFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestFragment(Context context) {
        super(context);
    }

    // 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast,xDown, mLeft;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                xDown = ev.getX();
                mLeft = ev.getX();// 解决与侧边栏滑动冲突
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (mLeft < 100 || xDistance < yDistance) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (getCurrentItem() == 0) {
                        if (curX < xDown) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else if (getCurrentItem() == (getAdapter().getCount()-1)) {
                        if (curX > xDown) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}