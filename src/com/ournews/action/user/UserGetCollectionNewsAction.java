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
        String uid = request.getParameter("uid");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String sort = request.getParameter("sort");

        sendJSON(new NewServiceImpl().getCollections(uid, token, page, size, sort));
    }
}
