package com.i5i58.live.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.db.User;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.LoginEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.animation.AnimationUtil;
import com.i5i58.live.common.utils.md5.MD5Util;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.common.view.listview.HorizontalListView;
import com.i5i58.live.login.adapter.HistoryLoginAdapter;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 手机登录界面
 * Created by Lee on 2017/4/7.
 */

@ContentView(R.layout.act_loginbyphone)
public class LoginByPhoneActivity extends BaseActivity{

    private HistoryLoginAdapter historyLoginAdapter;
    private List<String> historyList_icon = new ArrayList<>();
    private List<User> users;
    private boolean historyIsShowed = true;
    private LoginEnum loginType;
    private AbortableFuture<LoginInfo> loginRequest;
    private PopupWindow popWindow;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.loginbyphone_edt_number)
    private EditText phoneNumberEdt;

    @ViewInject(R.id.loginbyphone_edt_password)
    private EditText passwordEdt;

    @ViewInject(R.id.loginbyphone_unmber_ibt_clean)
    private ImageButton clearNumberIbt;

    @ViewInject(value = R.id.loginbyphone_til_number)
    private TextInputLayout numberTil;

    @ViewInject(value = R.id.loginbyphone_til_password)
    private TextInputLayout passwordTil;

    @ViewInject(R.id.loginbyphone_hlis_history)
    private HorizontalListView historyHlis;

    @ViewInject(R.id.loginbyphone_unmber_ibt_showhist)
    private ImageButton showHistorylisIbt;

    @ViewInject(R.id.loginbyphone_fl_history)
    private FrameLayout showHistorylisFl;

    @ViewInject(R.id.loginbyphone_icon)
    private ImageView iconImg;

    @Event(R.id.tv_forgetpassword)
    private void onProblemClick(View view){
        Intent intent = new Intent(this, MyReactActivity.class);
        intent.putExtra("accId", "0");
        intent.putExtra("token", "0");
        MyReactActivity.mainName = "SafeIndex";
        startActivity(intent);
    }

    @Event(R.id.tv_register)
    private void onRegisterClick(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Event(value = R.id.loginbyphone_hlis_history,type = AdapterView.OnItemClickListener.class)
    private void historyListviewItemClick(AdapterView<?> adapterView, View view, int i, long l){
        if(!historyLoginAdapter.canDelete){
            phoneNumberEdt.setText(users.get(i).getPhone());
            Glide.with(this).load(StringUtil.checkIcon(users.get(i).getIcon()))
                    .transform(new GlideCircleTransform(this))
                    .placeholder(R.drawable.icon_mine_personal_default)
                    .error(R.drawable.icon_mine_personal_default)
                    .into(iconImg);
            passwordEdt.setText("0123456789");
        }else{
            historyList_icon.remove(i);
            deleteUser(i);
            historyLoginAdapter.notifyDataSetChanged();
        }
        loginType = LoginEnum.TOKEN;
    }

    @Event(value = R.id.loginbyphone_hlis_history,type = AdapterView.OnItemLongClickListener.class)
    private boolean historyListviewItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
        historyLoginAdapter.canDelete = true;
        historyLoginAdapter.notifyDataSetChanged();
        return false;
    }

    @Event(R.id.loginbyphone_unmber_ibt_clean)
    private void clearPhoneNumber(ImageButton v){
        phoneNumberEdt.setText("");
        passwordEdt.setText("");
        numberTil.setErrorEnabled(false);
        passwordTil.setErrorEnabled(false);
        v.setVisibility(View.GONE);
        v.setAnimation(AnimationUtil.moveToViewRight());
    }

    @Event(R.id.loginbyphone_bt_login)
    private void loginBtClick(View v){
        loadingPopWindow();
//        hideKeyboard();
        exitEditListview();
        if(loginType == LoginEnum.PHONE){
            loginByPhone();
        }else {
            loginByToken();
        }
    }

    @Event(R.id.loginbyphone_unmber_ibt_showhist)
    private void showHistoryLayout(ImageButton v){
        if (historyIsShowed){
            v.setImageDrawable(getResources().getDrawable(R.drawable.bt_loginbyphone_showhist_up));
            showHistorylisFl.setVisibility(View.VISIBLE);
        }else {
            v.setImageDrawable(getResources().getDrawable(R.drawable.bt_loginbyphone_showhist_down));
            showHistorylisFl.setVisibility(View.GONE);
        }
        historyIsShowed = !historyIsShowed;
        exitEditListview();
    }

    @Event(R.id.ll_root)
    private void onRootTouch(View v) {
        exitEditListview();
//        hideKeyboard();
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

    /**
     * 退出历史纪录编辑模式
     */
    private void exitEditListview(){
        if (historyLoginAdapter.canDelete){
            historyLoginAdapter.canDelete = false;
            historyLoginAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 控件配置
     */
    private void configView(){
        phoneNumberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                exitEditListview();
                loginType = LoginEnum.PHONE;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(null != editable && !"".equals(editable) && editable.length() > 0){
                    clearNumberIbt.setVisibility(View.VISIBLE);
                }else{
                    clearNumberIbt.setVisibility(View.GONE);
                }
                numberTil.setErrorEnabled(false);
            }
        });

        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                exitEditListview();
                loginType = LoginEnum.PHONE;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if(null != editable && !"".equals(editable) && editable.length() > 0){
//                    passwordTil.setPasswordVisibilityToggleEnabled(true);
//                }else{
//                    passwordTil.setPasswordVisibilityToggleEnabled(false);
//                }
                passwordTil.setErrorEnabled(false);
            }
        });

        historyLoginAdapter = new HistoryLoginAdapter(this,historyList_icon);
        historyHlis.setAdapter(historyLoginAdapter);
    }

    /**
     * 账号登陆
     */
    private void loginByPhone(){
        final String number = phoneNumberEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        if(null == number || "".equals(number)){
            numberTil.setError("请输入账号");
            popWindow.dismiss();
            return;
        }
        if(null == password || "".equals(password)){
            numberTil.setError("请输入密码");
            popWindow.dismiss();
            return;
        }
        try {
            String md5Password = MD5Util.getMd5(password);
            if (number.length() == 11){
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.LOGINBYPHONE)
                        .addParam("phoneNo"     , number)
                        .addParam("password"    , md5Password)
                        .addParam("version"     , UserPreferences.getLoginVersion())
                        .addParam("device"      , SystemCache.DEVICE)
                        .addParam("serialNum"   , SystemCache.getSerialNumber())
                        .getResult(new HttpCallback() {
                            @Override
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
                                        UserPreferences.saveYxToken(data.getString("token"));
                                        saveUser(acc, data.getString("token"));
                                        NimLogin(acc.getAccId(), data.getString("token"));
                                    } else {
                                        popWindow.dismiss();
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
            }else {
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.LOGINBYOPENID)
                        .addParam("openId"     , number)
                        .addParam("password"    , md5Password)
                        .addParam("version"     , UserPreferences.getLoginVersion())
                        .addParam("device"      , SystemCache.DEVICE)
                        .addParam("serialNum"   , SystemCache.getSerialNumber())
                        .getResult(new HttpCallback() {
                            @Override
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
                                        UserPreferences.saveYxToken(data.getString("token"));
                                        saveUser(acc, data.getString("token"));
                                        NimLogin(acc.getAccId(), data.getString("token"));
                                    } else {
                                        popWindow.dismiss();
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

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }

    /**
     * token登陆
     */
    private void loginByToken(){
        final String number = phoneNumberEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        if(null == number || "".equals(number)){
            numberTil.setError("请输入账号");
            popWindow.dismiss();
            return;
        }
        if(null == password || "".equals(password)){
            numberTil.setError("请输入密码");
            popWindow.dismiss();
            return;
        }
        final User user = selectUserByPhone(number);
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrlWithToken(API.LOGINBYTOKEN, user.getAccId(), user.getToken())
                    .addParam("version"     , UserPreferences.getLoginVersion())
                    .addParam("device"      , SystemCache.DEVICE)
                    .addParam("serialNum"   , SystemCache.getSerialNumber())
                    .getResult(new HttpCallback() {
                        @Override
                        public void success(JSONObject js, boolean success) {
                            if (!success){
                                popWindow.dismiss();
                                TSBError("您的登录信息已失效，请重新输入账号密码登录！");
                                return;
                            }
                            try {
                                if (js.getString("code").equals("success")) {
                                    final JSONObject data = js.getJSONObject("data");
                                    AccByLogin acc = GsonInner.getGsonInstance().fromJson(data.toString(),AccByLogin.class);
                                    acc.setPhoneNo(number);
                                    SystemCache.setAuthed(acc.isAuthed());
                                    SysPreferences.savePhone(number);
                                    SysPreferences.saveIcon(user.getIcon());
                                    SysPreferences.saveLoginByPhone(true);
                                    UserPreferences.saveAccId(user.getAccId());
                                    UserPreferences.saveToken(user.getToken());
                                    UserPreferences.saveNickName(acc.getNickName());
                                    UserPreferences.saveOpenId(acc.getOpenId());
                                    UserPreferences.saveFace(acc.getFaceSmallUrl());
                                    saveUser(acc, user.getToken());
                                    NimLogin(user.getAccId(), user.getToken());
                                } else {
                                    popWindow.dismiss();
                                    TSBError("登陆出错：" + js.getString("msg"));
                                }
                            } catch (JSONException e) {
                                popWindow.dismiss();
                                TSBError("登陆出错");
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                            }catch (NullPointerException e){
                                popWindow.dismiss();
                                TSBError("登陆出错");
                                LogUtil.e(e.toString());
                            }
                        }
                    });
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
     * 进入主界面
     */
    private void intentToMain(){
        intentAct(this,new Intent(LoginByPhoneActivity.this, MainActivity.class));
        UserPreferences.saveLogin(true);
//        appManager.removeClass(LoginMainActivity.class);
        closeAct(this);
    }

    /**
     * 查询全部历史登陆数据
     */
    private void selectAllUsers(){
        try {
            historyList_icon.clear();
            users = TigerApplication.getInstance().getDbManager().findAll(User.class);
            if (null != users){
                for (User user: users) {
                    historyList_icon.add(user.getIcon());
                }
                historyLoginAdapter.notifyDataSetChanged();
                showHistorylisIbt.setImageDrawable(getResources().getDrawable(R.drawable.bt_loginbyphone_showhist_down));
                showHistorylisIbt.setVisibility(View.VISIBLE);
                if (null != SysPreferences.getPhone()){
                    phoneNumberEdt.setText(SysPreferences.getPhone());
                    passwordEdt.setText("0123456789");
                }
                String url = SysPreferences.getIcon();
                Glide.with(this).load(StringUtil.checkIcon(url))
                        .transform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.icon_mine_personal_default)
                        .error(R.drawable.icon_mine_personal_default)
                        .into(iconImg);
                loginType = LoginEnum.TOKEN;
            }else{
                showHistorylisIbt.setVisibility(View.GONE);
                showHistorylisFl.setVisibility(View.GONE);
            }
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("查询全部历史登陆数据出错");
        }
    }

    /**
     * 通过手机号查询历史登陆用户
     */
    private User selectUserByPhone(String phone){
        try {
            List<User> users = TigerApplication.getInstance().getDbManager().selector(User.class)
                    .where("phone","=",phone).limit(1).findAll();
            return users.get(0);
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("通过手机号查询历史登陆用户出错");
        }
        return null;
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
                 user   .setPhone(acc.getPhoneNo())
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

    /**
     * 删除用户登录信息
     */
    private void deleteUser(int index) {
        try {
            User user = TigerApplication.getInstance().getDbManager().findById(User.class, users.get(index).getAccId());
            TigerApplication.getInstance().getDbManager().delete(user);
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("删除用户登录信息失败");
        }
    }

    @Event(value = R.id.table_top_fl_back)
    private void back(View v){
        closeAct(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeAct(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        titleTxt.setText("登录");
        configView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectAllUsers();
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
