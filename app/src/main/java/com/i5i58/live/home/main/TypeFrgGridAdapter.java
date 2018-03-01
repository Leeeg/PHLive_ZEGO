package com.i5i58.live.home.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.ChannelData;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/4/18.
 */

public class TypeFrgGridAdapter extends BaseAdapter {
    private List<ChannelData> list;
    private LayoutInflater inflater;
    private Context mContext;
    Map<Integer,View> tMap = new HashMap<Integer,View>();
    public TypeFrgGridAdapter(List<ChannelData> list, Context context) {
        super();
        this.list = list;
        this.mContext = context;
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
            convertView = inflater.inflate(R.layout.frg_morefrg_gridview_item, null);
            holder.cover_image = (ImageView) convertView.findViewById(R.id.cover_image);
            holder.iv_persion_image = (ImageView) convertView.findViewById(R.id.iv_persion_image);
            holder.icon_living = (ImageView) convertView.findViewById(R.id.icon_living);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_online_count = (TextView) convertView.findViewById(R.id.tv_online_count);
            holder.tv_addres = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            convertView = tMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) holder.cover_image.getLayoutParams();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        linearParams.width = ((wm.getDefaultDisplay().getWidth()) - 0 ) / 2 ;
        linearParams.height= ((wm.getDefaultDisplay().getWidth()) - 0 ) / 2 ;
        holder.cover_image.setLayoutParams(linearParams);

        Glide.with(mContext).load(API.OSS_URL_CHANNELCOVER + list.get(position).getCoverUrl())
                .placeholder(R.drawable.bg_morefrg_gridview_item_default)
                .error(R.drawable.bg_morefrg_gridview_item_default)
                .into(holder.cover_image);
        if (null != list.get(position).getTitle()){
            holder.tv_name.setText(list.get(position).getTitle());
        }else {
            holder.tv_name.setText(list.get(position).getChannelName());
        }
        holder.tv_online_count.setText(String.valueOf(list.get(position).getPlayerTimes()));
        holder.tv_addres.setText(StringUtil.getLocation(list.get(position).getLocation()));
        if (null  != list.get(position).getOwnerId()){
            getIcon(list.get(position).getOwnerId(),holder.iv_persion_image);
        }else {
            holder.iv_persion_image.setImageResource(R.drawable.icon_mine_personal_default);
        }
        if (list.get(position).getStatus() == 1){
            holder.icon_living.setVisibility(View.VISIBLE);
        }else {
            holder.icon_living.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void getIcon(String ownerId, final ImageView imageView) {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.GETFACEURL)
                .addParam("targetAccId", ownerId)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                JSONObject dataFace = js.getJSONObject("data");
                                if (dataFace.has("faceSmallUrl")){
                                    String iconUrl = dataFace.getString("faceSmallUrl");
                                    Glide.with(mContext).load(StringUtil.checkIcon(iconUrl))
                                            .transform(new GlideCircleTransform(mContext))
                                            .placeholder(R.drawable.icon_mine_personal_default)
                                            .error(R.drawable.icon_mine_personal_default)
                                            .into(imageView);
                                } else {
                                    imageView.setImageResource(R.drawable.icon_mine_personal_default);
                                }
                            } else {
                                imageView.setImageResource(R.drawable.icon_mine_personal_default);
                                LogUtil.e("获取直播间列表数据出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    class ViewHolder {
        ImageView cover_image, icon_living, iv_persion_image;
        TextView tv_name, tv_online_count, tv_addres;
    }

}