package com.i5i58.live.home.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.i5i58.live.R;
import com.i5i58.live.home.entity.SingleInfoMultipleItem;

import java.util.List;

/**
 * Created by Lee on 2017/5/6.
 */

public class SingleInfoMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<SingleInfoMultipleItem> {

    private Context context;

    public SingleInfoMultipleItemQuickAdapter(Context context, List data) {
        super(data);
        this.context = context;
        addItemType(SingleInfoMultipleItem.IN, R.layout.item_single_info_in);
        addItemType(SingleInfoMultipleItem.OUT, R.layout.item_single_info_out);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingleInfoMultipleItem item) {
        switch (helper.getItemViewType()) {
            case SingleInfoMultipleItem.IN:
                helper.setText(R.id.chat_in_text, item.getContent());
                Glide.with(context)
                        .load(item.getIconUrl())
                        .asBitmap()
                        .into((ImageView) helper.getView(R.id.chat_in_image));
                break;
            case SingleInfoMultipleItem.OUT:
                helper.setText(R.id.chat_out_text, item.getContent());
                Glide.with(context)
                        .load(item.getIconUrl())
                        .asBitmap()
                        .placeholder(R.drawable.icon_mine_personal_default)
                        .error(R.drawable.icon_mine_personal_default)
                        .into((ImageView) helper.getView(R.id.chat_out_image));
                break;
        }
    }

}
