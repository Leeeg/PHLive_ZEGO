package com.i5i58.live.model.entity.liveRoom;

import android.os.Parcel;
import android.os.Parcelable;

import com.i5i58.live.model.entity.account.Account;

/**
 * Created by Lee on 2017/5/2.
 */

public class TaInfo implements Parcelable {


    /**
     * clubPersonalScore : 0
     * followCount : 10
     * clubScore : 9547
     * clubLevel : 1
     * guard : 3
     * clubName : 爱飞飞
     * clubTitle : 初出茅庐
     * fansCount : 29
     * channelId : 10990241
     * cId : c36d4913c2d54515ae95bc90de0fc89f
     */

    private int clubPersonalScore;
    private int followCount;
    private int clubScore;
    private int clubLevel;
    private int guard;
    private String clubName;
    private String clubTitle;
    private int fansCount;
    private String channelId;
    private String cId;
    private Account account;

    public int getClubPersonalScore() {
        return clubPersonalScore;
    }

    public void setClubPersonalScore(int clubPersonalScore) {
        this.clubPersonalScore = clubPersonalScore;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getClubScore() {
        return clubScore;
    }

    public void setClubScore(int clubScore) {
        this.clubScore = clubScore;
    }

    public int getClubLevel() {
        return clubLevel;
    }

    public void setClubLevel(int clubLevel) {
        this.clubLevel = clubLevel;
    }

    public int getGuard() {
        return guard;
    }

    public void setGuard(int guard) {
        this.guard = guard;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubTitle() {
        return clubTitle;
    }

    public void setClubTitle(String clubTitle) {
        this.clubTitle = clubTitle;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
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
        dest.writeInt(this.clubPersonalScore);
        dest.writeInt(this.followCount);
        dest.writeInt(this.clubScore);
        dest.writeInt(this.clubLevel);
        dest.writeInt(this.guard);
        dest.writeString(this.clubName);
        dest.writeString(this.clubTitle);
        dest.writeInt(this.fansCount);
        dest.writeString(this.channelId);
        dest.writeString(this.cId);
        dest.writeParcelable(this.account, flags);
    }

    public TaInfo() {
    }

    protected TaInfo(Parcel in) {
        this.clubPersonalScore = in.readInt();
        this.followCount = in.readInt();
        this.clubScore = in.readInt();
        this.clubLevel = in.readInt();
        this.guard = in.readInt();
        this.clubName = in.readString();
        this.clubTitle = in.readString();
        this.fansCount = in.readInt();
        this.channelId = in.readString();
        this.cId = in.readString();
        this.account = in.readParcelable(Account.class.getClassLoader());
    }

    public static final Parcelable.Creator<TaInfo> CREATOR = new Parcelable.Creator<TaInfo>() {
        @Override
        public TaInfo createFromParcel(Parcel source) {
            return new TaInfo(source);
        }

        @Override
        public TaInfo[] newArray(int size) {
            return new TaInfo[size];
        }
    };
}
