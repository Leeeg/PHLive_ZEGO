package com.i5i58.live.model.entity.sys;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/17.
 */

public class BannerData implements Parcelable {



    private String id;
    private String device;
    private String position;
    private String imgUrl;
    private String action;
    private String params;
    private String startTime;
    private String endTime;
    private String cId;
    private String coverUrl;
    private String hlsPullUrl;
    private String httpPullUrl;
    private String rtmpPullUrl;
    private String yunXinRId;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getHlsPullUrl() {
        return hlsPullUrl;
    }

    public void setHlsPullUrl(String hlsPullUrl) {
        this.hlsPullUrl = hlsPullUrl;
    }

    public String getHttpPullUrl() {
        return httpPullUrl;
    }

    public void setHttpPullUrl(String httpPullUrl) {
        this.httpPullUrl = httpPullUrl;
    }

    public String getRtmpPullUrl() {
        return rtmpPullUrl;
    }

    public void setRtmpPullUrl(String rtmpPullUrl) {
        this.rtmpPullUrl = rtmpPullUrl;
    }

    public String getYunXinRId() {
        return yunXinRId;
    }

    public void setYunXinRId(String yunXinRId) {
        this.yunXinRId = yunXinRId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.device);
        dest.writeString(this.position);
        dest.writeString(this.imgUrl);
        dest.writeString(this.action);
        dest.writeString(this.params);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.cId);
        dest.writeString(this.coverUrl);
        dest.writeString(this.hlsPullUrl);
        dest.writeString(this.httpPullUrl);
        dest.writeString(this.rtmpPullUrl);
        dest.writeString(this.yunXinRId);
    }

    public BannerData() {
    }

    protected BannerData(Parcel in) {
        this.id = in.readString();
        this.device = in.readString();
        this.position = in.readString();
        this.imgUrl = in.readString();
        this.action = in.readString();
        this.params = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.cId = in.readString();
        this.coverUrl = in.readString();
        this.hlsPullUrl = in.readString();
        this.httpPullUrl = in.readString();
        this.rtmpPullUrl = in.readString();
        this.yunXinRId = in.readString();
    }

    public static final Parcelable.Creator<BannerData> CREATOR = new Parcelable.Creator<BannerData>() {
        @Override
        public BannerData createFromParcel(Parcel source) {
            return new BannerData(source);
        }

        @Override
        public BannerData[] newArray(int size) {
            return new BannerData[size];
        }
    };
}
