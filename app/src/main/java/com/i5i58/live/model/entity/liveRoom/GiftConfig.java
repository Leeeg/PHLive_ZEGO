package com.i5i58.live.model.entity.liveRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/27.
 */

public class GiftConfig implements Parcelable {

    /**
     * name : 胖虎
     * price : 10
     * anchorPrice : 5
     * unit : 只
     * maxCount : 1314
     * version : 215e6014e19a46ec99b69c6bd3926861
     * path : 6c4f680d31c3b84bbc79f8c1c4ca8c8b
     * function :
     * Node : 1
     * flashPath :
     * sortId : 1
     * id : 1
     */

    private String name;
    private int price;
    private int anchorPrice;
    private String unit;
    private int maxCount;
    private String version;
    private String path;
    private boolean forGuard;
    private boolean forVip;
    private String function;
    private int node;
    private String flashPath;
    private int sortId;
    private int id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAnchorPrice() {
        return anchorPrice;
    }

    public void setAnchorPrice(int anchorPrice) {
        this.anchorPrice = anchorPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getFlashPath() {
        return flashPath;
    }

    public void setFlashPath(String flashPath) {
        this.flashPath = flashPath;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isForGuard() {
        return forGuard;
    }

    public void setForGuard(boolean forGuard) {
        this.forGuard = forGuard;
    }

    public boolean isForVip() {
        return forVip;
    }

    public void setForVip(boolean forVip) {
        this.forVip = forVip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.price);
        dest.writeInt(this.anchorPrice);
        dest.writeString(this.unit);
        dest.writeInt(this.maxCount);
        dest.writeString(this.version);
        dest.writeString(this.path);
        dest.writeByte(this.forGuard ? (byte) 1 : (byte) 0);
        dest.writeByte(this.forVip ? (byte) 1 : (byte) 0);
        dest.writeString(this.function);
        dest.writeInt(this.node);
        dest.writeString(this.flashPath);
        dest.writeInt(this.sortId);
        dest.writeInt(this.id);
    }

    public GiftConfig() {
    }

    protected GiftConfig(Parcel in) {
        this.name = in.readString();
        this.price = in.readInt();
        this.anchorPrice = in.readInt();
        this.unit = in.readString();
        this.maxCount = in.readInt();
        this.version = in.readString();
        this.path = in.readString();
        this.forGuard = in.readByte() != 0;
        this.forVip = in.readByte() != 0;
        this.function = in.readString();
        this.node = in.readInt();
        this.flashPath = in.readString();
        this.sortId = in.readInt();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<GiftConfig> CREATOR = new Parcelable.Creator<GiftConfig>() {
        @Override
        public GiftConfig createFromParcel(Parcel source) {
            return new GiftConfig(source);
        }

        @Override
        public GiftConfig[] newArray(int size) {
            return new GiftConfig[size];
        }
    };
}
