package com.ournews.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/3/11.
 */
public class PushUtil {

    private static final String JIGUANG_PUSH_URL = "https://api.jpush.cn/v3/push";

    private static final String JIGUANG_APP_KEY = "205c085cd3834658de4534b0";

    private static final String JIGUANG_MASTER_SECRET = "bc10118232a97136c5944dc6";

    private PushUtil() {
    }

    public static String pushNewToAll(String text, String extra) {
        return connectJiGuangServer(getJiGuangJSON(text, extra));
    }

    public static void pushChildCommentToUser(String uid, String otherUserName, String extra) {
        connectJiGuangServer(getJiGuangJSON(uid, "用户 " + otherUserName + " 回复了您的评论", extra));
    }

    private static JSONObject getJiGuangJSON(String uid, String text, String extra) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("platform", "all");

        JSONArray aliasArray = new JSONArray();
        aliasArray.add(uid);
        JSONObject aliasJSON = new JSONObject();
        aliasJSON.put("alias", aliasArray);
        jsonObject.put("audience", aliasJSON);

        JSONObject notifyJSON = new JSONObject();
        JSONObject androidJSON = new JSONObject();

        androidJSON.put("alert", text);
        androidJSON.put("title", "OurNews");

        JSONObject extraJSON = new JSONObject();
        extraJSON.put("pushContent", extra);

        androidJSON.put("extras", extraJSON);

        notifyJSON.put("android", androidJSON);

        jsonObject.put("notification", notifyJSON);

        return jsonObject;
    }

    private static JSONObject getJiGuangJSON(String text, String extra) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("platform", "all");
        jsonObject.put("audience", "all");

        JSONObject notifyJSON = new JSONObject();
        JSONObject androidJSON = new JSONObject();

        androidJSON.put("alert", text);
        androidJSON.put("title", "OurNews");

        JSONObject extraJSON = new JSONObject();
        extraJSON.put("pushContent", extra);

        androidJSON.put("extras", extraJSON);

        notifyJSON.put("android", androidJSON);

        jsonObject.put("notification", notifyJSON);

        return jsonObject;
    }

    private static String connectJiGuangServer(JSONObject jsonObject) {
        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse(OkHttpUtil.MEDIA_TYPE), jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + getBase64(JIGUANG_APP_KEY + ":" + JIGUANG_MASTER_SECRET))
                .url(JIGUANG_PUSH_URL)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ResultUtil.getSuccessJSON(JSONObject.fromObject(response.body().string())).toString();
        } catch (IOException e) {
            return ResultUtil.getErrorJSON(Constant.JIGUANG_INTERNET_ERROR).toString();
        }
    }

    private static String getBase64(String str) {
        try {
            byte[] b = str.getBytes("utf-8");
            return new BASE64Encoder().encode(b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
