package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */
public class UserSearchNewAction extends BaseAction {

    @Override
    public void action() throws IOException {
        String name = request.getParameter("name");
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String sort = request.getParameter("sort");

        sendJSON(new NewServiceImpl().getSearchNew(name, page, size, sort));
    }
}
