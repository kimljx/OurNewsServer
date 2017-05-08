package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/3.
 */
public class ManagerGetOwnNewAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("id");
            String token = request.getParameter("token");
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            String sort = request.getParameter("sort");

            sendJSON(new NewServiceImpl().getOwnNew(id, token, page, size, sort));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
