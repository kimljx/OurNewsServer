package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/3.
 */
public class ManagerChangeVersionInfoAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String mid = request.getParameter("mid");
            String token = request.getParameter("token");
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            String sort = request.getParameter("sort");

            sendJSON("");
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
