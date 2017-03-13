package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public class UserCheckLoginAction extends BaseAction {
    @Override
    public void action() throws IOException {
        String id = request.getParameter("id");
        String token = request.getParameter("token");

        sendJSON(new UserServiceImpl().checkLogin(id, token));
    }
}
