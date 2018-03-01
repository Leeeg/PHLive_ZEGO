package com.i5i58.live.home.entity;

import android.graphics.Bitmap;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SingleInfoMultipleItem extends MultiItemEntity {
    public static final int IN = 1;
    public static final int OUT = 2;
    public static final int TIME = 3;

    private String content;
    private Bitmap icon;
    private String iconUrl = "00";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
