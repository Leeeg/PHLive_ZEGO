package com.i5i58.live.data;

import com.i5i58.live.R;

/**
 * Created by Lee on 2017/5/5.
 */

public class ResData {

    public static int[] resLv = {
            R.drawable.lvl_1,
            R.drawable.lvl_2,
            R.drawable.lvl_3,
            R.drawable.lvl_4,
            R.drawable.lvl_5,
            R.drawable.lvl_6,
            R.drawable.lvl_7,
            R.drawable.lvl_8,
            R.drawable.lvl_9,
            R.drawable.lvl_10,
            R.drawable.lvl_11,
            R.drawable.lvl_12,
            R.drawable.lvl_13,
            R.drawable.lvl_14,
            R.drawable.lvl_15,
            R.drawable.lvl_16,
            R.drawable.lvl_17,
            R.drawable.lvl_18,
            R.drawable.lvl_19,
            R.drawable.lvl_20
    };

    public static int[] resVip = {
            R.drawable.vip_0,
            R.drawable.vip_1,
            R.drawable.vip_2,
            R.drawable.vip_3
    };

    public static int[] resGuard = {
            R.drawable.guard_1,
            R.drawable.guard_2,
            R.drawable.guard_3
    };

    public static int[] getResLv() {
        return resLv;
    }

    public static void setResLv(int[] resLv) {
        ResData.resLv = resLv;
    }

    public static int[] getResVip() {
        return resVip;
    }

    public static void setResVip(int[] resVip) {
        ResData.resVip = resVip;
    }

    public static int[] getResGuard() {
        return resGuard;
    }

    public static void setResGuard(int[] resGuard) {
        ResData.resGuard = resGuard;
    }
}
