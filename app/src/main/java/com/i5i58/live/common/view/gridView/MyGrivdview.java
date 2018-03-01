package com.i5i58.live.common.view.gridView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Lee on 2017/4/18.
 */

public class MyGrivdview extends GridView {

    public MyGrivdview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGrivdview(Context context) {
        super(context);
    }

    public MyGrivdview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
