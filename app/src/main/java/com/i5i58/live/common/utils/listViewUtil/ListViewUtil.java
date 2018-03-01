package com.i5i58.live.common.utils.listViewUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by Lee on 2017/4/19.
 */

public class ListViewUtil {

    /**
     * 重新设置Lisyview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        LogUtil.d("setListViewHeightBasedOnChildren：count   >>>" + listAdapter.getCount());
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            LogUtil.d("setListViewHeightBasedOnChildren：item  >>>" + listItem.getMeasuredHeight());
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 重新设置Lisyview高度
     * height    额外增加的高度
     */
    public static void setListViewHeightBasedOnChildren2(ListView listView, int height) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        LogUtil.d("setListViewHeightBasedOnChildren：count   >>>" + listAdapter.getCount());
        int totalHeight = height;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            LogUtil.d("setListViewHeightBasedOnChildren：item  >>>" + listItem.getMeasuredHeight());
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * ListView局部刷新
     * @param listview
     * @param position
     * @param data
     */
    public void updateItemView(ListView listview, int position, List data){
        int firstPos = listview.getFirstVisiblePosition();
        int lastPos = listview.getLastVisiblePosition();
        if(position >= firstPos && position <= lastPos){  //可见才更新，不可见则在getView()时更新
            //listview.getChildAt(i)获得的是当前可见的第i个item的view
            View view = listview.getChildAt(position - firstPos);
//            VH vh = (VH)view.getTag();
//            vh.text.setText(data.text);
        }
    }
}
