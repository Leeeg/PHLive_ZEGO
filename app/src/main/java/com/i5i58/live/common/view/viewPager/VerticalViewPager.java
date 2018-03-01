package com.i5i58.live.common.view.viewPager;

/**
 * Copyright (C) 2015 Kaelaela
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.i5i58.live.common.view.transforms.DefaultTransformer;

public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new DefaultTransformer());
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
        //If not intercept, touch event should not be swapped.
        swapTouchEvent(event);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }

//
//    // 滑动距离及坐标 归还父控件焦点
//    private float xDistance, yDistance, xLast, yLast,xDown, mLeft;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                xDown = ev.getX();
//                mLeft = ev.getX();// 解决与侧边栏滑动冲突
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//                if (mLeft < 100 || xDistance < yDistance) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    if (getCurrentItem() == 0) {
//                        if (curX < xDown) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                    } else if (getCurrentItem() == (getAdapter().getCount()-1)) {
//                        if (curX > xDown) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                    } else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

}
