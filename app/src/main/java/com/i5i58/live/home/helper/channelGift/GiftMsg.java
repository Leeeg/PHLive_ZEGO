package com.i5i58.live.home.helper.channelGift;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/8/17.
 */

public class GiftMsg implements Parcelable {

    private String cId;
    private String giftId;
    private String giftCount;
    private int continuous;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }

    public int getContinuous() {
        return continuous;
    }

    public void setContinuous(int continuous) {
        this.continuous = continuous;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cId);
        dest.writeString(this.giftId);
        dest.writeString(this.giftCount);
        dest.writeInt(this.continuous);
    }

    public GiftMsg() {
    }

    protected GiftMsg(Parcel in) {
        this.cId = in.readString();
        this.giftId = in.readString();
        this.giftCount = in.readString();
        this.continuous = in.readInt();
    }

    public static final Parcelable.Creator<GiftMsg> CREATOR = new Parcelable.Creator<GiftMsg>() {
        @Override
        public GiftMsg createFromParcel(Parcel source) {
            return new GiftMsg(source);
        }

        @Override
        public GiftMsg[] newArray(int size) {
            return new GiftMsg[size];
        }
    };
}
