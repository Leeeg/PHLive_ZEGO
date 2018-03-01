package com.i5i58.live.common.utils.mathUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lee on 2017/4/19.
 */

public class MathUtil {

    public static long[] checkScore(long y) {
        long[] result = new long[2];
        long star = 0;
        long scor = (long) Math.floor((y / 40800));
        star = (long) (Math.sqrt(y % 40800 + 4) / 2 - 1);
        result[0] = scor;
        result[1] = star;
        return result;
    }

    /**
     * 获取娱乐积分对应的等级
     * @return
     * @author frank
     */
    public static int getRichScoreLevel(long richScore) {
        richScore = richScore / 100;
        if (richScore <= 10) {
            return 1;
        }
        if (richScore <= 100) {
            return 2;
        }
        if (richScore <= 200) {
            return 3;
        }
        if (richScore <= 500) {
            return 4;
        }
        if (richScore <= 800) {
            return 5;
        }
        if (richScore <= 2000) {
            return 6;
        }
        if (richScore <= 5000) {
            return 7;
        }
        if (richScore <= 10000) {
            return 8;
        }
        if (richScore <= 20000) {
            return 9;
        }
        if (richScore <= 50000) {
            return 10;
        }
        if (richScore <= 100000) {
            return 11;
        }
        if (richScore <= 200000) {
            return 12;
        }
        if (richScore <= 300000) {
            return 13;
        }
        if (richScore <= 400000) {
            return 14;
        }
        if (richScore <= 500000) {
            return 15;
        }
        if (richScore <= 600000) {
            return 16;
        }
        if (richScore <= 800000) {
            return 17;
        }
        if (richScore <= 1000000) {
            return 18;
        }
        if (richScore <= 2000000) {
            return 19;
        }
        if (richScore <= 3000000) {
            return 20;
        }
        return 20;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }
}
