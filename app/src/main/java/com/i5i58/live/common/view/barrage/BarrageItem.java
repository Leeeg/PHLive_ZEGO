package com.i5i58.live.common.view.barrage;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i5i58.live.common.view.imageVIew.RoundImageView;

/**
 * Created by lixueyong on 16/2/19.
 */
public class BarrageItem {

    public int textColor;
    public String text;
    public LinearLayout parent;

    public int textSize;
    public int moveSpeed;//移动速度
    public int verticalPos;//垂直方向显示的位置
    public int textMeasuredWidth;//字体显示占据的宽度

    public FrameLayout itemLayout;//漂屏item
    public RoundImageView icon;//漂屏头像
    public ImageView vip, guard, lv;
    public TextView name;
    public TextView textView;
}
