package com.i5i58.live.common.utils.json;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lee on 2017/5/16.
 */

public class JsonUtil {

    public static JSONObject parseMap(Map map) throws JSONException {
        if (map == null) {
            return null;
        }

        JSONObject obj = new JSONObject();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            obj.put(key, value);
        }

        return obj;
    }

}
