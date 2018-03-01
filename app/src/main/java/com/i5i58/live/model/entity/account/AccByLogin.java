package com.i5i58.live.model.entity.account;

/**
 * Created by Lee on 2017/4/12.
 */

public class AccByLogin {

    /**
     * accId : 786e0d37aa0d4ec8af9a61c6d80455bc
     * openId : 10830617
     * phoneNo : 13*******80
     * nickName : Lee~
     * faceSmallUrl : c30a47cb-4267-4b4a-9ddc-37b13070999f.png
     * stageName : Booty booty
     * birthDate : 1370016000000
     * location : {"state":"上海","city":"奉贤区"}
     * personalBrief : 给G8个他
     * version : 34
     * authed : true
     * anchor : true
     * bindMobile : 15084844630
     */

    private String accId;
    private String openId;
    private String phoneNo;
    private String nickName;
    private String faceSmallUrl;
    private String faceOrgUrl;
    private String stageName;
    private long birthDate;
    private String location;
    private String personalBrief;
    private int version;
    private boolean authed;
    private boolean anchor;
    private String bindMobile;

    public String getFaceOrgUrl() {
        return faceOrgUrl;
    }

    public void setFaceOrgUrl(String faceOrgUrl) {
        this.faceOrgUrl = faceOrgUrl;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFaceSmallUrl() {
        return faceSmallUrl;
    }

    public void setFaceSmallUrl(String faceSmallUrl) {
        this.faceSmallUrl = faceSmallUrl;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersonalBrief() {
        return personalBrief;
    }

    public void setPersonalBrief(String personalBrief) {
        this.personalBrief = personalBrief;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public boolean isAnchor() {
        return anchor;
    }

    public void setAnchor(boolean anchor) {
        this.anchor = anchor;
    }

    public String getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
    }

}
