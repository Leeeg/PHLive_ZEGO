package com.i5i58.live.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.model.api.API;
import com.i5i58.live.reactNative.MyReactActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Lee on 2017/6/12.
 */

@ContentView(R.layout.act_register)
public class RegisterActivity extends BaseActivity {

    private Editable phonenum, code;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.bt_sendcode)
    private Button sendCodeBt;

    @ViewInject(R.id.bt_next)
    private Button nextBt;

    @ViewInject(R.id.et_phone)
    private EditText numberEdit;

    @ViewInject(R.id.et_code)
    private EditText codeEdit;

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

    @Event(R.id.bt_next)
    private void onNextClick(View view){
        phonenum = numberEdit.getText();
        code = codeEdit.getText();
        if(null == phonenum || "".equals(phonenum.toString())){
            TSBError("输入有误！");
            return;
        }
        if(null == code || "".equals(code.toString())){
            TSBError("输入有误！");
            return;
        }
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CHECKCODE)
                .addParam("phoneNo", phonenum.toString().trim())
                .addParam("verifCode", code.toString().trim())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                Intent intent = new Intent(RegisterActivity.this, RegisterSetPasswordActivity.class);
                                intent.putExtra("code", code.toString().trim());
                                intent.putExtra("number", phonenum.toString().trim());
                                startActivity(intent);
                            } else {
                                TSBError(js.getString("msg"));
                                LogUtil.e("check register code error !：" + js.getString("msg"));
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

    @Event(R.id.bt_sendcode)
    private void onSendCodeClick(View view){
        phonenum = numberEdit.getText();
        if(null == phonenum || "".equals(phonenum.toString())){
            TSBError("输入有误！");
            return;
        }
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.SENDCODE)
                .addParam("phoneNo", phonenum.toString().trim())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                TSBSuccess("验证码已发送，请注意查收！");
                                countDown();
                            } else {
                                TSBError(js.getString("msg"));
                                LogUtil.e("send register code error !：" + js.getString("msg"));
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


    private void countDown() {
        new Thread() {
            public void run() {
                super.run();
                try {
                    for (int i = 60; i >= 0; i--) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = i;
                        handler.sendMessage(msg);
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String count = msg.obj.toString();
                    if (count.equals("0")) {
                        sendCodeBt.setEnabled(true);
                        sendCodeBt.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_all_strokeyellow));
                        sendCodeBt.setText("获取验证码");
                    } else {
                        sendCodeBt.setEnabled(false);
                        sendCodeBt.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect));
                        sendCodeBt.setText("重新发送(" + count + ")");
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        titleTxt.setText("注册");
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
