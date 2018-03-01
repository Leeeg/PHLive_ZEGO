package com.i5i58.live.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.androidadvance.topsnackbar.TSnackbar;
import com.githang.statusbar.StatusBarCompat;
import com.i5i58.live.R;
import com.i5i58.live.common.manager.APPManager;
import com.i5i58.live.common.utils.tSnackBar.TSnackbarUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.xutils.x;


/**
 * Created by Lee on 2017/4/7.
 */

public class BaseActivity extends FragmentActivity {

    private String TAG = "BaseActivity";
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private int color;
    protected APPManager appManager;
    InputMethodManager manager;
    private static final Handler handler = new Handler();
    protected final Handler getHandler() {
        return handler;
    }

    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
    }

    protected void setStatusBar(Activity context){//修改状态栏颜色
        StatusBarCompat.setStatusBarColor(context, color, false);
    }

    protected void intentAct(Activity activity,Intent intent){
        activity.startActivity(intent);
        overridePendingTransition(R.anim.act_in_from_right, R.anim.act_out_to_left);
    }

    protected void closeAct(Activity activity){
        appManager.removeActivity(activity);
        overridePendingTransition(R.anim.act_in_from_left, R.anim.act_out_to_right);
    }

    public void TSBSuccess(String msg){
        TSnackbarUtil.TSBSuccess(this,msg);
    }

    public void TSBError(String msg){
        TSnackbarUtil.TSBError(this,msg);
    }

    public TSnackbar TSBErrorLongtimeShow(String msg){
        return TSnackbarUtil.TSBErrorLongtimeShow(this,msg);
    }

    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
//                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//        return false;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        PushAgent.getInstance(this).onAppStart();
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        color = getResources().getColor(R.color.colorBlack);
//        setStatusBar(this);
        appManager = APPManager.getInstance();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
