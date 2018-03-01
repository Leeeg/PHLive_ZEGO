package com.i5i58.live.mine.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.rnConfig.React;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/4/19.
 */

public class RnListAdapter extends BaseAdapter{
    private List<React> list;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean showIcon;
    Map<Integer, View> tMap = new HashMap<Integer, View>();

    public RnListAdapter(List<React> list, Context context) {
        super();
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public RnListAdapter(List<React> list, Context context, boolean showIcon) {
        super();
        this.list = list;
        this.mContext = context;
        this.showIcon = showIcon;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (tMap.get(position) == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_mine_rnlistview, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            convertView = tMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        if(showIcon){
            holder.iv_icon.setVisibility(View.VISIBLE);
            String url = API.OSS_URL_SYSTEMICON + list.get(position).getIcon();
            Glide.with(mContext).load(url)
                    .into(holder.iv_icon);
        }
        holder.tv_name.setText(list.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }
}
