package com.i5i58.live.home.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.i5i58.live.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftCountAdapter extends BaseAdapter {
	private List<Map> mList;
	private Context mContext;
	Map<Integer,View> tMap = new HashMap<Integer,View>();

	public GiftCountAdapter(Context context, List<Map> list) {
		mContext = context;
		mList = list;
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Map appInfo = mList.get(position);
		final AppItem appItem;
		if (tMap.get(position) == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.gift_count_item, null);

			appItem = new AppItem();
			appItem.tv_num = (TextView) v.findViewById(R.id.tv_gnum);
			appItem.tv_words = (TextView) v.findViewById(R.id.tv_gwords);

			tMap.put(position, null);
			v.setTag(appItem);
			convertView = v;
		} else {
			convertView = tMap.get(position);
			appItem = (AppItem) convertView.getTag();
		}
		if(mList != null){
            appItem.tv_words.setText(appInfo.get("words").toString());
            if(null != appInfo.get("count")){
                appItem.tv_num.setText(appInfo.get("count").toString());
            }else{
                appItem.tv_num.setVisibility(View.GONE);
                appItem.tv_words.setGravity(Gravity.CENTER);
            }
        }
		return convertView;
	}

	class AppItem {
		TextView tv_num, tv_words;
	}
}
