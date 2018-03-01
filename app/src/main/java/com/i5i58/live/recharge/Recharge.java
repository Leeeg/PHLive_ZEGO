package com.i5i58.live.recharge;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.imageVIew.RoundImageView;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.recharge)
public class Recharge extends BaseActivity {

    @ViewInject(R.id.table_top_tv_title)
    private TextView title;

    @ViewInject(R.id.tv_name)
    private TextView nameTXt;

    @ViewInject(R.id.tv_wallet)
    private TextView walletTxt;

    @ViewInject(R.id.iv_persion_image)
    private RoundImageView iconImg;

    @Event(R.id.table_top_fl_back)
    private void onBackClick(View view){
        closeAct(this);
    }

    private String payType;
    private ListView list_recharge;
    private RechargeListviewAdapter rechargeListviewAdapter;
    private List<String> moneyList = new ArrayList();
    private List<String> donationList = new ArrayList();
    private PopupWindow popWindow;
    private IWXAPI api;
    private String APP_ID = "wx880fa74e302e49c6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        initView();
        getWallet();
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
//        if (TigerApplication.getInstance().inMainProcess()) {
//            NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
//                @Override
//                public void onEvent(CustomNotification message) {
//                    LogUtil.d("支付回调");
//                    // 在这里处理自定义通知。
//                    rechargeOverPopwindow(message.getApnsText());
//                }
//            }, true);
//        }
    }

    private void initView() {
        title.setText("充值ֵ");

        String url = SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl();
        if (null != url) {
            Glide.with(this).load(StringUtil.checkIcon(url))
                    .placeholder(R.drawable.icon_mine_personal_default)
                    .error(R.drawable.icon_mine_personal_default)
                    .into(iconImg);
        }
        String name = SystemCache.getPersonalMsg().getAccount().getName();
        if (null != name) {
            nameTXt.setText(name);
        }

        rechargeListviewAdapter = new RechargeListviewAdapter(getMoney(), getDonation(), this);
        list_recharge = find(R.id.list_recharge);
        list_recharge.setAdapter(rechargeListviewAdapter);
        list_recharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                payWindow(moneyList.get(position));
            }
        });
    }

    private void createOrder(String shareId, String toAccId, String orderAmount, String payAmount) {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.CREATORDER)
                .addParam("shareId", shareId)
                .addParam("toAccId", toAccId)
                .addParam("orderAmount", orderAmount)
                .addParam("payAmount", payAmount)
                .addParam("serialNum", orderAmount)
                .addParam("device", SystemCache.DEVICE)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (payType.equals("8")) {
                                    JSONObject data = js.getJSONObject("data");
                                    aliPay(data.getString("orderString"));
                                } else if (payType.equals("10")){
                                    JSONObject data = js.getJSONObject("data");
                                    JSONObject wxpay = data.getJSONObject("wxpay");
                                    PayReq request = new PayReq();
                                    request.appId           = wxpay.getString("appid");
                                    request.partnerId       = wxpay.getString("partnerid");
                                    request.prepayId        = wxpay.getString("prepayid");
                                    request.packageValue    = wxpay.getString("package");
                                    request.nonceStr        = wxpay.getString("noncestr");
                                    request.timeStamp       = wxpay.getString("timestamp");
                                    request.sign            = wxpay.getString("sign");
                                    api.sendReq(request);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getWallet() {
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETWALLET)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                String iGold = js.getJSONObject("data").getString("iGold");
                                if (null == iGold) return;
                                double iGoldF = Double.valueOf(iGold) / 100;
                                iGold = String.valueOf(iGoldF);
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith("0")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
                                if(iGold.endsWith(".")){
                                    iGold = iGold.substring(0,iGold.length()-1);
                                }
//                                }
                                walletTxt.setText(iGold);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //支付宝
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(Recharge.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map result = (Map) msg.obj;
            TSBSuccess(result.get("memo").toString());
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String respCode = data.getExtras().getString("respCode");
        String respMsg = data.getExtras().getString("respMsg");
        if (null == respCode) {
            Toast.makeText(Recharge.this, "未支付", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        StringBuilder temp = new StringBuilder();
        if (respCode.equals("00")) {
            temp.append("交易状态:成功");
        }

        if (respCode.equals("02")) {
            temp.append("交易状态:取消");
        }

        if (respCode.equals("01")) {
            temp.append("交易状态:失败").append("\n").append("原因:" + respMsg);
        }

        if (respCode.equals("03")) {
            temp.append("交易状态:未知").append("\n").append("原因:" + respMsg);
        }
        builder.setMessage(temp.toString());
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private List getMoney() {
        moneyList.clear();
        moneyList.add("6");
        moneyList.add("30");
        moneyList.add("98");
        moneyList.add("298");
        moneyList.add("568");
        moneyList.add("1598");
        moneyList.add("3000");
        moneyList.add("5000");
        moneyList.add("10000");
        moneyList.add("20000");
        return moneyList;
    }

    private List getDonation() {
        donationList.clear();
        donationList.add("送30钻石6000欢乐豆");
        donationList.add("送150钻石30000欢乐豆");
        donationList.add("送490钻石98000欢乐豆");
        donationList.add("送1490钻石298000欢乐豆");
        donationList.add("送2840钻石568000欢乐豆");
        donationList.add("送7990钻石1598000欢乐豆");
        donationList.add("送15000钻石3000000欢乐豆");
        donationList.add("送25000钻石5000000欢乐豆");
        donationList.add("送50000钻石10000000欢乐豆");
        donationList.add("送100000钻石20000000欢乐豆");
        return donationList;
    }

    /**
     * 支付PopWindow
     */
    private void payWindow(final String much) {
        payType = "8";
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.recharge_pop, null);
        View login_close = popView.findViewById(R.id.login_close);
        final TextView bt_alpay = (TextView) popView.findViewById(R.id.bt_alpay);
        final TextView bt_weichatpay = (TextView) popView.findViewById(R.id.bt_weichatpay);
        TextView bt_recharge = (TextView) popView.findViewById(R.id.bt_recharge);
        TextView tv_needypay = (TextView) popView.findViewById(R.id.tv_needpay);
        TextView tv_reallypay = (TextView) popView.findViewById(R.id.tv_reallypay);
        tv_needypay.setText("需支付:¥ " + much);
        tv_reallypay.setText("¥ " + much);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白消失
        OnClickListener listener = new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_alpay:
                        bt_weichatpay.setBackground(getResources().getDrawable(R.drawable.yuanjiao_white));
                        bt_alpay.setBackground(getResources().getDrawable(R.drawable.shape_recharge));
                        break;
                    case R.id.bt_weichatpay:
                        payType = "10";
                        bt_alpay.setBackground(getResources().getDrawable(R.drawable.yuanjiao_white));
                        bt_weichatpay.setBackground(getResources().getDrawable(R.drawable.shape_recharge));
                        break;
                    case R.id.bt_recharge:
                        createOrder(payType, UserPreferences.getAccId(), much, much);
                        popWindow.dismiss();
                        break;
                    case R.id.login_close:
                        popWindow.dismiss();
                        WindowManager.LayoutParams lp2 = getWindow().getAttributes();
                        lp2.alpha = 1f;
                        getWindow().setAttributes(lp2);
                        break;
                    default:
                        break;
                }
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
        bt_alpay.setOnClickListener(listener);
        bt_weichatpay.setOnClickListener(listener);
        bt_recharge.setOnClickListener(listener);
        login_close.setOnClickListener(listener);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 充值成功Popwindow
     */
    public void rechargeOverPopwindow(String str) {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.recharge_success_pop, null);

        TextView msg = (TextView) popView.findViewById(R.id.msg);
        msg.setText(str);
        TextView recharge_tv_ok = (TextView) popView.findViewById(R.id.recharge_tv_ok);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                popWindow.dismiss();
                getWallet();
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

        recharge_tv_ok.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


}
