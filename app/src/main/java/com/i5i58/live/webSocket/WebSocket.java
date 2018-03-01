package com.i5i58.live.webSocket;

import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.ThreadPool;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoad;
import com.i5i58.live.common.utils.downLoad.XUtilsDownLoadCallback;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.webSocket.receive.Channel;
import com.i5i58.live.webSocket.receive.Owner;
import com.i5i58.live.webSocket.receive.Self;
import com.i5i58.live.webSocket.send.Enter;
import com.i5i58.live.webSocket.send.Params;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.net.URI;

public class WebSocket extends WebSocketClient{

    private final String ENTER = "enter";
    private final String GUEST = "guest";
    private final String GIFT = "gift";
    private final String DRIFTCOMMENT = "driftComment";
    private WebSocketCallback webCallback;
    private String roomId;
//    private boolean connected;
    private Enter enter;
    private Params params;

    public interface WebSocketCallback {
        void open(String msg);

        void success();

        void play(boolean play);

        void failed(String failedMsg);

        void lost();

        void welcome(String welcomeMsg);

        void recharge(String msg);

        void guard(String msg);
    }

    public WebSocket(URI serverURI, String roomId, final WebSocketCallback webCallback) {
        super(serverURI, new Draft_17());
        LogUtil.d("WebSocket init!");
        this.roomId = roomId;
        this.webCallback = webCallback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LogUtil.d("webSocket onOpen");
//        connected = true;
        params = new Params()
                .setAccId(UserPreferences.getAccId())
                .setToken(UserPreferences.getToken())
//                .setSerialNum(SystemCache.getSerialNumber())
                .setDevice(SystemCache.DEVICE)
                .setcId(roomId)
                .setGiftVersion(SysPreferences.getGiftVersion())
                .setMountVersion(SysPreferences.getMountVersion())
                .setAnimationVersion(SysPreferences.getAnimationVersion());
        enter = new Enter()
                .setCmd("enter")
                .setParams(params);
        String enterMsg = GsonInner.getGsonInstance().toJson(enter);
        webCallback.open(enterMsg);
    }

//    public boolean isConnected() {
//        return connected;
//    }

    @Override
    public void onMessage(final String message) {
        LogUtil.d("webSocket onMessage");
        ThreadPool.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                receiveMsg(message, webCallback);
            }
        });

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                receiveMsg(message, webCallback);
//            }
//        }.start();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogUtil.d("webSocket onClose");
        if (code != 1000){
            ThreadPool.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        LogUtil.d("webSocket reConnect");
                        Thread.sleep(1000);
//                        connected = false;
                        webCallback.lost();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onError(Exception ex) {
        LogUtil.d("webSocket onError");
        if(!isClosed()){
            this.close();
        }
//        connected = false;
    }

    private void receiveMsg(String message, WebSocketCallback webSocketCallback) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            LogUtil.d("WebSocket receive message !======================================");
            LogUtil.d(jsonObject.toString());
            LogUtil.d("WebSocket receive message !======================================");
            if(!jsonObject.has("code") && jsonObject.getString("cmd").equals("welcome")){//welcome消息
                webSocketCallback.welcome(jsonObject.getString("data"));
                return;
            }
            switch (jsonObject.get("cmd").toString()) {
                case ENTER:
                    if (jsonObject.get("code").equals(ResultCode.SUCCESS.getCode())) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Owner owner = GsonInner.getGsonInstance().fromJson(data.getJSONObject("owner").toString(), Owner.class);
                        Channel channel = GsonInner.getGsonInstance().fromJson(data.getJSONObject("channel").toString(), Channel.class);
                        Self self = GsonInner.getGsonInstance().fromJson(data.getJSONObject("self").toString(), Self.class);
                        LiveRoomCache.setOwner(owner);
                        LiveRoomCache.setChannel(channel);
                        LiveRoomCache.setSelf(self);
                        if (null != channel.getStatus()) {
                            LogUtil.d("channel.getStatus()  :" + channel.getStatus());
                            if (channel.getStatus().equals("1")) {
                                webSocketCallback.play(true);
                            } else {
                                webSocketCallback.play(false);
                            }
                        }
                        webSocketCallback.success();
                        if (owner.isAnchor()){
                            XUtilsDownLoad.downloadOneFile(API.OSS_URL_CHANNELCOVER + channel.getCoverUrl(), SystemCache.BASE_PATH + SystemCache.ROOT_PATH + "FACE.png", new XUtilsDownLoadCallback() {
                                @Override
                                public void success() {
                                    LiveRoomCache.setCoverFile(SystemCache.BASE_PATH + SystemCache.ROOT_PATH + "FACE.png");
                                    LogUtil.d("封面下载成功！");
                                }
                            });
                        }
                    }else {
                        LogUtil.d("webSocket enter channel failed !");
                    }

                    break;

                case GUEST:
                    if (jsonObject.get("code").equals(ResultCode.SUCCESS.getCode())) {

                    }else {
                        LogUtil.d("webSocket guard failed !");
                    }

                    break;

                case GIFT:
                    if (jsonObject.get("code").equals(ResultCode.SUCCESS.getCode())) {

                    }else {
                        String msg = jsonObject.getString("msg");
                        String code = jsonObject.getString("code");
                        switch (code){
                            case "guard_right":
                                webSocketCallback.guard(msg);
                                break;
                            case "vip_right":

                                break;
                            case "igold_not_enough":
                                webSocketCallback.recharge(msg);
                                break;
                            default:
                                webSocketCallback.failed(msg);
                                break;
                        }
//                        if (code.equals("param_invalid")){
//                            webSocketCallback.guard(msg);
//                        }else if (null != code && code.equals("igold_not_enough")){
//                            webSocketCallback.recharge(msg);
//                        }else {
//                            Toast.makeText(TigerApplication.getInstance(), msg + "", Toast.LENGTH_SHORT).show();
//                        }
                        LogUtil.d("webSocket gift failed !");
                    }

                    break;
                case DRIFTCOMMENT:
                    if (jsonObject.get("code").equals(ResultCode.SUCCESS.getCode())) {

                    }else {
                        String msg = jsonObject.getString("msg");
                        String code = jsonObject.getString("code");
                        if (code.equals("param_invalid")){
                            webSocketCallback.recharge(msg);
                        }
                        LogUtil.d("webSocket gift failed !");
                    }

                    break;

                default:
                    break;
            }
//            } else {
//                if (null != jsonObject.getString("msg") && jsonObject.getString("msg").equals("您的虎币不足")){
//                    webSocketCallback.recharge(jsonObject.getString("msg"));
//                }else {
//                    webSocketCallback.failed(jsonObject.get("code").toString());
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("webSocket receive message failed " + e.toString());
        }
    }

}
