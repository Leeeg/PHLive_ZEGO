package com.i5i58.live.model.entity.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/18.
 */

public class Account implements Parcelable {



        /**
         * openId : 10830617
         * name : Lee~
         * stageName : Booty booty
         * anchor : true
         * birthDate : 1370016000000
         * faceSmallUrl : c30a47cb-4267-4b4a-9ddc-37b13070999f.png
         * faceOrgUrl : null
         * version : 34
         * vip : 3
         * vipDeadLine : 1523721600000
         * score : 1210
         * richScore : 38135460
         * mountsId : 305
         * mountsName : 兰博基尼
         * clubCId : 0248bad961fc455492bc66dea85f28a1
         * clubName : 爱飞飞
         * location : {"state":"上海","city":"奉贤区"}
         * signature : null
         * personalBrief : 给G8个他
         * faceUseInGame : true
         * accId : 786e0d37aa0d4ec8af9a61c6d80455bc
         */

        private String openId;
        private String name;
        private String stageName;
        private boolean anchor;
        private long birthDate;
        private String faceSmallUrl;
        private String faceOrgUrl;
        private int version;
        private int vip;
        private long vipDeadLine;
        private int score = 0;
        private int richScore;
        private int mountsId;
        private String mountsName;
        private String clubCId;
        private String clubName;
        private String location;
        private String signature;
        private String personalBrief;
        private boolean faceUseInGame;
        private String accId;
        private int gender;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public boolean isAnchor() {
        return anchor;
    }

    public void setAnchor(boolean anchor) {
        this.anchor = anchor;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getFaceSmallUrl() {
        return faceSmallUrl;
    }

    public void setFaceSmallUrl(String faceSmallUrl) {
        this.faceSmallUrl = faceSmallUrl;
    }

    public String getFaceOrgUrl() {
        return faceOrgUrl;
    }

    public void setFaceOrgUrl(String faceOrgUrl) {
        this.faceOrgUrl = faceOrgUrl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public long getVipDeadLine() {
        return vipDeadLine;
    }

    public void setVipDeadLine(long vipDeadLine) {
        this.vipDeadLine = vipDeadLine;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRichScore() {
        return richScore;
    }

    public void setRichScore(int richScore) {
        this.richScore = richScore;
    }

    public int getMountsId() {
        return mountsId;
    }

    public void setMountsId(int mountsId) {
        this.mountsId = mountsId;
    }

    public String getMountsName() {
        return mountsName;
    }

    public void setMountsName(String mountsName) {
        this.mountsName = mountsName;
    }

    public String getClubCId() {
        return clubCId;
    }

    public void setClubCId(String clubCId) {
        this.clubCId = clubCId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPersonalBrief() {
        return personalBrief;
    }

    public void setPersonalBrief(String personalBrief) {
        this.personalBrief = personalBrief;
    }

    public boolean isFaceUseInGame() {
        return faceUseInGame;
    }

    public void setFaceUseInGame(boolean faceUseInGame) {
        this.faceUseInGame = faceUseInGame;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.openId);
        dest.writeString(this.name);
        dest.writeString(this.stageName);
        dest.writeByte(this.anchor ? (byte) 1 : (byte) 0);
        dest.writeLong(this.birthDate);
        dest.writeString(this.faceSmallUrl);
        dest.writeString(this.faceOrgUrl);
        dest.writeInt(this.version);
        dest.writeInt(this.vip);
        dest.writeLong(this.vipDeadLine);
        dest.writeInt(this.score);
        dest.writeInt(this.richScore);
        dest.writeInt(this.gender);
        dest.writeInt(this.mountsId);
        dest.writeString(this.mountsName);
        dest.writeString(this.clubCId);
        dest.writeString(this.clubName);
        dest.writeString(this.location);
        dest.writeString(this.signature);
        dest.writeString(this.personalBrief);
        dest.writeByte(this.faceUseInGame ? (byte) 1 : (byte) 0);
        dest.writeString(this.accId);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.openId = in.readString();
        this.name = in.readString();
        this.stageName = in.readString();
        this.anchor = in.readByte() != 0;
        this.birthDate = in.readLong();
        this.faceSmallUrl = in.readString();
        this.faceOrgUrl = in.readString();
        this.version = in.readInt();
        this.vip = in.readInt();
        this.vipDeadLine = in.readLong();
        this.score = in.readInt();
        this.richScore = in.readInt();
        this.mountsId = in.readInt();
        this.gender = in.readInt();
        this.mountsName = in.readString();
        this.clubCId = in.readString();
        this.clubName = in.readString();
        this.location = in.readString();
        this.signature = in.readParcelable(Object.class.getClassLoader());
        this.personalBrief = in.readString();
        this.faceUseInGame = in.readByte() != 0;
        this.accId = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
