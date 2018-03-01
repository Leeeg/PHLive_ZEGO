package com.i5i58.live.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.i5i58.live.R;

/**
 * Created by Lee on 2017/4/13.
 */

public class FilterAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int[] images = {R.drawable.nomal, R.drawable.pingjing, R.drawable.qingshuang, R.drawable.tonghua,
            R.drawable.nature, R.drawable.jiankang, R.drawable.wenrou, R.drawable.meibai,
            R.drawable.nature, R.drawable.jiankang, R.drawable.wenrou, R.drawable.meibai,
            R.drawable.nature, R.drawable.jiankang};
    private String[] texts = {"原图", "简洁", "黑白", "老化", "哥特", "锐色", "淡雅", "酒红", "青柠", "浪漫", "光晕", "蓝调", "梦幻", "夜色"};

    public FilterAdapter(Context context){
        this.mContext = context;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.length;
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
            convertView = mInflater.inflate(R.layout.item_filter, null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.filter_image);
            holder.textView = (TextView) convertView.findViewById(R.id.filter_text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.imageView.setImageResource(images[position]);
        holder.textView.setText(texts[position]);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }

}
