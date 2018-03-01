package com.i5i58.live.attachment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;


/**
 * Created by Lee on 2017/3/15.
 */

public class MyCustomAttachParser implements MsgAttachmentParser {

    private static final String MAIN = "main";
    private static final String SUB = "sub";

    @Override
    public MsgAttachment parse(String json) {
        MyCustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            String main = object.getString(MAIN);
            String sub = object.getString(SUB);
            JSONObject data = object.getJSONObject("data");
            switch (main) {
                case "channel":
                    switch (sub){
                        case "livep2p":
                            attachment = new SingleChatAttachment();
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

        }

        return attachment;
    }

    public static String packData(String main, String sub, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(SUB,sub);
        object.put(MAIN,main);
        if (data != null) {
            object.put("data",data);
        }
        return object.toJSONString();
    }
}

