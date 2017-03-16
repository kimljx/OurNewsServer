package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/16.
 */
public class ManagerSetModeAction extends BaseAction {
    @Override
    public void action() throws IOException {
        String mode = request.getParameter("mode");

        JSONObject resultJSON;
        if (MyUtils.isNull(mode) || (!mode.equals("debug") && !mode.equals("release"))) {
            resultJSON = ResultUtil.getErrorJSON(Constant.VALUES_ERROR);
        } else {
            if (mode.equals("debug")) {
                Constant.IS_DEBUG = true;
            } else {
                Constant.IS_DEBUG = false;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mode", mode);
            resultJSON = ResultUtil.getSuccessJSON(jsonObject);
        }
        sendJSON(resultJSON.toString());
    }
}
