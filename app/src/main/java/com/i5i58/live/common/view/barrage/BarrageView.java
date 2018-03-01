package com.i5i58.live.common.view.barrage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.view.imageVIew.RoundImageView;
import com.i5i58.live.model.api.API;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;

/**
 * Created by lixueyong on 16/2/19.
 */
public class BarrageView extends RelativeLayout {
    private Context mContext;
//    private BarrageHandler mHandler = new BarrageHandler();
    private Random random = new Random(System.currentTimeMillis());

    private int lineHeight = 300;//每一行弹幕的高度
    private int totalLine = 3;//弹幕的行数

    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void generateItem(Map<String, String> map) {
        BarrageItem item = new BarrageItem();
        item.itemLayout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.barrage_item, null);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        item.itemLayout.setLayoutParams(params);
        item.icon = (RoundImageView) item.itemLayout.findViewById(R.id.ri_icon);
        item.name = (TextView) item.itemLayout.findViewById(R.id.tv_name);
        item.textView = (TextView) item.itemLayout.findViewById(R.id.tv_text);

        if(null != map.get("icon")){
            Glide.with(mContext).load(API.OSS_URL_ICON + map.get("icon"))
                    .asBitmap()
                    .into(item.icon);
        }
        if(null != map.get("vip")){
            item.vip = (ImageView) item.itemLayout.findViewById(R.id.iv_vip);
            InputStream vipIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + map.get("vip")));
            Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
            item.vip.setImageBitmap(vipBitmap);
            item.vip.setVisibility(View.VISIBLE);
        }
        if(null != map.get("guard")){
            item.guard = (ImageView) item.itemLayout.findViewById(R.id.iv_guard);
            InputStream guardIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + map.get("guard")));
            Bitmap guardBitmap = BitmapFactory.decodeStream(guardIs);
            item.guard.setImageBitmap(guardBitmap);
            item.guard.setVisibility(View.VISIBLE);
        }
        if(null != map.get("lv")){
            item.lv = (ImageView) item.itemLayout.findViewById(R.id.iv_lv);
            InputStream richScoreIs = getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(Long.valueOf(map.get("lv")))));
            Bitmap guardBitmap = BitmapFactory.decodeStream(richScoreIs);
            item.lv.setImageBitmap(guardBitmap);
            item.lv.setVisibility(View.VISIBLE);
        }
        if(null != map.get("name")){
            item.name.setText(map.get("name"));
        }
        if(null != map.get("content")){
            item.textView.setText(map.get("content"));
        }
        if(null != map.get("color")){
            item.textView.setTextColor(Color.rgb(243 ,127, 243));
        }
        item.textMeasuredWidth = item.itemLayout.getWidth();
        item.moveSpeed = 6000;
        item.verticalPos = random.nextInt(totalLine) * lineHeight + 400;
        showBarrageItem(item);
    }

    private void showBarrageItem(final BarrageItem item) {

        int leftMargin = this.getRight() - this.getLeft() - this.getPaddingLeft();

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = item.verticalPos;
        this.addView(item.itemLayout, params);
        Animation anim = generateTranslateAnim(item, leftMargin);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                item.itemLayout.clearAnimation();
                BarrageView.this.removeView(item.itemLayout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        item.itemLayout.startAnimation(anim);
    }

    private TranslateAnimation generateTranslateAnim(BarrageItem item, int leftMargin) {
        TranslateAnimation anim = new TranslateAnimation(leftMargin, -item.textMeasuredWidth - 500, 0, 0);
        anim.setDuration(item.moveSpeed);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 计算TextView中字符串的长度
     * @param text 要计算的字符串
     * @param Size 字体大小
     * @return TextView中字符串的长度
     */
    public float getTextWidth(BarrageItem item, String text, float Size) {
        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }
}
