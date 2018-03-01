package com.i5i58.live.home.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;
    private int TAG;
    Map<Integer, View> tMap = new HashMap<>();

    public CommonAdapter(Context context, List<Map<String, Object>> list, int tag) {
        super();
        this.context = context;
        this.list = list;
        this.TAG = tag;
    }

    @Override
    public int getCount() {

        return list.size();
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
        ViewHolder viewHolder = null;
        if (tMap.get(position) == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.mac_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_rank = (ImageView) convertView.findViewById(R.id.iv_rank);
            viewHolder.iv_guard = (ImageView) convertView.findViewById(R.id.iv_guard);
            viewHolder.iv_vip = (ImageView) convertView.findViewById(R.id.iv_vip);
            viewHolder.tv_l1 = (TextView) convertView.findViewById(R.id.tv_l1);
            viewHolder.tv_l2 = (TextView) convertView.findViewById(R.id.tv_l2);
            viewHolder.iv_badge = (ImageView) convertView.findViewById(R.id.iv_badge);
            viewHolder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
            if (TAG == 0) {
                viewHolder.iv_diamond = (ImageView) convertView.findViewById(R.id.iv_diamond);
                viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            } else if (TAG == 1) {
                viewHolder.tv_l3 = (TextView) convertView.findViewById(R.id.tv_l3);
                viewHolder.fl_badge = (FrameLayout) convertView.findViewById(R.id.fl_badge);
                ViewHolder.iv_attention = (ImageView) convertView.findViewById(R.id.iv_attention);
            }
            tMap.put(position, null);
            convertView.setTag(viewHolder);
        } else {
            convertView = tMap.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (TAG == 0) {
            switch (list.get(position).get("num").toString()) {
                case "1":
                    viewHolder.iv_badge.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.micadapter_item_badge));
                    break;
                case "2":
                    viewHolder.iv_badge.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.micadapter_item_badge2));
                    break;
                case "3":
                    viewHolder.iv_badge.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.micadapter_item_badge3));
                default:
                    if (position > 2) {
                        viewHolder.tv_num.setText(String.valueOf(position + 1));
                    }
                    break;
            }
            if (null != list.get(position).get("faceSmallUrl")) {
                Glide.with(context)
                        .load(StringUtil.checkIcon(list.get(position).get("faceSmallUrl").toString()))
                        .placeholder(R.drawable.icon_mine_personal_default)
                        .error(R.drawable.icon_mine_personal_default)
                        .transform(new GlideCircleTransform(context))
                        .into(viewHolder.iv_head);
            }else {
                viewHolder.iv_head.setImageResource(R.drawable.icon_mine_personal_default);
            }
            if (null != list.get(position).get("vip")) {
                int vip = Integer.valueOf(list.get(position).get("vip").toString());
                InputStream vipIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                viewHolder.iv_vip.setImageBitmap(vipBitmap);
                viewHolder.iv_vip.setVisibility(View.VISIBLE);
            }
            if (null != list.get(position).get("guard")) {
                int guard = Integer.valueOf(list.get(position).get("guard").toString());
                InputStream guardIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard));
                Bitmap guardBitmap = BitmapFactory.decodeStream(guardIs);
                viewHolder.iv_guard.setImageBitmap(guardBitmap);
                viewHolder.iv_guard.setVisibility(View.VISIBLE);
            }
            if (null != list.get(position).get("richScore")) {
                long richScore = Long.parseLong(list.get(position).get("richScore").toString());
                InputStream richScoreIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
                Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                viewHolder.iv_rank.setImageBitmap(richScoreBitmap);
            }else {
                viewHolder.iv_rank.setImageDrawable(context.getResources().getDrawable(R.drawable.lvl_1));
            }

            viewHolder.tv_name.setText(list.get(position).get("name").toString());
            viewHolder.tv_l1.setText("贡献值：");
            viewHolder.tv_l1.setTextColor(context.getResources().getColor(R.color.grey_word));
            viewHolder.tv_l2.setText(list.get(position).get("offer").toString());
            viewHolder.tv_l2.setTextColor(context.getResources().getColor(R.color.grey_word));
            viewHolder.iv_diamond.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_miccommomadapter_diamond));
        } else if (TAG == 1) {
            try {
                viewHolder.fl_badge.setVisibility(View.GONE);
                if (null != list.get(position).get("faceSmallUrl")) {
                    Glide.with(context)
                            .load(StringUtil.checkIcon(list.get(position).get("faceSmallUrl").toString()))
                            .placeholder(R.drawable.icon_mine_personal_default)
                            .error(R.drawable.icon_mine_personal_default)
                            .transform(new GlideCircleTransform(context))
                            .into(viewHolder.iv_head);
                }else {
                    viewHolder.iv_head.setImageResource(R.drawable.icon_mine_personal_default);
                }
                if (null != list.get(position).get("vip")) {
                    int vip = Integer.valueOf(list.get(position).get("vip").toString());
                    InputStream vipIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "vip_" + vip));
                    Bitmap vipBitmap = BitmapFactory.decodeStream(vipIs);
                    viewHolder.iv_vip.setImageBitmap(vipBitmap);
                    viewHolder.iv_vip.setVisibility(View.VISIBLE);
                }
                if (null != list.get(position).get("guard")) {
                    int guard = Integer.valueOf(list.get(position).get("guard").toString());
                    InputStream guardIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "guard_" + guard));
                    Bitmap guardBitmap = BitmapFactory.decodeStream(guardIs);
                    viewHolder.iv_guard.setImageBitmap(guardBitmap);
                    viewHolder.iv_guard.setVisibility(View.VISIBLE);
                }
                if (null != list.get(position).get("richScore")) {
                    long richScore = Long.parseLong(list.get(position).get("richScore").toString());
                    InputStream richScoreIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
                    Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                    viewHolder.iv_rank.setImageBitmap(richScoreBitmap);
                }
                viewHolder.tv_name.setText(list.get(position).get("name") + "");
                viewHolder.ll_bottom.setVisibility(View.GONE);
                if (list.get(position).get("attention").toString().equals("1")) {
                    ViewHolder.iv_attention.setVisibility(View.VISIBLE);
                    if (null != list.get(position).get("isAtt") && (boolean) list.get(position).get("isAtt")) {
                        ViewHolder.iv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bt_attention_cancel));
                    } else {
                        ViewHolder.iv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bt_attention));
                    }
                }
            } catch (NullPointerException e) {
                e.getMessage();
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_name, tv_l1, tv_l2, tv_l3, tv_num;
        private ImageView iv_badge, iv_rank, iv_diamond, iv_guard, iv_vip;
        public static ImageView iv_attention;
        private ImageView iv_head;
        private FrameLayout fl_badge;
        private LinearLayout ll_bottom;
    }


}
