package com.i5i58.live.common.utils.json;

import com.google.gson.reflect.TypeToken;
import com.i5i58.live.common.Inners.GsonInner;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.entity.account.SofaAccount;
import com.i5i58.live.model.entity.liveRoom.BroadSideAction;
import com.i5i58.live.model.entity.liveRoom.GiftConfig;
import com.i5i58.live.model.entity.liveRoom.MountConfig;
import com.i5i58.live.model.entity.liveRoom.Node;
import com.i5i58.live.model.entity.sys.BannerData;
import com.i5i58.live.model.entity.sys.ChannelData;
import com.i5i58.live.model.entity.sys.HomeTypeData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/4/7.
 */

public class GsonUtil {

    public static List<HomeTypeData> getHomeTypeList(String jsonData){
        if (null == jsonData || jsonData.isEmpty()){
            return null;
        }
        List<HomeTypeData> list = new ArrayList<>();
        Type listType = new TypeToken<LinkedList<HomeTypeData>>(){}.getType();
        LinkedList<HomeTypeData> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
        for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
            HomeTypeData resource = (HomeTypeData) iterator.next();
            list.add(resource);
        }
        return list;
    }

    public static List<BannerData> getBannerList(String jsonData){
        List<BannerData> list = new ArrayList<>();
        if (null == jsonData || jsonData.isEmpty()){
            return null;
        }
        Type listType = new TypeToken<LinkedList<BannerData>>(){}.getType();
        LinkedList<BannerData> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
        for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
            BannerData resource = (BannerData) iterator.next();
            list.add(resource);
        }
        return list;
    }

    public static List<ChannelData> getChannelList(String jsonData,List<ChannelData> channelDatas){
        if (null != jsonData && !jsonData.isEmpty()){
            Type listType = new TypeToken<LinkedList<ChannelData>>(){}.getType();
            LinkedList<ChannelData> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                ChannelData resource = (ChannelData) iterator.next();
                channelDatas.add(resource);
            }
        }
        return channelDatas;
    }

    public static List<GiftConfig> setGiftConfig(String jsonData){
        if (null != jsonData && !jsonData.isEmpty() && !"-1".equals(jsonData)){
            List<GiftConfig> configList = new ArrayList<>();
            Map<Integer, GiftConfig> giftConfigMap = new HashMap<>();
            Type listType = new TypeToken<LinkedList<GiftConfig>>(){}.getType();
            LinkedList<GiftConfig> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                GiftConfig resource = (GiftConfig) iterator.next();
                configList.add(resource);
                giftConfigMap.put(resource.getId(), resource);
            }
            LiveRoomCache.setGiftConfigMap(giftConfigMap);
            return configList;
        }
        return null;
    }

    public static List<MountConfig> setMountConfig(String jsonData){
        if (null != jsonData && !jsonData.isEmpty() && !"-1".equals(jsonData)){
            List<MountConfig> configList = new ArrayList<>();
            Map<Integer, MountConfig> mountConfigMap = new HashMap<>();
            Type listType = new TypeToken<LinkedList<MountConfig>>(){}.getType();
            LinkedList<MountConfig> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                MountConfig resource = (MountConfig) iterator.next();
                mountConfigMap.put(resource.getId(), resource);
            }
            LiveRoomCache.setMountConfigMap(mountConfigMap);
            return configList;
        }
        return null;
    }

    public static void setGiftNode(String jsonData){
        if (null != jsonData && !jsonData.isEmpty() && !"-1".equals(jsonData)){
            Map<Integer, Node> nodeMap = new HashMap<>();
            Type listType = new TypeToken<LinkedList<Node>>(){}.getType();
            LinkedList<Node> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                Node resource = (Node) iterator.next();
                nodeMap.put(resource.getId(), resource);
            }
            LiveRoomCache.setGiftNodeMap(nodeMap);
        }
    }

    public static List<SofaAccount> getSofaAccountList(String jsonData, List<SofaAccount> sofaList){
        if (null != jsonData && !jsonData.isEmpty() && !"-1".equals(jsonData)){
            Type listType = new TypeToken<LinkedList<SofaAccount>>(){}.getType();
            LinkedList<SofaAccount> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                SofaAccount resource = (SofaAccount) iterator.next();
                sofaList.add(resource);
            }
        }
        return sofaList;
    }

    public static List<BroadSideAction> getBroadSideList(String jsonData, List<BroadSideAction> broadSideActions){
        if (null != jsonData && !jsonData.isEmpty()){
            Type listType = new TypeToken<LinkedList<BroadSideAction>>(){}.getType();
            LinkedList<BroadSideAction> resources = GsonInner.getGsonInstance().fromJson(jsonData, listType);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                BroadSideAction resource = (BroadSideAction) iterator.next();
                broadSideActions.add(resource);
            }
        }
        return broadSideActions;
    }

}
