package com.i5i58.live.common.utils.md5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lee on 2017/4/12.
 */

public class MD5Util {
    /**
     * 利用MD5进行加密
     */
    public static String getMd5(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());
        String res = new BigInteger(1, md.digest()).toString(16).toUpperCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32 - res.length(); ++i) {
            builder.append("0");
        }
        return builder.append(res).toString();
    }
}
