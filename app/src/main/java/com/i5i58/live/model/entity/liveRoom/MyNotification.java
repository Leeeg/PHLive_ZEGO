package com.i5i58.live.model.entity.liveRoom;

import com.google.gson.Gson;

/**
 * Created by Lee on 2017/7/23.
 */

public class MyNotification {

    /**
     * coverUrl : 9bfc353d-32de-4fa5-8c32-30e200ccddbb
     * playerTimes : 9414
     * owner : 718ea9caa00247a9863e9c6dc277eda9
     * httpPullUrl : http://v96340cea.live.126.net/live/57878ccb0c2646de911055068402f1c7.flv?netease=v96340cea.live.126.net
     * ownerName : 火凤燎原
     * yunXinRId : 9636155
     * playerCount : 382
     * channelName : GLHF
     * channelId : 11866485
     * cId : 36b57317b865431ea0385591e9bf5096
     */

    private String coverUrl;
    private String playerTimes;
    private String owner;
    private String httpPullUrl;
    private String ownerName;
    private String yunXinRId;
    private String playerCount;
    private String channelName;
    private String channelId;
    private String cId;

    public static MyNotification objectFromData(String str) {

        return new Gson().fromJson(str, MyNotification.class);
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPlayerTimes() {
        return playerTimes;
    }

    public void setPlayerTimes(String playerTimes) {
        this.playerTimes = playerTimes;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getHttpPullUrl() {
        return httpPullUrl;
    }

    public void setHttpPullUrl(String httpPullUrl) {
        this.httpPullUrl = httpPullUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getYunXinRId() {
        return yunXinRId;
    }

    public void setYunXinRId(String yunXinRId) {
        this.yunXinRId = yunXinRId;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }
}
