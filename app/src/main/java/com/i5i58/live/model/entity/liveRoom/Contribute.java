package com.i5i58.live.model.entity.liveRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/5/3.
 */

public class Contribute implements Parcelable {



    /**
     * accId : d334e270f7cb493f9cbbda5f95c1d5d2
     * offer : 65640
     * name : 你才是笨蛋
     * faceSmallUrl : a76d9331-fa09-4f36-a8da-5ce695032c4f.png
     * vip : 3
     * guardLevel : 1
     * richScore : 2101810
     */

    private String accId;
    private int offer;
    private String name;
    private String faceSmallUrl;
    private int vip;
    private int guardLevel;
    private int richScore;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceSmallUrl() {
        return faceSmallUrl;
    }

    public void setFaceSmallUrl(String faceSmallUrl) {
        this.faceSmallUrl = faceSmallUrl;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(int guardLevel) {
        this.guardLevel = guardLevel;
    }

    public int getRichScore() {
        return richScore;
    }

    public void setRichScore(int richScore) {
        this.richScore = richScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accId);
        dest.writeInt(this.offer);
        dest.writeString(this.name);
        dest.writeString(this.faceSmallUrl);
        dest.writeInt(this.vip);
        dest.writeInt(this.guardLevel);
        dest.writeInt(this.richScore);
    }

    public Contribute() {
    }

    protected Contribute(Parcel in) {
        this.accId = in.readString();
        this.offer = in.readInt();
        this.name = in.readString();
        this.faceSmallUrl = in.readString();
        this.vip = in.readInt();
        this.guardLevel = in.readInt();
        this.richScore = in.readInt();
    }

    public static final Parcelable.Creator<Contribute> CREATOR = new Parcelable.Creator<Contribute>() {
        @Override
        public Contribute createFromParcel(Parcel source) {
            return new Contribute(source);
        }

        @Override
        public Contribute[] newArray(int size) {
            return new Contribute[size];
        }
    };
}
