package com.i5i58.live.webSocket.receive;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/26.
 */

public class Channel implements Parcelable {

    private String channelId;
    private String ownerId;
    private String channelName;
    private String type;
    private String status;
    private String coverUrl;
    private String gId;
    private String channelNotice;
    private String yunXinCId;
    private String yunXinRId;
    private String pushDevice;
    private String httpPullUrl;
    private String hlsPullUrl;
    private String rtmpPullUrl;
    private String location;
    private String playerCount;
    private String playerTimes;
    private String title;
    private String clubName;
    private String clubScore;
    private String clubLevel;
    private String clubTitle;
    private String clubMemberCount;
    private String cId;
    private String weekOffer = "0";

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getChannelNotice() {
        return channelNotice;
    }

    public void setChannelNotice(String channelNotice) {
        this.channelNotice = channelNotice;
    }

    public String getYunXinCId() {
        return yunXinCId;
    }

    public void setYunXinCId(String yunXinCId) {
        this.yunXinCId = yunXinCId;
    }

    public String getYunXinRId() {
        return yunXinRId;
    }

    public void setYunXinRId(String yunXinRId) {
        this.yunXinRId = yunXinRId;
    }

    public String getPushDevice() {
        return pushDevice;
    }

    public void setPushDevice(String pushDevice) {
        this.pushDevice = pushDevice;
    }

    public String getHttpPullUrl() {
        return httpPullUrl;
    }

    public void setHttpPullUrl(String httpPullUrl) {
        this.httpPullUrl = httpPullUrl;
    }

    public String getHlsPullUrl() {
        return hlsPullUrl;
    }

    public void setHlsPullUrl(String hlsPullUrl) {
        this.hlsPullUrl = hlsPullUrl;
    }

    public String getRtmpPullUrl() {
        return rtmpPullUrl;
    }

    public void setRtmpPullUrl(String rtmpPullUrl) {
        this.rtmpPullUrl = rtmpPullUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public String getPlayerTimes() {
        return playerTimes;
    }

    public void setPlayerTimes(String playerTimes) {
        this.playerTimes = playerTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubScore() {
        return clubScore;
    }

    public void setClubScore(String clubScore) {
        this.clubScore = clubScore;
    }

    public String getClubLevel() {
        return clubLevel;
    }

    public void setClubLevel(String clubLevel) {
        this.clubLevel = clubLevel;
    }

    public String getClubTitle() {
        return clubTitle;
    }

    public void setClubTitle(String clubTitle) {
        this.clubTitle = clubTitle;
    }

    public String getClubMemberCount() {
        return clubMemberCount;
    }

    public void setClubMemberCount(String clubMemberCount) {
        this.clubMemberCount = clubMemberCount;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getWeekOffer() {
        return weekOffer;
    }

    public void setWeekOffer(String weekOffer) {
        this.weekOffer = weekOffer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelId);
        dest.writeString(this.ownerId);
        dest.writeString(this.channelName);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeString(this.coverUrl);
        dest.writeString(this.gId);
        dest.writeString(this.channelNotice);
        dest.writeString(this.yunXinCId);
        dest.writeString(this.yunXinRId);
        dest.writeString(this.pushDevice);
        dest.writeString(this.httpPullUrl);
        dest.writeString(this.hlsPullUrl);
        dest.writeString(this.rtmpPullUrl);
        dest.writeString(this.location);
        dest.writeString(this.playerCount);
        dest.writeString(this.playerTimes);
        dest.writeString(this.title);
        dest.writeString(this.clubName);
        dest.writeString(this.clubScore);
        dest.writeString(this.clubLevel);
        dest.writeString(this.clubTitle);
        dest.writeString(this.clubMemberCount);
        dest.writeString(this.cId);
        dest.writeString(this.weekOffer);
    }

    public Channel() {
    }

    protected Channel(Parcel in) {
        this.channelId = in.readString();
        this.ownerId = in.readString();
        this.channelName = in.readString();
        this.type = in.readString();
        this.status = in.readString();
        this.coverUrl = in.readString();
        this.gId = in.readString();
        this.channelNotice = in.readString();
        this.yunXinCId = in.readString();
        this.yunXinRId = in.readString();
        this.pushDevice = in.readString();
        this.httpPullUrl = in.readString();
        this.hlsPullUrl = in.readString();
        this.rtmpPullUrl = in.readString();
        this.location = in.readString();
        this.playerCount = in.readString();
        this.playerTimes = in.readString();
        this.title = in.readString();
        this.clubName = in.readString();
        this.clubScore = in.readString();
        this.clubLevel = in.readString();
        this.clubTitle = in.readString();
        this.clubMemberCount = in.readString();
        this.cId = in.readString();
        this.weekOffer = in.readString();
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
