package com.i5i58.live.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.db.User;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.md5.MD5Util;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.main.activity.MainActivity;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.account.AccByLogin;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Lee on 2017/6/12.
 */

@ContentView(R.layout.act_setpassword)
public class RegisterSetPasswordActivity extends BaseActivity {

    private String number, code, password;
    private boolean checkOver = false;
    private AbortableFuture<LoginInfo> loginRequest;
    private PopupWindow popWindow;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.tv_passworderr)
    private TextView notifyTxt;

    @ViewInject(R.id.password2)
    private EditText confirmEdit;

    @ViewInject(R.id.password1)
    private EditText passwordTxt;

    @Event(R.id.table_top_fl_back)
    private void onBack(View view){
        closeAct(this);
    }

    @Event(R.id.tv_secre)
    private void onClauseClick(View view){
        Intent intent = new Intent(this, MyReactActivity.class);
        MyReactActivity.mainName = "SecretIndex";
        startActivity(intent);
    }

    @Event(R.id.bt_over)
    private void onCompleteClick(View view){
        Editable p1 = passwordTxt.getText();
        Editable p2 = confirmEdit.getText();
        if (null == p1 || null == p2 || !checkOver){
            TSBError("请输入正确的密码！");
            return;
        }
        loadingPopWindow();
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.REGISTER)
                .addParam("phoneNo", number)
                .addParam("verifCode", code)
                .addParam("password", password)
                .addParam("device", SystemCache.DEVICE)
                .addParam("serialNum", SystemCache.getSerialNumber())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                String md5Password = MD5Util.getMd5(password);
                                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                                        .addRouteUrl(API.LOGINBYPHONE)
                                        .addParam("phoneNo"     , number)
                                        .addParam("password"    , md5Password)
                                        .addParam("version"     , UserPreferences.getLoginVersion())
                                        .addParam("device"      , SystemCache.DEVICE)
                                        .addParam("serialNum"   , SystemCache.getSerialNumber())
                                        .getResult(new HttpCallback() {
                                            @Override
                                            @HttpResultCut
                                            public void success(JSONObject js, boolean success) {
                                                try {
                                                    if (js.getString("code").equals("success")) {
                                                        final JSONObject data = js.getJSONObject("data");
                                                        AccByLogin acc = GsonInner.getGsonInstance().fromJson(data.getJSONObject("acc").toString(),AccByLogin.class);
                                                        acc.setPhoneNo(number);
                                                        SystemCache.setAuthed(acc.isAuthed());
                                                        SysPreferences.savePhone(number);
                                                        SysPreferences.saveIcon(acc.getFaceSmallUrl());
                                                        SysPreferences.saveLoginByPhone(true);
                                                        UserPreferences.saveAccId(acc.getAccId());
                                                        UserPreferences.saveNickName(acc.getNickName());
                                                        UserPreferences.saveFace(acc.getFaceSmallUrl());
                                                        UserPreferences.saveOpenId(acc.getOpenId());
                                                        UserPreferences.saveToken(data.getString("token"));
                                                        saveUser(acc, data.getString("token"));
                                                        NimLogin(acc.getAccId(), data.getString("token"));
                                                    } else {
                                                        TSBError("登陆出错：" + js.getString("msg"));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    LogUtil.e(e.toString());
                                                }catch (NullPointerException e){
                                                    LogUtil.e(e.toString());
                                                }
                                            }
                                        });
                            } else {
                                TSBError(js.getString("msg"));
                                LogUtil.e("check register code error !：" + js.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }catch (NullPointerException e){
                            LogUtil.e(e.toString());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    /**
     * 进入主界面
     */
    private void intentToMain(){
        intentAct(this,new Intent(RegisterSetPasswordActivity.this, MainActivity.class));
        UserPreferences.saveLogin(true);
//        appManager.removeClass(LoginMainActivity.class);
//        appManager.removeClass(RegisterActivity.class);
//        appManager.removeClass(RegisterSetPasswordActivity.class);
        closeAct(this);
    }

    /**
     * 登录云信
     */
    private void NimLogin(String account, String token){
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                popWindow.dismiss();
                intentToMain();
            }

            @Override
            public void onFailed(int code) {
                popWindow.dismiss();
                if (code == 302 || code == 404) {
                    TSBError("code:" + code + "\n账号或密码错误");
                } else {
                    TSBError("code:" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                popWindow.dismiss();
            }
        });
    }

    /**
     * 保存用户登录信息
     */
    private void saveUser(AccByLogin acc, String token) {
        try {
            User user = TigerApplication.getInstance().getDbManager().findById(User.class, acc.getAccId());
            if(null == user){
                User userNew = new User()
                        .setPhone(acc.getPhoneNo())
                        .setAccId(acc.getAccId())
                        .setToken(token)
                        .setIcon(acc.getFaceSmallUrl());
                TigerApplication.getInstance().getDbManager().save(userNew);
            }else{
                user.setPhone(acc.getPhoneNo())
                        .setAccId(acc.getAccId())
                        .setToken(token)
                        .setIcon(acc.getFaceSmallUrl());
                TigerApplication.getInstance().getDbManager().update(user);
            }
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("保存用户登录信息失败");
        }
    }

    private void loadingPopWindow() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_loading, null);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        ImageView imageView = (ImageView) popView.findViewById(R.id.pop_loading_img);
        imageView.setImageResource(com.handmark.pulltorefresh.library.R.drawable.drawable_waiting);
        final AnimationDrawable mAnimationDrawable = (AnimationDrawable) imageView.getDrawable();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopupAnimation);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                mAnimationDrawable.stop();
                popWindow = null;
            }
        });
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mAnimationDrawable.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        code = getIntent().getStringExtra("code");
        number = getIntent().getStringExtra("number");
        titleTxt.setText("设置密码");
        confirmEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwordOne = passwordTxt.getText().toString().trim();
                String passwordTwo = editable.toString().trim();
                if (!passwordTwo.equals(passwordOne)) {
                    notifyTxt.setText("密码不一致，请重新输入！");
                } else {
                    notifyTxt.setText("");
                    password = passwordTwo;
                    checkOver = true;
                }
            }
        });
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return false;
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
