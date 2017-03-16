package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class UserChangeInfoAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("id");
            String token = request.getParameter("token");
            String nickName = request.getParameter("nick_name");
            String sex = request.getParameter("sex");
            String photo = request.getParameter("photo");

            sendJSON(new UserServiceImpl().changeInfo(id, token, nickName, sex, photo));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
