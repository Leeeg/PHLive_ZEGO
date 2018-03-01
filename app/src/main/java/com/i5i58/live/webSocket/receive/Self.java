package com.i5i58.live.webSocket.receive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by Lee on 2017/7/23.
 */

public class Self implements Parcelable {

    /**
     * richScore : 86672910
     * clubLevel : 1
     * guard : 3
     * clubName : 冷月心
     * fansClub : 1
     * guardDeadLine : 1502380800000
     * superUser : true
     * score : 3640
     * face : 7d9aadb8-cc09-4f35-a7b0-cb29193f0fbe.png
     * name : 憨厚老实李大大
     * accId : 826e958280d545549faf6982dda0744f
     * fansClubDeadLine : 1502640000000
     * vip : 1
     * vipDeadLine : 1501171200000
     */

    private int richScore;
    private int clubLevel;
    private int guard;
    private String clubName;
    private int fansClub;
    private boolean superUser;
    private int vip;

    private long guardDeadLine;
    private int score;
    private String face;
    private String name;
    private String accId;
    private long fansClubDeadLine;
    private long vipDeadLine;

    public static Self objectFromData(String str) {

        return new Gson().fromJson(str, Self.class);
    }

    public int getRichScore() {
        return richScore;
    }

    public void setRichScore(int richScore) {
        this.richScore = richScore;
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

    public int getFansClub() {
        return fansClub;
    }

    public void setFansClub(int fansClub) {
        this.fansClub = fansClub;
    }

    public long getGuardDeadLine() {
        return guardDeadLine;
    }

    public void setGuardDeadLine(long guardDeadLine) {
        this.guardDeadLine = guardDeadLine;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public long getFansClubDeadLine() {
        return fansClubDeadLine;
    }

    public void setFansClubDeadLine(long fansClubDeadLine) {
        this.fansClubDeadLine = fansClubDeadLine;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.richScore);
        dest.writeInt(this.clubLevel);
        dest.writeInt(this.guard);
        dest.writeString(this.clubName);
        dest.writeInt(this.fansClub);
        dest.writeLong(this.guardDeadLine);
        dest.writeByte(this.superUser ? (byte) 1 : (byte) 0);
        dest.writeInt(this.score);
        dest.writeString(this.face);
        dest.writeString(this.name);
        dest.writeString(this.accId);
        dest.writeLong(this.fansClubDeadLine);
        dest.writeInt(this.vip);
        dest.writeLong(this.vipDeadLine);
    }

    public Self() {
    }

    protected Self(Parcel in) {
        this.richScore = in.readInt();
        this.clubLevel = in.readInt();
        this.guard = in.readInt();
        this.clubName = in.readString();
        this.fansClub = in.readInt();
        this.guardDeadLine = in.readLong();
        this.superUser = in.readByte() != 0;
        this.score = in.readInt();
        this.face = in.readString();
        this.name = in.readString();
        this.accId = in.readString();
        this.fansClubDeadLine = in.readLong();
        this.vip = in.readInt();
        this.vipDeadLine = in.readLong();
    }

    public static final Parcelable.Creator<Self> CREATOR = new Parcelable.Creator<Self>() {
        @Override
        public Self createFromParcel(Parcel source) {
            return new Self(source);
        }

        @Override
        public Self[] newArray(int size) {
            return new Self[size];
        }
    };
}
