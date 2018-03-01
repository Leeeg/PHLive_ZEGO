package com.i5i58.live.model.entity.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/18.
 */

public class PersonalMsg implements Parcelable {


    private String clubPersonalScore;
    private String followCount = "0";
    private String clubLevel;
    private String guard;
    private String clubName;
    private String fansCount = "0";
    private String qrCode;
    private String clubScore;
    private String clubTitle;
    private String getDeadLine;
    private String channelId;
    private String cId;
    private Account account;

    public String getClubPersonalScore() {
        return clubPersonalScore;
    }

    public void setClubPersonalScore(String clubPersonalScore) {
        this.clubPersonalScore = clubPersonalScore;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getClubLevel() {
        return clubLevel;
    }

    public void setClubLevel(String clubLevel) {
        this.clubLevel = clubLevel;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getClubScore() {
        return clubScore;
    }

    public void setClubScore(String clubScore) {
        this.clubScore = clubScore;
    }

    public String getClubTitle() {
        return clubTitle;
    }

    public void setClubTitle(String clubTitle) {
        this.clubTitle = clubTitle;
    }

    public String getGetDeadLine() {
        return getDeadLine;
    }

    public void setGetDeadLine(String getDeadLine) {
        this.getDeadLine = getDeadLine;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clubPersonalScore);
        dest.writeString(this.followCount);
        dest.writeString(this.clubLevel);
        dest.writeString(this.guard);
        dest.writeString(this.clubName);
        dest.writeString(this.fansCount);
        dest.writeString(this.qrCode);
        dest.writeString(this.clubScore);
        dest.writeString(this.clubTitle);
        dest.writeString(this.getDeadLine);
        dest.writeString(this.channelId);
        dest.writeString(this.cId);
        dest.writeParcelable(this.account, flags);
    }

    public PersonalMsg() {
    }

    protected PersonalMsg(Parcel in) {
        this.clubPersonalScore = in.readString();
        this.followCount = in.readString();
        this.clubLevel = in.readString();
        this.guard = in.readString();
        this.clubName = in.readString();
        this.fansCount = in.readString();
        this.qrCode = in.readString();
        this.clubScore = in.readString();
        this.clubTitle = in.readString();
        this.getDeadLine = in.readString();
        this.channelId = in.readString();
        this.cId = in.readString();
        this.account = in.readParcelable(Account.class.getClassLoader());
    }

    public static final Parcelable.Creator<PersonalMsg> CREATOR = new Parcelable.Creator<PersonalMsg>() {
        @Override
        public PersonalMsg createFromParcel(Parcel source) {
            return new PersonalMsg(source);
        }

        @Override
        public PersonalMsg[] newArray(int size) {
            return new PersonalMsg[size];
        }
    };
}
