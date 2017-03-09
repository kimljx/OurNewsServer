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
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String sort = request.getParameter("sort");

        sendJSON(new NewServiceImpl().getHistory(id, token, page, size, sort));
    }
}
