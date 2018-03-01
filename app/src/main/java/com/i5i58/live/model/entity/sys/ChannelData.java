package com.i5i58.live.model.entity.sys;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/17.
 */

public class ChannelData implements Parcelable {



        /**
         * channelId : 10816337
         * ownerId : d9e40973589a42289d54bdb8d25f73fa
         * channelName : Frank
         * type : 2
         * status : 2
         * coverUrl : 02104185-f03c-4518-af2a-5995a3eff042
         * gId : aca7947fdaf64742aa1415be59b743af
         * channelNotice : 环境啊第三个分很高杀人犯
         * yunXinCId : 5aabfef61448410a83d463d9227be782
         * yunXinRId : 6623785
         * pushDevice : 3
         * httpPullUrl : http://v96340cea.live.126.net/live/5aabfef61448410a83d463d9227be782.flv?netease=v96340cea.live.126.net
         * hlsPullUrl : http://pullhls96340cea.live.126.net/live/5aabfef61448410a83d463d9227be782/playlist.m3u8
         * rtmpPullUrl : rtmp://v96340cea.live.126.net/live/5aabfef61448410a83d463d9227be782
         * location : {"state":"浙江","city":"杭州"}
         * playerCount : 260
         * playerTimes : 1125
         * weekOffer : 151690
         * title : 每天都那么美好
         * clubName : 雄霸团
         * clubScore : 4231
         * clubLevel : 1
         * clubTitle : 初出茅庐
         * clubIcon : 4b186eee-ed9a-4ed4-97f3-722b98fa38f2
         * clubMemberCount : 18
         * cId : 4158bd2d6b0a46c6890d175851e750b8
         */

        private String channelId;
        private String ownerId;
        private String channelName;
        private int type;
        private int status;
        private String coverUrl;
        private String gId;
        private String channelNotice;
        private String yunXinCId;
        private String yunXinRId;
        private int pushDevice;
        private String httpPullUrl;
        private String hlsPullUrl;
        private String rtmpPullUrl;
        private String location;
        private int playerCount;
        private int playerTimes;
        private int weekOffer;
        private String title;
        private String clubName;
        private int clubScore;
        private int clubLevel;
        private String clubTitle;
        private String clubIcon;
        private int clubMemberCount;
        private String cId;
        private String ownerName;

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getGId() {
            return gId;
        }

        public void setGId(String gId) {
            this.gId = gId;
        }

        public String getChannelNotice() {
            return channelNotice;
        }

        public void setChannelNotice(String channelNotice) {
            this.channelNotice = channelNotice;
        }

        public String getYunXinCId() {
            return yunXinCId;
        }

        public void setYunXinCId(String yunXinCId) {
            this.yunXinCId = yunXinCId;
        }

        public String getYunXinRId() {
            return yunXinRId;
        }

        public void setYunXinRId(String yunXinRId) {
            this.yunXinRId = yunXinRId;
        }

        public int getPushDevice() {
            return pushDevice;
        }

        public void setPushDevice(int pushDevice) {
            this.pushDevice = pushDevice;
        }

        public String getHttpPullUrl() {
            return httpPullUrl;
        }

        public void setHttpPullUrl(String httpPullUrl) {
            this.httpPullUrl = httpPullUrl;
        }

        public String getHlsPullUrl() {
            return hlsPullUrl;
        }

        public void setHlsPullUrl(String hlsPullUrl) {
            this.hlsPullUrl = hlsPullUrl;
        }

        public String getRtmpPullUrl() {
            return rtmpPullUrl;
        }

        public void setRtmpPullUrl(String rtmpPullUrl) {
            this.rtmpPullUrl = rtmpPullUrl;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getPlayerCount() {
            return playerCount;
        }

        public void setPlayerCount(int playerCount) {
            this.playerCount = playerCount;
        }

        public int getPlayerTimes() {
            return playerTimes;
        }

        public void setPlayerTimes(int playerTimes) {
            this.playerTimes = playerTimes;
        }

        public int getWeekOffer() {
            return weekOffer;
        }

        public void setWeekOffer(int weekOffer) {
            this.weekOffer = weekOffer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClubName() {
            return clubName;
        }

        public void setClubName(String clubName) {
            this.clubName = clubName;
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

        public String getClubTitle() {
            return clubTitle;
        }

        public void setClubTitle(String clubTitle) {
            this.clubTitle = clubTitle;
        }

        public String getClubIcon() {
            return clubIcon;
        }

        public void setClubIcon(String clubIcon) {
            this.clubIcon = clubIcon;
        }

        public int getClubMemberCount() {
            return clubMemberCount;
        }

        public void setClubMemberCount(int clubMemberCount) {
            this.clubMemberCount = clubMemberCount;
        }

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelId);
        dest.writeString(this.ownerId);
        dest.writeString(this.channelName);
        dest.writeInt(this.type);
        dest.writeInt(this.status);
        dest.writeString(this.coverUrl);
        dest.writeString(this.gId);
        dest.writeString(this.channelNotice);
        dest.writeString(this.yunXinCId);
        dest.writeString(this.yunXinRId);
        dest.writeInt(this.pushDevice);
        dest.writeString(this.httpPullUrl);
        dest.writeString(this.hlsPullUrl);
        dest.writeString(this.rtmpPullUrl);
        dest.writeString(this.location);
        dest.writeInt(this.playerCount);
        dest.writeInt(this.playerTimes);
        dest.writeInt(this.weekOffer);
        dest.writeString(this.title);
        dest.writeString(this.clubName);
        dest.writeInt(this.clubScore);
        dest.writeInt(this.clubLevel);
        dest.writeString(this.clubTitle);
        dest.writeString(this.clubIcon);
        dest.writeInt(this.clubMemberCount);
        dest.writeString(this.cId);
    }

    public ChannelData() {
    }

    protected ChannelData(Parcel in) {
        this.channelId = in.readString();
        this.ownerId = in.readString();
        this.channelName = in.readString();
        this.type = in.readInt();
        this.status = in.readInt();
        this.coverUrl = in.readString();
        this.gId = in.readString();
        this.channelNotice = in.readString();
        this.yunXinCId = in.readString();
        this.yunXinRId = in.readString();
        this.pushDevice = in.readInt();
        this.httpPullUrl = in.readString();
        this.hlsPullUrl = in.readString();
        this.rtmpPullUrl = in.readString();
        this.location = in.readString();
        this.playerCount = in.readInt();
        this.playerTimes = in.readInt();
        this.weekOffer = in.readInt();
        this.title = in.readString();
        this.clubName = in.readString();
        this.clubScore = in.readInt();
        this.clubLevel = in.readInt();
        this.clubTitle = in.readString();
        this.clubIcon = in.readString();
        this.clubMemberCount = in.readInt();
        this.cId = in.readString();
    }

    public static final Parcelable.Creator<ChannelData> CREATOR = new Parcelable.Creator<ChannelData>() {
        @Override
        public ChannelData createFromParcel(Parcel source) {
            return new ChannelData(source);
        }

        @Override
        public ChannelData[] newArray(int size) {
            return new ChannelData[size];
        }
    };
}
