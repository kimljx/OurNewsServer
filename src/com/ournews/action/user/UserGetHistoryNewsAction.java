package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/3/9 0009.
 */
public class UserGetHistoryNewsAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String token = request.getParameter("token");
            String oid = request.getParameter("oid");
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            String sort = request.getParameter("sort");

            sendJSON(new NewServiceImpl().getHistory(uid, token, oid, page, size, sort));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
