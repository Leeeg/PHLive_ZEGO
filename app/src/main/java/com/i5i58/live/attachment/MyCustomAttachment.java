package com.i5i58.live.attachment;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by Lee on 2017/3/15.
 */

public abstract class MyCustomAttachment implements MsgAttachment {

    private String main;
    private String sub;

    public MyCustomAttachment() {
    }

    public MyCustomAttachment(String main, String sub) {
        this.main = main;
        this.sub = sub;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        return MyCustomAttachParser.packData(main, sub, packData());
    }


    protected abstract void parseData(JSONObject data);

    protected abstract JSONObject packData();

}