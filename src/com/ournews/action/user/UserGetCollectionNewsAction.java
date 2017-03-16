package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/9.
 */
public class UserGetCollectionNewsAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("nid");
            String token = request.getParameter("token");
            String uid = request.getParameter("uid");
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            String sort = request.getParameter("sort");

            sendJSON(new NewServiceImpl().getCollections(id, token, uid, page, size, sort));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
