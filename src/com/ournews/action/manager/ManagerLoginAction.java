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
            String code = request.getParameter("code");
            String time = request.getParameter("time");
            String key = request.getParameter("key");

            sendJSON(new UserServiceImpl().loginManager(phone, code, time, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
