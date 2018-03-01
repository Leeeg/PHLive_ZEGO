package com.i5i58.live.common.view.button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.i5i58.live.common.utils.imgUtil.BitmapUtil;

import org.xutils.common.util.LogUtil;

/**
 * Created by Lee on 2017/5/12.
 */

public class MyButton extends View {

    private volatile int time;
    private int distance = 20;
    private Bitmap bitmapBg, bitmapNum;
    private int width, height, widthNum, heightNum, widthBg, heightBg;
    private Paint paint;
    private int pointW, pointH;
    private int marginLeftOne, marginLeftTwo, marginTopOne, marginTopTwo;

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyButton(Context context)
    {
        this(context, null);
    }

    public void setBitmap(Bitmap bt1, Bitmap bt2) {
        width = 220;
        height = 220;

        bitmapBg = bt1;
        bitmapNum = BitmapUtil.cutBitmap(bt2, 1.158f);

        pointW = width / 2;
        pointH = height / 2;

        widthNum = bitmapNum.getWidth() / 10;
        heightNum = bitmapNum.getHeight();
        widthBg = bitmapBg.getWidth();
        heightBg = bitmapBg.getHeight();

        marginLeftOne = pointW - widthNum / 2;
        marginTopOne = pointH - heightNum / 2;
        marginLeftTwo = pointW - heightNum;
        marginTopTwo = pointH - heightNum / 2;

        paint = new Paint(); // 建立画笔
    }

    public void changeTime(int time){
        if (time == 1){
            setVisibility(GONE);
        }else {
            LogUtil.d("changeTime >>>  " + time );
            this.time = time;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmapBg == null || bitmapNum == null) {
            return;
        }
        LogUtil.d("onDraw >>>  " + time );
        canvas.drawBitmap(bitmapBg, new Rect(0, 0, widthBg, heightBg) , new Rect(0, 0, width, height), paint);
        if (time >= 10){
            int t1 = time/10 + 1;
            int t2 = time%10 + 1;
            canvas.drawBitmap(bitmapNum, new Rect((t1 - 1) * widthNum, 0, t1 * widthNum, heightNum) , new Rect(marginLeftOne - distance, marginTopOne, widthNum + marginLeftOne - distance, heightNum + marginTopOne), paint);
            canvas.drawBitmap(bitmapNum, new Rect((t2 - 1) * widthNum, 0, t2 * widthNum, heightNum) , new Rect(marginLeftOne + distance, marginTopOne, widthNum + marginLeftOne + distance, heightNum + marginTopOne), paint);
        }else {
            canvas.drawBitmap(bitmapNum, new Rect((time - 1) * widthNum, 0, time * widthNum, heightNum) , new Rect(marginLeftOne, marginTopOne, widthNum + marginLeftOne, heightNum + marginTopOne), paint);
        }

    }

}