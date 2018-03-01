package com.i5i58.live.common.net;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.i5i58.live.TigerApplication;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.manager.APPManager;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.tSnackBar.TSnackbarUtil;
import com.i5i58.live.login.activity.LoginMainActivity;
import com.i5i58.live.main.activity.MainActivity;
import com.i5i58.live.main.activity.WelcomeActivity;
import com.i5i58.live.model.entity.sys.SystemCache;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Lee on 2017/4/7.
 */

public class Http {

    private String rootUrl;
    private String routeUrl;
    private RequestParams params;

    private HttpEnum httpEnum;

    public Http(HttpEnum httpEnum)
    {
        this.httpEnum = httpEnum;
    }

    public Http addRootUrl(String rootUrl)
    {
        this.rootUrl = rootUrl;
        return this;
    }

    public Http addRouteUrl(String routeUrl)
    {
        this.routeUrl = routeUrl;
        params=new RequestParams(rootUrl + routeUrl);
        if (httpEnum == HttpEnum.POST) addHeader();
        return this;
    }

    public Http addRouteUrlWithToken(String routeUrl, String accId, String token)
    {
        this.routeUrl = routeUrl;
        params=new RequestParams(rootUrl + routeUrl);
        params.addHeader("accId", accId);
        params.addHeader("token", token);
        return this;
    }

    private void addHeader()
    {
        LogUtil.d("serialNum" + SystemCache.getSerialNumber());
        LogUtil.d("authorization" + UserPreferences.getToken());
        params.addHeader("accId", UserPreferences.getAccId());
        params.addHeader("token", UserPreferences.getToken());
    }

    public Http addParam(String key, String value)
    {
        if(httpEnum == HttpEnum.GET){
            params.addQueryStringParameter(key, value);
        }else{
            params.addBodyParameter(key, value);
        }
        return this;
    }

    private void dialog() {
        final BaseActivity activity = (BaseActivity) APPManager.getInstance().currentActivity();
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(activity).create();
        dialog.setTitle("应用更新");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        float density = activity.getResources().getDisplayMetrics().density;
        TextView tv = new TextView(TigerApplication.getInstance());
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));

        tv.setText("登录状态已失效，请重新登录！");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserPreferences.saveLogin(false);

                Intent broad = new Intent(WelcomeActivity.action);
                activity.sendBroadcast(broad);

                SystemCache.clear();

                Intent intent = new Intent(activity, LoginMainActivity.class);
                activity.startActivity(intent);
                activity.finish();
                APPManager.getInstance().removeClass(MainActivity.class);
            }
        });

        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);
    }

    /**
     * xUtils请求网络Get
     */
    public void getResult(final HttpCallback HttpCallback){
        List list = params.getHeaders();
        if(httpEnum == HttpEnum.GET){
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {//KeyValue{key='giftVersion', value=136eb655b3cb4e9d9b7bc8f327987495}   KeyValue{key='giftVersion', value=99fe2df04eaa45539b78165411ecbd81}
                        JSONObject js = new JSONObject(result);
                        LogUtil.i(result);
                        HttpCallback.success(js, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e(e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (ex instanceof HttpException) {          // 网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        LogUtil.e("responseCode :" + responseCode);
                        LogUtil.e("responseMsg :" + responseMsg);
                        LogUtil.e("errorResult :" + errorResult);
                        LogUtil.e("liveUrl :" + rootUrl + routeUrl);
                        if (responseCode == 401){
                            dialog();
                            HttpCallback.success(new JSONObject(),false);
                        }else {
                            TSnackbarUtil.TSBError(APPManager.getInstance().currentActivity(),"网络错误");
                        }
                    } else {            // 其他错误
                        LogUtil.e(ex.toString());
                        TSnackbarUtil.TSBError(APPManager.getInstance().currentActivity(),"出现其他错误");
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else{
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject js = new JSONObject(result);
                        LogUtil.i(result);
                        HttpCallback.success(js, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e(e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (ex instanceof HttpException) {          // 网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        LogUtil.e("responseCode :" + responseCode);
                        LogUtil.e("responseMsg :" + responseMsg);
                        LogUtil.e("errorResult :" + errorResult);
                        LogUtil.e("liveUrl :" + rootUrl + routeUrl);
                        BaseActivity topActivity = (BaseActivity) APPManager.getInstance().currentActivity();
                        if (responseCode == 401){
                            dialog();
                            HttpCallback.success(new JSONObject(),false);
                        }else {
                            topActivity.TSBError("网络错误：" + responseCode);
                        }
                    } else {            // 其他错误
                        LogUtil.e("未知错误");
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

}