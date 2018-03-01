package com.i5i58.live.recharge;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.i5i58.live.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeListviewAdapter extends BaseAdapter {
	private List<String> list;
	private List<String> donationList;
	private LayoutInflater inflater;
	private Context context;
	Map<Integer,View> tMap = new HashMap<Integer,View>();
	public RechargeListviewAdapter(List<String> list, List<String> donationList, Context context) {
		super();
		this.list = list;
		this.donationList = donationList;
		this.context = context;
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
			convertView = inflater.inflate(R.layout.recharge_listview_item, null);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_donation = (TextView) convertView.findViewById(R.id.tv_donation);
            holder.tv_buy_money = (TextView) convertView.findViewById(R.id.tv_buy_money);
            convertView.setTag(holder);
		} else {
			convertView = tMap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
        holder.tv_money.setText(list.get(position));
        holder.tv_donation.setText(donationList.get(position));
        holder.tv_buy_money.setText("¥" + list.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView tv_money, tv_donation, tv_buy_money;
	}

}
