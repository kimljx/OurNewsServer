package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class ManagerAddNewsAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String mid = request.getParameter("mid");
            String token = request.getParameter("token");
            String title = request.getParameter("title");
            String cover = request.getParameter("cover");
            String abstractContent = request.getParameter("abstract");
            String content = request.getParameter("content");
            String type = request.getParameter("type");
            String push = request.getParameter("push");

            sendJSON(new NewServiceImpl().addNew(mid, token, title, cover, abstractContent, content, type, push));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
