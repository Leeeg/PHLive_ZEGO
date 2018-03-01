package com.i5i58.live.recharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i5i58.live.R;

import java.math.BigDecimal;


public class RechargeAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private Double[] rmb = new Double[5];

	public RechargeAdapter(Context context, Double[] rmb) {
		super();
		this.context = context;
		this.rmb = rmb;
		inflater=LayoutInflater.from(context);
	}



	@Override
	public int getCount() {
		return rmb.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.recharge_item, parent, false);
			holder.tv_rmb=(TextView) convertView.findViewById(R.id.tv_rmb);
			holder.tv_ib=(TextView) convertView.findViewById(R.id.tv_ib);
			if (position == 0) {
				holder.rl_item=(RelativeLayout) convertView.findViewById(R.id.rl_item);
			}
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		double d = rmb[position];
		BigDecimal b = new BigDecimal(d * 0.7); 
		double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		holder.tv_rmb.setText("¥"+(int)d);
		holder.tv_ib.setText(f1 +" 虎币");
		if (position == 0) {
			holder.rl_item.setBackgroundResource(R.drawable.shape_recharge);
		}
		return convertView;
	}

	
	class ViewHolder{
		TextView tv_rmb,tv_ib;
		RelativeLayout rl_item;
	}
}
