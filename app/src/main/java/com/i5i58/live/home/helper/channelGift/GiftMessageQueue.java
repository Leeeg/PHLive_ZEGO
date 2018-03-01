package com.i5i58.live.home.helper.channelGift;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.reactNative.MyReactActivity;
import com.i5i58.live.recharge.Recharge;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.concurrent.LinkedBlockingQueue;

public class GiftMessageQueue {

    private Activity activity;
    private String wallet;
    public static LinkedBlockingQueue<GiftMsg> queue;//礼物的队列
    private final static int GET_QUEUE_GIFT = 0;
    private final static int SHOW_GIFT_FLAG = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_QUEUE_GIFT:
                    if (null == queue) return;
                    GiftMsg vo = queue.poll();
                    if (vo != null) {
                        Message giftMsg = new Message();
                        giftMsg.obj = vo;
                        giftMsg.what = SHOW_GIFT_FLAG;
                        handler.sendMessage(giftMsg);
                    } else {
                        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);
                    }
                    break;
                case SHOW_GIFT_FLAG:
                    try {
                        final GiftMsg giftMsg = (GiftMsg) msg.obj;
                        LogUtil.d("thread looping !");
                        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                                .addRouteUrl(API.GIVEGIFT)
                                .addParam("device",     SystemCache.DEVICE)
                                .addParam("cId",        giftMsg.getcId())
                                .addParam("giftId",     giftMsg.getGiftId())
                                .addParam("giftCount",  giftMsg.getGiftCount())
                                .addParam("continuous", String.valueOf(giftMsg.getContinuous()))
                                .getResult(new HttpCallback() {
                                    @Override
                                    @HttpResultCut
                                    public void success(JSONObject js, boolean success) {
                                        try {
                                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                                LogUtil.d("sendGift success !");
                                                handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);
                                            }else {
                                                String msg = js.getString("msg");
                                                String code = js.getString("code");
                                                switch (code){
                                                    case "guard_right":
                                                        Intent intent = new Intent(activity, MyReactActivity.class);
                                                        intent.putExtra("accId", UserPreferences.getAccId());
                                                        intent.putExtra("token", UserPreferences.getToken());
                                                        intent.putExtra("cId", LiveRoomCache.getChannelId());
                                                        MyReactActivity.mainName = "GuardIndex";
                                                        activity.startActivity(intent);
                                                        break;
                                                    case "vip_right":

                                                        break;
                                                    case "igold_not_enough":
                                                        rechargePopWindow();
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                LogUtil.d("sendGift error ! : " + js);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void setWallet(String wallet){
        this.wallet = wallet;
    }

    public GiftMessageQueue(Activity activity) {
        this.activity = activity;
        queue = new LinkedBlockingQueue();
    }

    public void destroyQueue(){
        queue.clear();
        queue = null;
    }

    public void addGift(GiftMsg vo) {
        queue.add(vo);
        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 300);//轮询队列获取礼物
    }

    /**
     * 充值PopWindow
     */
    private void rechargePopWindow() {
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(activity, R.layout.channel_recharge, null);

        TextView recharge_pop_wallet = (TextView) popView.findViewById(R.id.recharge_pop_wallet);
        recharge_pop_wallet.setText("余额：" + wallet + "虎币");

        TextView tv_login = (TextView) popView.findViewById(R.id.tv_recharge);
        View login_close = popView.findViewById(R.id.login_close);

        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白消失
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_recharge:
                        Intent intent = new Intent(activity, Recharge.class);
                        intent.putExtra("Channel", "Channel");
                        activity.startActivity(intent);
                        break;
                    case R.id.login_close:

                        break;

                    default:
                        break;
                }
                popWindow.dismiss();
            }
        };
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });

        tv_login.setOnClickListener(listener);
        login_close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    
}
