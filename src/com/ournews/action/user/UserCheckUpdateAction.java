package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */
public class UserCheckUpdateAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String time = request.getParameter("time");
            String key = request.getParameter("key");

            sendJSON(new UserServiceImpl().checkUpdate(time, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
