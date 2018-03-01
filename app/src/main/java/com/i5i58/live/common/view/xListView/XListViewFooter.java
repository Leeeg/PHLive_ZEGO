/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.i5i58.live.common.view.xListView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i5i58.live.R;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_NOMORE = 3;

	private Context mContext;

	private View mContentView;
	private TextView mHintView;
	private ImageView mImageview;
	private AnimationDrawable animationDrawable;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		mImageview.setVisibility(View.INVISIBLE);
		if (state != STATE_LOADING) {
			animationDrawable.stop();
		}
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			mImageview.setVisibility(View.VISIBLE);
			animationDrawable.start();
		} else if (state == STATE_NOMORE) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_nomore);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mImageview.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mImageview.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mImageview = (ImageView) moreView
				.findViewById(R.id.xlistview_footer_progress_img);
		animationDrawable = (AnimationDrawable) mImageview.getDrawable();
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
	}

}
