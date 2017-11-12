package com.vaiyee.myweather.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/11/11.
 */

public class HttpUtil {
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);

    }
}
