package com.i5i58.live.model.entity.GiftModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lee on 2017/4/28.
 */

public class GiftModel  implements Parcelable {

    private String index;
    private String name;
    private String price;
    private String url;
    private String gId;
    private String guard;

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }


    public GiftModel(String index, String name) {
        this.index = index;
        this.name = name;
    }
    public GiftModel(String index, String name, String price, String url,String gId,String guard) {
        this.index = index;
        this.name = name;
        this.price = price;
        this.url = url;
        this.gId = gId;
        this.guard = guard;
    }

    // 重写describeContents方法，内容接口描述，默认返回0就可以，基本不用
    @Override
    public int describeContents() {
        return 0;
    }

    // 重写writeToParcel方法，将你的对象序列化为一个Parcel对象，即：将类的数据写入外部提供的Parcel中，打包需要传递的数据到Parcel容器保存，以便从
    // Parcel容器获取数据
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(index);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(url);
    }

    /**
     * 其中public static
     * final一个都不能少，内部对象CREATOR的名称也不能改变，必须全部大写。需重写本接口中的两个方法：createFromParcel
     * (Parcel in) 实现从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层，newArray(int size)
     * 创建一个类型为T，长度为size的数组，仅一句话即可（return new T[size]），供外部类反序列化本类数组使用。
     */
    public static final Parcelable.Creator<GiftModel> CREATOR = new Parcelable.Creator<GiftModel>() {

        @Override
        public GiftModel createFromParcel(Parcel source) {
            return new GiftModel(source.readString(), source.readString());
        }

        @Override
        public GiftModel[] newArray(int size) {
            return new GiftModel[size];
        }

    };

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getgId() {
        return gId;
    }
    public void setgId(String gId) {
        this.gId = gId;
    }

}
