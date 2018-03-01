package com.i5i58.live.attachment;

import com.alibaba.fastjson.JSONObject;
import com.i5i58.live.home.helper.LiveRoomCache;

/**
 * Created by Lee on 2017/3/15.
 */

public class SingleChatAttachment extends MyCustomAttachment{

    public String cId;
    public String content;

    public SingleChatAttachment() {

    }

    public SingleChatAttachment(String main, String sub, String content) {
        super(main, sub);
        this.content = content;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.cId = data.getString("cId");
        this.content = data.getString("content");
    }

    @Override
    protected JSONObject packData() {
        try {
            JSONObject data = new JSONObject();
            data.put("cId", LiveRoomCache.getChannelId());
            data.put("content",content);
            return data;
        }catch (NullPointerException e){
            return null;
        }
    }

    public String getcId() {
        return cId;
    }

    public String getContent() {
        return content;
    }
}
