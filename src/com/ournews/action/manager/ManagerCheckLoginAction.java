package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ManagerCheckLoginAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("id");
            String token = request.getParameter("token");

            sendJSON(new UserServiceImpl().checkLoginManager(id, token));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
