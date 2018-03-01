package com.i5i58.live.home.helper;

import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.liveRoom.Node;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.webSocket.receive.Channel;
import com.i5i58.live.webSocket.receive.Owner;
import com.i5i58.live.webSocket.receive.Self;

import java.util.Map;

/**
 * Created by Lee on 2017/4/27.
 */

public class LiveRoomCache {

    private static String roomId;//聊天室Id
    private static String liveUrl; // 推流/拉流地址
    private static String channelId; // 频道Id
    private static String coverImg; // 频道封面图
    private static String coverFile;
    private static ChannelData channelData;

    private static Owner owner;
    private static Channel channel;
    private static Self self;

    private static boolean isGiftOpen = true;
    private static boolean isMountOpen = true;


    private static Map<Integer, MountConfig> mountConfigMap;
    private static Map<Integer, GiftConfig> giftConfigMap;
    private static Map<Integer, Node> giftNodeMap;

    public static String getCoverFile() {
        return coverFile;
    }

    public static void setCoverFile(String coverFile) {
        LiveRoomCache.coverFile = coverFile;
    }

    public static ChannelData getChannelData() {
        return channelData;
    }

    public static void setChannelData(ChannelData channelData) {
        LiveRoomCache.channelData = channelData;
    }

    public static boolean isGiftOpen() {
        return isGiftOpen;
    }

    public static void setIsGiftOpen(boolean isGiftOpen) {
        LiveRoomCache.isGiftOpen = isGiftOpen;
    }

    public static boolean isMountOpen() {
        return isMountOpen;
    }

    public static void setIsMountOpen(boolean isMountOpen) {
        LiveRoomCache.isMountOpen = isMountOpen;
    }

    public static String getCoverImg() {
        return coverImg;
    }

    public static void setCoverImg(String coverImg) {
        LiveRoomCache.coverImg = coverImg;
    }

    public static String getRoomId() {
        return roomId;
    }

    public static void setRoomId(String roomId) {
        LiveRoomCache.roomId = roomId;
    }

    public static String getLiveUrl() {
        return liveUrl;
    }

    public static void setLiveUrl(String liveUrl) {
        LiveRoomCache.liveUrl = liveUrl;
    }

    public static String getChannelId() {
        return channelId;
    }

    public static void setChannelId(String channelId) {
        LiveRoomCache.channelId = channelId;
    }

    public static Owner getOwner() {
        return owner;
    }

    public static void setOwner(Owner owner) {
        LiveRoomCache.owner = owner;
    }

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        LiveRoomCache.channel = channel;
    }

    public static Self getSelf() {
        return self;
    }

    public static void setSelf(Self self) {
        LiveRoomCache.self = self;
    }

    public static Map<Integer, MountConfig> getMountConfigMap() {
        return mountConfigMap;
    }

    public static void setMountConfigMap(Map<Integer, MountConfig> mountConfigMap) {
        LiveRoomCache.mountConfigMap = mountConfigMap;
    }

    public static Map<Integer, GiftConfig> getGiftConfigMap() {
        return giftConfigMap;
    }

    public static void setGiftConfigMap(Map<Integer, GiftConfig> giftConfigMap) {
        LiveRoomCache.giftConfigMap = giftConfigMap;
    }

    public static Map<Integer, Node> getGiftNodeMap() {
        return giftNodeMap;
    }

    public static void setGiftNodeMap(Map<Integer, Node> giftNodeMap) {
        LiveRoomCache.giftNodeMap = giftNodeMap;
    }

    public static void clearLiveRoomCache() {
        roomId = null;
        liveUrl = null;
        channelId = null;
        owner = null;
        channel = null;
        self = null;
        isMountOpen = true;
        isGiftOpen = true;
        isMountOpen = true;
        if (null != giftConfigMap) {
            giftConfigMap.clear();
            giftConfigMap = null;
        }
        if (null != mountConfigMap) {
            mountConfigMap.clear();
            mountConfigMap = null;
        }
        if (null != giftNodeMap) {
            giftNodeMap.clear();
            giftNodeMap = null;
        }
    }
}
