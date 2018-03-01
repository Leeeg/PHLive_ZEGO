package com.i5i58.live.home.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.i5i58.live.R;
import com.i5i58.live.home.entity.BroadSideQuickItem;
import com.i5i58.live.model.api.API;

import java.util.List;

/**
 * Created by Lee on 2017/5/6.
 */

public class BroadSideQuickAdapter extends BaseQuickAdapter<BroadSideQuickItem> {

    private Context context;

    public BroadSideQuickAdapter(Context context, List data) {
        super(R.layout.item_dfrg_broadside, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BroadSideQuickItem item) {
        Glide.with(context)
                .load(API.OSS_URL_CHANNELCOVER + item.getUrl())
                .asBitmap()
                .placeholder(R.drawable.bg_morefrg_gridview_item_default)
                .error(R.drawable.bg_morefrg_gridview_item_default)
                .into((ImageView) helper.getView(R.id.broad_img));
    }

}
