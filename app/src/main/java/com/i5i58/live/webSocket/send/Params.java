package com.i5i58.live.webSocket.send;

/**
 * Created by Lee on 2017/4/26.
 */


public class Params{

    private String accId;
    private String token;
    private String device;
    private String cId;
    private String giftVersion;
    private String mountVersion;
    private String animationVersion;
    private String serialNum;

    public String getSerialNum() {
        return serialNum;
    }

    public Params setSerialNum(String serialNum) {
        this.serialNum = serialNum;
        return this;
    }

    public String getAccId() {
        return accId;
    }

    public Params setAccId(String accId) {
        this.accId = accId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Params setToken(String token) {
        this.token = token;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public Params setDevice(String device) {
        this.device = device;
        return this;
    }

    public String getcId() {
        return cId;
    }

    public Params setcId(String cId) {
        this.cId = cId;
        return this;
    }

    public String getGiftVersion() {
        return giftVersion;
    }

    public Params setGiftVersion(String giftVersion) {
        this.giftVersion = giftVersion;
        return this;
    }

    public String getMountVersion() {
        return mountVersion;
    }

    public Params setMountVersion(String mountVersion) {
        this.mountVersion = mountVersion;
        return this;
    }

    public String getAnimationVersion() {
        return animationVersion;
    }

    public Params setAnimationVersion(String animationVersion) {
        this.animationVersion = animationVersion;
        return this;
    }
}