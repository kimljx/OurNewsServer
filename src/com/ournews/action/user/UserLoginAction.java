package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.bean.User;
import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/13 0013.
 */
public class UserLoginAction extends BaseAction {

    private String loginName;
    private String password;
    private String time;

    @Override
    public void action() {
        loginName = request.getParameter("loginname");
        password = request.getParameter("password");
        time = request.getParameter("time");
        userDao = new UserDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password) || !MyUtils.isNumber(time)) {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        } else if ((System.currentTimeMillis() - Long.valueOf(time)) > Constant.CONNECT_OUT_TIME) {
            setResult(false);
            setErrorCode(Constant.CONNECT_TIME_OUT);
        } else {
            User user = userDao.login(loginName, password, time);
            if (user == null) {
                setResult(false);
                setErrorCode(Constant.LOGIN_NAME_NO_EXIST_OR_PASSWORD_ERROR);
            } else {
                setResult(true);
                JSONObject userJSON = new JSONObject();
                userJSON.put("id", user.getId());
                userJSON.put("loginname", user.getLoginName());
                userJSON.put("nickname", user.getNickName());
                userJSON.put("sex", user.getSex());
                userJSON.put("photo", user.getPhoto());
                userJSON.put("token", user.getToken());
                jsonObject.put("user", userJSON);
            }
        }
    }
}
