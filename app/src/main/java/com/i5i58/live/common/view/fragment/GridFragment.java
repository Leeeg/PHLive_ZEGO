package com.i5i58.live.common.view.fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.i5i58.live.R;
import com.i5i58.live.home.adapters.GridBaseAdapter;
import com.i5i58.live.model.entity.GiftModel.GiftModel;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends Fragment {

	private View view;
	private GridView gv;
	private int index = -1;
	private FragmentActivity context;
	private List<GiftModel> modelList;
	private GridCallback gridCallback;

	public interface GridCallback{
		void sendGift(String gId);
	}

	public static GridFragment newInstance(int index, ArrayList<GiftModel> modelList, GridCallback gridCallback) {
		GridFragment gf = new GridFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("index", index);
		bundle.putParcelableArrayList("model", modelList);
		gf.setArguments(bundle);
		gf.gridCallback = gridCallback;
		return gf;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			context = getActivity();
			Bundle bundle = getArguments();
			index = bundle.getInt("index");
			modelList = bundle.getParcelableArrayList("model");
			List<GiftModel> newGiftModels;
			int last = 8 * index + 8;
			if (last >= modelList.size()) {
				newGiftModels = modelList.subList((8 * index), (modelList.size()));
			} else {
				newGiftModels = modelList.subList((8 * index), (last));
			}
			view = LayoutInflater.from(context).inflate(R.layout.item_gridfragment_gift,
					container, false);
			gv = (GridView) view.findViewById(R.id.gridview);
			// 这里重新开辟一个地址空间，来保存list，否则会报ConcurrentModificationException错误
			final ArrayList<GiftModel> text = new ArrayList<GiftModel>();
			text.addAll(newGiftModels);
			gv.setAdapter(new GridBaseAdapter(getActivity(), text));
			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gv.setOnItemClickListener(new OnItemClickListener() {

				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < parent.getCount(); i++) {
                        View v = parent.getChildAt(i);
                        if (position == i) {// 当前选中的Item改变背景颜色
                            ImageView imageView = (ImageView) v.findViewById(R.id.item_chosed);
                            imageView.setBackground(getResources().getDrawable(R.drawable.bg_item_gift_chosed));
                        } else {
                            ImageView imageView = (ImageView) v.findViewById(R.id.item_chosed);
                            imageView.setBackgroundResource(0);
                        }
                    }
					gridCallback.sendGift(String.valueOf(text.get(position).getgId()));
				}
			});
		} else {
			ViewGroup root = (ViewGroup) view.getParent();
			if (root != null) {
				root.removeView(view);
			}
		}
		return view;
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResume() {
        super.onResume();

    }

}
