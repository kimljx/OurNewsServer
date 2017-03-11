package com.ournews.utils;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/3/11.
 */
public class OkHttpUtil {

    public static final String MEDIA_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    private static OkHttpClient client;

    private OkHttpUtil() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (client == null) {
            synchronized (OkHttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().build();
                }
            }
        }
        return client;
    }
}
