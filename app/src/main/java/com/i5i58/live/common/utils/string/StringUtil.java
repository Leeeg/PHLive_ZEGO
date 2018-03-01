package com.i5i58.live.common.utils.string;

import android.text.TextUtils;

import com.i5i58.live.model.api.API;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Lee on 2017/4/18.
 */

public class StringUtil {
    /**
     * 检查头像格式
     */
    public static String checkIcon(String faceSmallUrl){
        String url = null;
        if(null != faceSmallUrl) {
            if (faceSmallUrl.length() > 4) {
                Boolean bl = faceSmallUrl.substring(0, 4).equals("http");
                if (!bl) {
                    url = API.OSS_URL_ICON + faceSmallUrl;
                }else {
                    url = faceSmallUrl;
                }
            } else {
                url = API.OSS_URL_ICON + faceSmallUrl;
            }
        }
        return url;
    }

    /**
     * 地址解析
     * @param locationData
     * @return
     */
    public static String getLocation(String locationData){
        LogUtil.d("" + locationData);
        try {
            if (null == locationData){
                return "未知星球";
            }else {
                JSONObject loc = new JSONObject(locationData);
                String addr = loc.getString("state") + " " + loc.getString("city");
                return addr;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "未知星球";
    }

    /**
     * 时间戳转日期
     * @param s
     * @return
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
 * 将时间戳转换为时间
 */
    public static String stampToTime(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static String getPercentString(float percent) {
        return String.format(Locale.US, "%d%%", (int) (percent * 100));
    }
    /**
     * 删除字符串中的空白符
     *
     * @param content
     * @return String
     */
    public static String removeBlanks(String content) {
        if (content == null) {
            return null;
        }
        StringBuilder buff = new StringBuilder();
        buff.append(content);
        for (int i = buff.length() - 1; i >= 0; i--) {
            if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
                    || ('\r' == buff.charAt(i))) {
                buff.deleteCharAt(i);
            }
        }
        return buff.toString();
    }
    /**
     * 获取32位uuid
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    /**
     * 生成唯一号
     *
     * @return
     */
    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    //	public static String makeMd5(String source) {
//		return MD5.getStringMD5(source);
//	}
//
    public static final String filterUCS4(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.codePointCount(0, str.length()) == str.length()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        int index = 0;
        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            index += Character.charCount(codePoint);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                continue;
            }

            sb.appendCodePoint(codePoint);
        }

        return sb.toString();
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    /**
     * 是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
