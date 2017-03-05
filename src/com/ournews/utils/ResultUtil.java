package com.ournews.utils;

import com.sun.istack.internal.NotNull;
import net.sf.json.JSONObject;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */
public class ResultUtil {
    public static JSONObject getErrorJSON(@NotNull String errorCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "error");
        jsonObject.put("error_code", errorCode);
        jsonObject.put("data", "");
        return jsonObject;
    }

    public static JSONObject getSuccessJSON(@NotNull String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "success");
        jsonObject.put("error_code", "0");
        jsonObject.put("data", data);
        return jsonObject;
    }
}
