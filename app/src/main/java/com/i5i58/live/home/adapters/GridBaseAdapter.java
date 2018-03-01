package com.i5i58.live.home.adapters;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.i5i58.live.R;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.GiftModel.GiftModel;

import java.util.List;

public class GridBaseAdapter extends BaseAdapter {

	private List<GiftModel> modelList;
	private Context context;

	public GridBaseAdapter(Context context, List<GiftModel> modelList) {
		this.modelList = modelList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return modelList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_dfrg_gift_grid, parent, false);
			holder.mAppIcon = (ImageView) convertView.findViewById(R.id.imgdetail);
			holder.guard = (ImageView) convertView.findViewById(R.id.guard);
			holder.mAppName = (TextView) convertView.findViewById(R.id.tuaninfo);
			holder.mPrice = (TextView) convertView.findViewById(R.id.tuaninfo2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Glide.with(context)
				.load(API.OSS_URL_GIFT + modelList.get(position).getUrl() + ".png")
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
				.into(holder.mAppIcon);
		if(modelList.get(position).getGuard().equals("1")){
			holder.guard.setBackground(context.getResources().getDrawable(R.drawable.icon_gift_for_guard));
		}
		holder.mAppName.setText(modelList.get(position).getName());
		String price = modelList.get(position).getPrice();
		String newPrice = String.valueOf(Float.valueOf(price) / 100);
		if(newPrice.endsWith("0")){
			newPrice = newPrice.substring(0,newPrice.length()-1);
		}
		if(newPrice.endsWith("0")){
			newPrice = newPrice.substring(0,newPrice.length()-1);
		}
		if(newPrice.endsWith(".")){
			newPrice = newPrice.substring(0,newPrice.length()-1);
		}
		holder.mPrice.setText(newPrice+"虎币");
		return convertView;
	}

	static class ViewHolder {
		ImageView mAppIcon,guard;
		TextView mAppName, mPrice;
	}
}
