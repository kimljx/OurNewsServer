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
        if (isPost()) {
            String loginName = request.getParameter("login_name");
            String password = request.getParameter("password");
            String time = request.getParameter("time");

            sendJSON(new UserServiceImpl().login(loginName, password, time));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
