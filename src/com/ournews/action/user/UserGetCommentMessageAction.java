package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.CommentServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/24.
 */
public class UserGetCommentMessageAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String token = request.getParameter("token");
            String page = request.getParameter("page");
            String size = request.getParameter("size");

            sendJSON(new CommentServiceImpl().getChildComment(uid, token, page, size));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
