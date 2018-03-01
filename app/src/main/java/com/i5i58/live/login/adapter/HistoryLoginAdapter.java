package com.i5i58.live.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;

import java.util.List;

/**
 * Created by Lee on 2017/4/13.
 */

public class HistoryLoginAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mIconList;
    public boolean canDelete = false;

    public HistoryLoginAdapter(Context context, List iconList){
        this.mContext = context;
        this.mIconList = iconList;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mIconList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_historylogin_hlis, null);
            holder.iconImg = (ImageView)convertView.findViewById(R.id.img_list_item_icon);
            holder.editIbt = (ImageButton) convertView.findViewById(R.id.ibt_list_item_deltet);
            holder.space = convertView.findViewById(R.id.item_loginhistory_v_space);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position != 0){
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.space.getLayoutParams();
            linearParams.width = (SystemCache.getScreenWith() - 650) / 4 ;
            holder.space.setLayoutParams(linearParams);
        }
        Glide.with(mContext).load(API.OSS_URL_ICON + mIconList.get(position).toString())
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.icon_mine_personal_default)
                .error(R.drawable.icon_mine_personal_default)
                .into(holder.iconImg);
        if(canDelete){
            holder.editIbt.setVisibility(View.VISIBLE);
        }else{
            holder.editIbt.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        private View space;
        private ImageView iconImg;
        private ImageButton editIbt;
    }

}
