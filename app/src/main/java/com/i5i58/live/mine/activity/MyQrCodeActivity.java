package com.i5i58.live.mine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.imgUtil.BitmapUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.glide.GlideRoundTransform;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.ref.WeakReference;

/**
 * Created by Lee on 2017/4/20.
 */

@ContentView(R.layout.act_mine_myqrcode)
public class MyQrCodeActivity extends BaseActivity {

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.table_top_img_menu)
    private ImageView editImg;

    @ViewInject(R.id.iv_qrcode)
    private ImageView qrCodeImg;

    @ViewInject(R.id.mine_qrcode_icon)
    private ImageView iconImg;

    @ViewInject(R.id.tv_nickname)
    private TextView nickNameTxt;

    @ViewInject(R.id.tv_location)
    private TextView locationTxt;

    @ViewInject(R.id.my_qrcode_ll_share)
    private LinearLayout saveLayout;

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<MyQrCodeActivity> mActivity;

        private CustomShareListener(MyQrCodeActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST
                        && platform != SHARE_MEDIA.LINKEDIN
                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST
                    && platform != SHARE_MEDIA.LINKEDIN
                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    private void share() {
        final UMImage image = new UMImage(MyQrCodeActivity.this, BitmapUtil.copyView(saveLayout));//bitmap文件
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        new ShareAction(MyQrCodeActivity.this)
                                .withMedia(image)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setTitleText("分享到");
        config.setTitleTextColor(R.color.black);
        config.setIndicatorVisibility(false);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        mShareAction.open(config);
    }

    /**
     * 分享图片Popwindow
     */
    @Event(R.id.table_top_fl_menu)
    private void shareImagePopwindow(View v) {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_myqrcode_share, null);

        TextView share    = (TextView) popView.findViewById(R.id.pop_qrcode_share);
        TextView save     = (TextView) popView.findViewById(R.id.pop_qrcode_save);
        TextView cancel   = (TextView) popView.findViewById(R.id.pop_qrcode_cancel);
        View     close    = popView.findViewById(R.id.pop_close);

        share.setText("分享");
        save.setText("保存照片");
        cancel.setText("取消");

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    //分享
                    case R.id.pop_qrcode_share:
                        share();
                        break;
                    //保存照片
                    case R.id.pop_qrcode_save:
                        FileUtils.saveImageToGallery(MyQrCodeActivity.this, BitmapUtil.copyView(saveLayout));
                        break;

                    case R.id.pop_qrcode_cancel:
                    case R.id.pop_close:

                        break;
                }
                popWindow.dismiss();

            }
        };
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        share.setOnClickListener(listener);
        save.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }

    private void init() {
        try {
            titleTxt.setText(R.string.my_qrcode);
            editImg.setImageDrawable(getResources().getDrawable(R.drawable.bt_mine_myqrcode_menu));
            nickNameTxt.setText(SystemCache.getPersonalMsg().getAccount().getName());
            locationTxt.setText(StringUtil.getLocation(SystemCache.getPersonalMsg().getAccount().getLocation()));
            Glide.with(this).load(StringUtil.checkIcon(SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl()))
                    .transform(new GlideRoundTransform(this,5))
                    .placeholder(R.drawable.icon_myqrcode_default)
                    .error(R.drawable.icon_myqrcode_default)
                    .into(iconImg);
            qrCodeImg.setImageBitmap(BitmapUtil.Create2DCode(SystemCache.getPersonalMsg().getQrCode()));
        } catch (NullPointerException e){
            LogUtil.e(e.toString());
        } catch (WriterException e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
