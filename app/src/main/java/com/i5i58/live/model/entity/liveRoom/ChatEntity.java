package com.i5i58.live.model.entity.liveRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ying on 2016/2/24.
 */
public class ChatEntity implements Parcelable {


    private int mType;
    private String mText;
    private int mPic;

    private String iconUrl = "";
    private String name = "";
    private String content;
    private String time;
    private String accId;
    private int point;

    private int index;

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmPic() {
        return mPic;
    }

    public void setmPic(int mPic) {
        this.mPic = mPic;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addPoint(){
        this.point++;
    }

    public void clranPoint(){
        this.point = 0;
    }

    public void replaceContent(String content){
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mText);
        dest.writeInt(this.mPic);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.accId);
        dest.writeInt(this.point);
        dest.writeInt(this.index);
    }

    public ChatEntity() {
    }

    protected ChatEntity(Parcel in) {
        this.mType = in.readInt();
        this.mText = in.readString();
        this.mPic = in.readInt();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.accId = in.readString();
        this.point = in.readInt();
        this.index = in.readInt();
    }

    public static final Parcelable.Creator<ChatEntity> CREATOR = new Parcelable.Creator<ChatEntity>() {
        @Override
        public ChatEntity createFromParcel(Parcel source) {
            return new ChatEntity(source);
        }

        @Override
        public ChatEntity[] newArray(int size) {
            return new ChatEntity[size];
        }
    };
}
