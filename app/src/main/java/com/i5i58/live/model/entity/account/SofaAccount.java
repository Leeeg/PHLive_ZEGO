package com.i5i58.live.model.entity.account;

/**
 * Created by Lee on 2017/4/28.
 */

public class SofaAccount {

    /**
     * cId : c36d4913c2d54515ae95bc90de0fc89f
     * accId : 6f95731c0569462eb9905a4781134d16
     * name : YUOHE
     * faceSmallUrl : http://q.qlogo.cn/qqapp/1105906694/8212117E31FD31D6AEE426C5B834A579/100
     * vip : 1
     * vipDeadLine : 1494864000000
     * guardLevel : 3
     * guardDeadLine : 1523894400000
     * richScore : 455800
     * fansClub : 1
     * clubName : 啊啊啊
     * clubLevel : 1
     * clubDeadLine : 1492414431017
     */

    private String cId;
    private String accId;
    private String name;
    private String faceSmallUrl;
    private int vip;
    private long vipDeadLine;
    private int guardLevel;
    private long guardDeadLine;
    private int richScore;
    private int fansClub;
    private String clubName;
    private int clubLevel;
    private long clubDeadLine;


    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

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

    public long getVipDeadLine() {
        return vipDeadLine;
    }

    public void setVipDeadLine(long vipDeadLine) {
        this.vipDeadLine = vipDeadLine;
    }

    public int getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(int guardLevel) {
        this.guardLevel = guardLevel;
    }

    public long getGuardDeadLine() {
        return guardDeadLine;
    }

    public void setGuardDeadLine(long guardDeadLine) {
        this.guardDeadLine = guardDeadLine;
    }

    public int getRichScore() {
        return richScore;
    }

    public void setRichScore(int richScore) {
        this.richScore = richScore;
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

    public int getClubLevel() {
        return clubLevel;
    }

    public void setClubLevel(int clubLevel) {
        this.clubLevel = clubLevel;
    }

    public long getClubDeadLine() {
        return clubDeadLine;
    }

    public void setClubDeadLine(long clubDeadLine) {
        this.clubDeadLine = clubDeadLine;
    }
}
