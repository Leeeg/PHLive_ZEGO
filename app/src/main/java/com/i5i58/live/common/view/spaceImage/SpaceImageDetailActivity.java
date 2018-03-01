package com.i5i58.live.common.view.spaceImage;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.i5i58.live.common.base.BaseActivity;

public class SpaceImageDetailActivity extends BaseActivity implements OnTouchListener {

    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private String url;
    SmoothImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ScaleType.FIT_CENTER);
        setContentView(imageView);
        imageView.setOnTouchListener(this);

        url = getIntent().getStringExtra("images");
        Glide.with(this).load(url)
                .asBitmap()
                .into(imageView);
        if(getIntent().getBooleanExtra("isOrg",false)){
            Toast.makeText(this, "暂无高清图", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
        return false;
    }


}
