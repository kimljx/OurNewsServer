package com.ournews.utils;

import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/20.
 */
public class CodeUtil {

    private static final String CODE_URL = "http://www.jintang54vip.com/APPInterface/user/Login.ashx??act=login&phone=";

    private CodeUtil() {
    }

    public static int getCode(String phone) {
        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        Request request = new Request.Builder()
                .url(CODE_URL + phone)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.fromObject(response.body().string());
            if (jsonObject.getInt("Status") == 0) {
                return jsonObject.getInt("Data");
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
