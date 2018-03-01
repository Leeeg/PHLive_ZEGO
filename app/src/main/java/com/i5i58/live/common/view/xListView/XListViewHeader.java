/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.i5i58.live.common.view.xListView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.i5i58.live.R;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView animImageView;
	private AnimationDrawable animationDrawable;
	private int mState = STATE_NORMAL;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		animImageView = (ImageView) findViewById(R.id.xlistview_anim_header);
	}

	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // 显示进度
			animImageView.setImageResource(R.drawable.load_header_animation);
			animationDrawable = (AnimationDrawable) animImageView.getDrawable();
		}

		switch (state) {
		// 下拉过程或则正常状态中
		case STATE_NORMAL:
			animImageView.setImageResource(R.drawable.hold1_2x);
			break;
		// 释放就可以刷新的状态
		case STATE_READY:
			animImageView.setImageResource(R.drawable.hold1_2x);
			break;
		// 刷新状态
		case STATE_REFRESHING:
			animationDrawable.start();
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
