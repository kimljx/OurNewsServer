package com.ournews.utils;

import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/11.
 */
public class PushUtil {

    private static final String UMENG_APP_KEY = "58c38c8daed1790c750014a2";

    private static final String UMENG_APP_MASTER_SECRET = "auf6sl4c5u7dvtzysfkot0sjvl8ebnol";

    private static final String UMENG_PUSH_URL = "http://msg.umeng.com/api/send?sign=";

    private PushUtil() {
    }

    public static String pushToAndroid(String title, String text, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appkey", UMENG_APP_KEY);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("type", "broadcast");

        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put("display_type", "notification");

        JSONObject bodyJSON = new JSONObject();
        bodyJSON.put("ticker", "OurNews Push");
        bodyJSON.put("title", title);
        bodyJSON.put("text", text);
        bodyJSON.put("play_vibrate", true);
        bodyJSON.put("play_lights", true);
        bodyJSON.put("play_sound", true);
        bodyJSON.put("after_open", "go_activity");
        bodyJSON.put("activity", "com.team60.ournews.module.ui.activity.FirstActivity");

        JSONObject extraJSON = new JSONObject();
        extraJSON.put("pushContent", content);

        payloadJSON.put("extra", extraJSON);
        payloadJSON.put("body", bodyJSON);
        jsonObject.put("payload", payloadJSON);

        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse(OkHttpUtil.MEDIA_TYPE), jsonObject.toString());
        Request request = new Request.Builder().url(UMENG_PUSH_URL + getSign(jsonObject.toString())).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseJSON = JSONObject.fromObject(response.body().string());
            if (responseJSON.getString("ret").equals("SUCCESS")) {
                return ResultUtil.getSuccessJSON("").toString();
            } else {
                return ResultUtil.getErrorJSON(responseJSON.getJSONObject("data").getString("error_code")).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.UMENG_INTERNET_ERROR).toString();
        }
    }

    private static String getSign(String postBody) {
        String sign = "POSThttp://msg.umeng.com/api/send" + postBody + UMENG_APP_MASTER_SECRET;
        return MD5Util.getMD5(sign);
    }
}
