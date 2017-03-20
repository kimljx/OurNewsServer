package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/20.
 */
public class ManagerChangeUserInfoAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("id");
            String token = request.getParameter("token");
            String nickName = request.getParameter("nick_name");
            String sex = request.getParameter("sex");
            String sign = request.getParameter("sign");
            String birthday = request.getParameter("birthday");
            String photo = request.getParameter("photo");

            sendJSON(new UserServiceImpl().changeManagerInfo(id, token, nickName, sex, sign, birthday, photo));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
