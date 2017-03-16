package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */
public class UserGetNewListAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String type = request.getParameter("type");
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            String sort = request.getParameter("sort");

            sendJSON(new NewServiceImpl().getTypeNew(type, page, size, sort));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
