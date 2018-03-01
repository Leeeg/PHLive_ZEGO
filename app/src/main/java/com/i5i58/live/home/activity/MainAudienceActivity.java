package com.i5i58.live.home.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.ZegoAppHelper;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.view.gift2dview.Box2DFragment;
import com.i5i58.live.common.view.gift2dview.Tools.ScreenParamUtil;
import com.i5i58.live.emoji.PickerConfig;
import com.i5i58.live.emoji.PickerlImageLoadTool;
import com.i5i58.live.home.fragments.RoomFragment;
import com.i5i58.live.home.helper.channelGift.GiftMessageQueue;
import com.i5i58.live.home.helper.channelGift.MessageThread;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.permission.MPermission;
import com.i5i58.live.permission.annotation.OnMPermissionDenied;
import com.i5i58.live.permission.annotation.OnMPermissionGranted;
import com.i5i58.live.permission.annotation.OnMPermissionNeverAskAgain;
import com.i5i58.live.permission.util.MPermissionUtil;
import com.umeng.socialize.UMShareAPI;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by Lee on 2017/5/9.
 */

public class MainAudienceActivity extends BaseActivity implements AndroidFragmentApplication.Callbacks {

    private VerticalViewPager mViewPager;
    private RelativeLayout mRoomContainer;
    private PagerAdapter mPagerAdapter;
    private FrameLayout mFragmentContainer;
    private FragmentManager mFragmentManager;
    private int mCurrentItem;
    private int mRoomId = -1;
    private boolean mInit = false;
    private RoomFragment mRoomFragment;
    private List<ChannelData> channels;
    private int position;

    //box2D
    private Box2DFragment m_box2dFgm;
    private FrameLayout m_container;

    private GiftMessageQueue giftMessageQueue;

    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE};


    private void initPicker(boolean isInit) {
        if (isInit){
            PickerConfig.checkImageLoaderConfig(this);
        }else {
            PickerlImageLoadTool.clear();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
//        hideNavigationBar();
        appManager.addActivity(this);
        TigerApplication.getInstance().isInRoom = true;
        TigerApplication.getInstance().isFromWeb = false;
        requestBasicPermission();

        channels = getIntent().getParcelableArrayListExtra("channels");
        position = getIntent().getIntExtra("position", 0);
        mRoomFragment = RoomFragment.newInstance(channels);

        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);
        mRoomContainer = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.view_room_container, null);
        mFragmentContainer = (FrameLayout) mRoomContainer.findViewById(R.id.fragment_container);
        mFragmentManager = getSupportFragmentManager();
        mPagerAdapter = new PagerAdapter(channels);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.d("mCurrentId == " + position + ", positionOffset == " + positionOffset +
                        ", positionOffsetPixels == " + positionOffsetPixels);
                mCurrentItem = position;
            }
        });

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                ViewGroup viewGroup = (ViewGroup) page;
                LogUtil.d("page.id == " + page.getId() + ", position == " + position);

                if ((position < 0 && viewGroup.getId() != mCurrentItem)) {
                    View roomContainer = viewGroup.findViewById(R.id.room_container);
                    if (roomContainer != null && roomContainer.getParent() != null && roomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (roomContainer.getParent())).removeView(roomContainer);
                    }
                }
                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == mCurrentItem && position == 0 && mCurrentItem != mRoomId) {
                    if (mRoomContainer.getParent() != null && mRoomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (mRoomContainer.getParent())).removeView(mRoomContainer);
                    }
                    loadVideoAndChatRoom(viewGroup, mCurrentItem);
                }
            }
        });

        initBox2d();

        initPicker(true);

        giftMessageQueue = new GiftMessageQueue(this);

    }

    public GiftMessageQueue getGiftMessageQueue(){
        return giftMessageQueue;
    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mRoomFragment.change((Integer) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadFragment(final int currentItem){
        new Thread() {
            public void run() {
                super.run();
                if (null == mRoomFragment || !mRoomFragment.isAdded()) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadFragment(currentItem);
                        }
                    }, 200);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = currentItem;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * @param viewGroup
     * @param currentItem
     */
    private void loadVideoAndChatRoom(ViewGroup viewGroup, int currentItem) {
        //聊天室的fragment只加载一次，以后复用
        LogUtil.d("currentItem >>>>> " + currentItem);
        mRoomId = currentItem;
        if (!mInit) {
            mFragmentManager.beginTransaction().add(mFragmentContainer.getId(), mRoomFragment).commitAllowingStateLoss();
            mInit = true;
        }
        viewGroup.addView(mRoomContainer);
        mRoomFragment.clear(false);
        loadFragment(currentItem);
    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(MainAudienceActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        TSBSuccess("授权成功！");
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        finish();
        TSBSuccess("授权失败！");
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        TSBSuccess("授权成功！");
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        TSBError(tip);
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if(deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }
        TSBError(sb.toString());
    }

    private void close(){
        mRoomFragment.clear(true);
        closeAct(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
        TigerApplication.getInstance().isInRoom = false;
        mRoomFragment.clear(true);
        initPicker(false);
        giftMessageQueue.destroyQueue();
        LogUtil.d("System >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GC");
    }

    @Override
    public void exit() {

    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mShareAction.close();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showBox2dFgmFullScreen();
        } else {
            showBox2dFgmNormalScreen();
        }
    }

    private void initBox2d() {
        m_container = find(R.id.lyt_container);
        m_box2dFgm = new Box2DFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.lyt_container, m_box2dFgm).commitAllowingStateLoss();
        showBox2dFgmNormalScreen();
    }

    private void showBox2dFgmFullScreen() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m_container.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        m_container.setLayoutParams(params);
    }

    private void showBox2dFgmNormalScreen() {
        int width = ScreenParamUtil.GetScreenWidthPx(this);
        int height = ScreenParamUtil.GetScreenHeightPx(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m_container.getLayoutParams();
        params.width = width;
        params.height = height;
        m_container.setLayoutParams(params);
    }

    public void addStar(boolean b){
        LogUtil.d("addStar");
        m_box2dFgm.addBall(b);
    }

    class PagerAdapter extends android.support.v4.view.PagerAdapter {

        List<ChannelData> channels;

        public PagerAdapter(List<ChannelData> channels) {
            this.channels = channels;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_room_item, null);
            view.setId(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        LogUtil.d("Umeng shaer onActivityresult !");
    }

    private void hideNavigationBar() {

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏Navigation Bar

                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;//防止Navigation Bar在覆盖view的情况下上弹

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_room, null);

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View v, MotionEvent event) {

                Log.e("rocky", "Hi....");

                return false;

            }

        });

        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        int width = windowManager.getDefaultDisplay().getWidth();

        int height = windowManager.getDefaultDisplay().getHeight();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(width, height,

                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,

                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, //让window占满整个手机屏幕，不留任何边界（border）

                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;

        params.y = 0;

        params.x = 0;

        windowManager.addView(view, params);

        view.setSystemUiVisibility(flags);

    }


    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

}
