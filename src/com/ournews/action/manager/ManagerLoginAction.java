package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/20.
 */
public class ManagerLoginAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String time = request.getParameter("time");

            sendJSON(new UserServiceImpl().loginManager(phone, password, time));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
