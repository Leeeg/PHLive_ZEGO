package com.i5i58.live.home.helper.channelGift;

import android.os.Looper;
import android.os.Message;

import com.i5i58.live.ZegoAppHelper;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.LinkedList;
import java.util.logging.Handler;

/**
 * Created by admin on 2017/8/17.
 */

public class MessageThread implements Runnable{

    private MessageThreadCallback messageThreadCallback;
    public interface MessageThreadCallback {
        void recharge(String msg);
        void guard(String msg);
    }

    private GiftMsg giftMsg;

    public MessageThread(GiftMsg giftMsg, MessageThreadCallback messageThreadCallback) {
        this.giftMsg = giftMsg;
        this.messageThreadCallback = messageThreadCallback;
    }

    @Override
    public void run() {
        try {
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
                                }else {
                                    String msg = js.getString("msg");
                                    String code = js.getString("code");
                                    switch (code){
                                        case "guard_right":
                                            messageThreadCallback.guard(msg);
                                            break;
                                        case "vip_right":

                                            break;
                                        case "igold_not_enough":
                                            messageThreadCallback.recharge(msg);
                                            break;
                                        default:
                                            break;
                                    }
                                    LogUtil.d("sendGift error ! : " + js);
                                    GiftHelper.removeTask(MessageThread.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
