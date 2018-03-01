package com.i5i58.live.home.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.model.entity.account.SofaAccount;

import org.xutils.common.util.LogUtil;

import java.io.InputStream;
import java.util.List;

public class SofaListAdapter extends RecyclerView.Adapter<SofaListAdapter.ViewHolder>{

    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    private LayoutInflater mInflater;
    private List<SofaAccount> mDatas;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public SofaListAdapter(Context context, List<SofaAccount> datats) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datats;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.operate_gift_top_sofalist, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.im_sofa_image);
        viewHolder.lv_live = (ImageView) view.findViewById(R.id.lv_live);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        try {
            if (0 < mDatas.size()) {
                if (i <= mDatas.size() && null != mDatas.get(i).getFaceSmallUrl()) {
                    Glide.with(context).load(StringUtil.checkIcon(mDatas.get(i).getFaceSmallUrl()))
                            .error(R.drawable.icon_mine_personal_default)
                            .transform(new GlideCircleTransform(context))
                            .into(viewHolder.mImg);
                }
                if (0 != mDatas.get(i).getRichScore()) {
                    long richScore = mDatas.get(i).getRichScore();
                    InputStream richScoreIs = context.getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
                    Bitmap richScoreBitmap = BitmapFactory.decodeStream(richScoreIs);
                    viewHolder.lv_live.setImageBitmap(richScoreBitmap);
                }
                if (mOnItemClickLitener != null) {
                    viewHolder.itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                        }
                    });
                }
            }
        } catch (NullPointerException e) {
            LogUtil.e(e.toString());
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        ImageView lv_live;
    }

}
