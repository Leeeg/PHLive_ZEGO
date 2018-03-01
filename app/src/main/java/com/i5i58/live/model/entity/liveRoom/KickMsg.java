package com.i5i58.live.model.entity.liveRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/7/11.
 */

public class KickMsg implements Parcelable {



    private String accId = "";
    private String name = "";
    private String face = "";
    private int vip;
    private long vipDeadLine;
    private int guard;
    private long guardDeadLine;
    private long richScore;
    private long score;
    private int fansClub;
    private String clubName = "";
    private long fansClubDeadLine;
    private int clubLevel;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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

    public int getGuard() {
        return guard;
    }

    public void setGuard(int guard) {
        this.guard = guard;
    }

    public long getGuardDeadLine() {
        return guardDeadLine;
    }

    public void setGuardDeadLine(long guardDeadLine) {
        this.guardDeadLine = guardDeadLine;
    }

    public long getRichScore() {
        return richScore;
    }

    public void setRichScore(long richScore) {
        this.richScore = richScore;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getFansClub() {
        return fansClub;
    }

    public void setFansClub(int fansClub) {
        this.fansClub = fansClub;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public long getFansClubDeadLine() {
        return fansClubDeadLine;
    }

    public void setFansClubDeadLine(long fansClubDeadLine) {
        this.fansClubDeadLine = fansClubDeadLine;
    }

    public int getClubLevel() {
        return clubLevel;
    }

    public void setClubLevel(int clubLevel) {
        this.clubLevel = clubLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accId);
        dest.writeString(this.name);
        dest.writeString(this.face);
        dest.writeInt(this.vip);
        dest.writeLong(this.vipDeadLine);
        dest.writeInt(this.guard);
        dest.writeLong(this.guardDeadLine);
        dest.writeLong(this.richScore);
        dest.writeLong(this.score);
        dest.writeInt(this.fansClub);
        dest.writeString(this.clubName);
        dest.writeLong(this.fansClubDeadLine);
        dest.writeInt(this.clubLevel);
    }

    public KickMsg() {
    }

    protected KickMsg(Parcel in) {
        this.accId = in.readString();
        this.name = in.readString();
        this.face = in.readString();
        this.vip = in.readInt();
        this.vipDeadLine = in.readLong();
        this.guard = in.readInt();
        this.guardDeadLine = in.readLong();
        this.richScore = in.readLong();
        this.score = in.readLong();
        this.fansClub = in.readInt();
        this.clubName = in.readString();
        this.fansClubDeadLine = in.readLong();
        this.clubLevel = in.readInt();
    }

    public static final Parcelable.Creator<KickMsg> CREATOR = new Parcelable.Creator<KickMsg>() {
        @Override
        public KickMsg createFromParcel(Parcel source) {
            return new KickMsg(source);
        }

        @Override
        public KickMsg[] newArray(int size) {
            return new KickMsg[size];
        }
    };
}
