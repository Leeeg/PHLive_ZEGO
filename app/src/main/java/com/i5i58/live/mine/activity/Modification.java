package com.i5i58.live.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.model.api.API;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Lee on 2017/4/21.
 */

@ContentView(R.layout.act_modification)
public class Modification extends BaseActivity {

    private String title;
    private String hint;
    private final int NICKNAME_REQUEST_CODE   = 101;
    private final int STAGENAME_REQUEST_CODE  = 102;
    private final int SINGURE_REQUEST_CODE    = 103;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.table_top_img_save)
    private TextView saveTxt;

    @ViewInject(R.id.modification_Edit)
    private EditText edit;

    @ViewInject(R.id.noanchor)
    private ImageView onAnchor;

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

    @Event(R.id.table_top_fl_menu)
    private void saveClick(View v){
        final CharSequence textE = edit.getText();
        String text = "";
        if (null != textE) text = textE.toString().trim();
        if (TextUtils.isEmpty(text)){
            TSBError("请输入修改内容");
            return;
        }
        if (text.toString().equals(hint)){
            TSBError("当前没有修改");
            return;
        }
        switch (title){
            case "昵称":
                final String finalText1 = text;
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.NICKNAME)
                        .addParam("nickName", text.toString())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals("success")) {
                                        Intent intent = new Intent();
                                        intent.putExtra("text", finalText1);
                                        setResult(NICKNAME_REQUEST_CODE,intent);
                                        closeAct(Modification.this);
                                    } else {
                                        LogUtil.e("修改昵称出错");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    LogUtil.e(e.toString());
                                } catch (NullPointerException e){
                                    LogUtil.e(e.toString());
                                }
                            }
                        });
                break;
            case "艺名":
                final String finalText = text;
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.STAGENAME)
                        .addParam("stageName", text.toString())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals("success")) {
                                        Intent intent = new Intent();
                                        intent.putExtra("text", finalText);
                                        setResult(STAGENAME_REQUEST_CODE,intent);
                                        closeAct(Modification.this);
                                    } else {
                                        LogUtil.e("艺名昵称出错");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    LogUtil.e(e.toString());
                                } catch (NullPointerException e){
                                    LogUtil.e(e.toString());
                                }
                            }
                        });
                break;
            case "个性签名":
                final String finalText2 = text;
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.SIGNATURE)
                        .addParam("personalBrief", text.toString())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals("success")) {
                                        Intent intent = new Intent();
                                        intent.putExtra("text", finalText2);
                                        setResult(SINGURE_REQUEST_CODE,intent);
                                        closeAct(Modification.this);
                                    } else {
                                        LogUtil.e("修改个性签名出错");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    LogUtil.e(e.toString());
                                } catch (NullPointerException e){
                                    LogUtil.e(e.toString());
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void init() {
        saveTxt.setText("保存");
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        titleTxt.setText(title);
        hint = intent.getStringExtra("hint");
        edit.setText(hint);
        edit.setSelection(hint.length());
        if (title.equals("艺名") && !intent.getBooleanExtra("isAnchor", false)){
            edit.setVisibility(View.GONE);
            saveTxt.setVisibility(View.GONE);
            onAnchor.setVisibility(View.VISIBLE);
        }else {
            saveTxt.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            onAnchor.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        init();
    }
}
