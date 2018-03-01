package com.i5i58.live.common.utils.preference;

import com.i5i58.live.common.preferences.SysPreferences;

/**
 * Created by Lee on 2017/4/14.
 */

public class PreferenceUtil {
    public static boolean needClear(String phone){
        if (phone.equals(SysPreferences.getPhone())){
            return false;
        }
        return true;
    }
}
