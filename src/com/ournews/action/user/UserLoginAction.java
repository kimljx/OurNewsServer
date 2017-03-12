package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/13 0013.
 */
public class UserLoginAction extends BaseAction {

    @Override
    public void action() throws IOException {
        String loginName = request.getParameter("login_name");
        String password = request.getParameter("password");
        String time = request.getParameter("time");
        String umengToken = request.getParameter("umeng_token");

        sendJSON(new UserServiceImpl().login(loginName, password, time, umengToken));
    }
}
