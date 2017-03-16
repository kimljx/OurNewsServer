package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class UserRegisterAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String loginName = request.getParameter("login_name");
            String password = request.getParameter("password");
            String time = request.getParameter("time");
            String key = request.getParameter("key");

            sendJSON(new UserServiceImpl().register(loginName, password, time, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
