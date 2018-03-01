package com.i5i58.live.common.view.imageVIew;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.i5i58.live.R;

/**
 * Created by Administrator on 2015/4/30.
 */
public class MyCircleAngleImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint mPressPaint;

    private int mWidth;
    private int mHeight;

    private int mPressAlpha;
    private int mPressColor;
    private int mRadius;
    private int mShapeType;
    private int mBorderWidth;
    private int mBorderColor;

    public MyCircleAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public MyCircleAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyCircleAngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mPressAlpha = 64;
        mPressColor = getResources().getColor(R.color.ml_gray);
        mRadius = 16;
        mShapeType = 1;
        mBorderWidth = 0;
        mBorderColor = getResources().getColor(R.color.ml_red);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MLImageView);
            mPressColor = array.getColor(R.styleable.MLImageView_press_color, mPressColor);
            mPressAlpha = array.getInteger(R.styleable.MLImageView_press_alpha, mPressAlpha);
            mRadius = array.getDimensionPixelSize(R.styleable.MLImageView_radius, mRadius);
            mShapeType = array.getInteger(R.styleable.MLImageView_shape_type, mShapeType);
            mBorderWidth = array.getDimensionPixelOffset(R.styleable.MLImageView_border_width1, mBorderWidth);
            mBorderColor = array.getColor(R.styleable.MLImageView_border_color1, mBorderColor);
            array.recycle();
        }

        mPressPaint = new Paint();
        mPressPaint.setAntiAlias(true);
        mPressPaint.setStyle(Paint.Style.FILL);
        mPressPaint.setColor(mPressColor);
        mPressPaint.setAlpha(0);
        mPressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        setClickable(true);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawDrawable(canvas, bitmap);
        drawPress(canvas);
        drawBorder(canvas);

    }

    private void drawDrawable(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColor(0xffffffff);
        paint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        int saveFlags = Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, mWidth, mHeight, null, saveFlags);

        if (mShapeType == 0) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, paint);
        } else {
            RectF rectf = new RectF(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(rectf, mRadius, mRadius, paint);
        }

        paint.setXfermode(xfermode);
        float scaleWidth = ((float) getWidth()) / bitmap.getWidth();
        float scaleHeight = ((float) getHeight()) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }

    private void drawPress(Canvas canvas) {

        if(mShapeType == 0){
            canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2, mPressPaint);
        }else if (mShapeType == 1) {
            RectF rectF = new RectF(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPressPaint);
        }
    }
    private void drawBorder(Canvas canvas){
        if(mBorderWidth > 0){
            Paint paint = new Paint();
            paint.setStrokeWidth(mBorderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(mBorderColor);
            paint.setAntiAlias(true);
            if (mShapeType == 0) {
                canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, paint);
            } else {
                RectF rectf = new RectF(0, 0, getWidth(), getHeight());
                canvas.drawRoundRect(rectf, mRadius, mRadius, paint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPressPaint.setAlpha(mPressAlpha);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                mPressPaint.setAlpha(0);
//                invalidate();
//                break;
//            default:
//                invalidate();
//                break;
//        }
        return false;
    }

}
