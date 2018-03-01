package com.i5i58.live.common.utils.httpUtil;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    private static String getParameters(Map<String, Object> map) {
        StringBuffer buffer = new StringBuffer();
        if (map == null || map.isEmpty()) {
            return "";
        } else {
            for (String key : map.keySet()) {
                String value = String.valueOf(map.get(key));
                if (buffer.length() < 1) {
                    buffer.append(key).append("=").append(value);
                } else {
                    buffer.append("&").append(key).append("=").append(value);
                }
            }
        }
        return buffer.toString();
    }

    public static String doGet(Net net) {
        String result = "";
        try {
            Log.e("GET_URL", net.getUrl() + "?" + getParameters(net.getMap()));
            URL urlGet = new URL(net.getUrl() + "?" + getParameters(net.getMap()));
            HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();

            conn.setRequestMethod("GET");
            result = getResult(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String doPost(Net net) {
        String result = "";
        try {
            URL urlPost = new URL(net.getUrl());
            HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
            conn.setRequestMethod("POST");//设置请求方式
            conn.setDoInput(true);//设置可以使用输入流读取数�?
            conn.setDoOutput(true);//设置可以使用输出流写数据
            conn.setUseCaches(false);//不使用缓存?
            if (null != net.getAccid()) {
                conn.setRequestProperty("accId", net.getAccid());
            }
            if (null != net.getToken()) {
                conn.setRequestProperty("token", net.getToken());
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            String data = getParameters(net.getMap());
            conn.setRequestProperty("Connection", "Keep-Alive");

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();//刷新
            out.close();
            result = getResult(conn);
            Log.e("POST_RESULT", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getResult(HttpURLConnection conn) {
        StringBuffer buffer = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            buffer = new StringBuffer();
            String line = null;
            while (null != (line = reader.readLine())) {
                buffer.append(line);
            }
            reader.close();
            conn.disconnect();
            Log.i("HttpURLConnection",conn.getResponseCode()+"");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                conn.getResponseCode();
                Log.e("HttpURLConnection",conn.getResponseCode()+"");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return buffer == null ? "" : buffer.toString();
    }

}
