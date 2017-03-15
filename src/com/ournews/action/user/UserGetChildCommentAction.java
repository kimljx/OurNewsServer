package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.CommentServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/15.
 */
public class UserGetChildCommentAction extends BaseAction {
    @Override
    public void action() throws IOException {
        String cid = request.getParameter("cid");
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String sort = request.getParameter("sort");

        sendJSON(new CommentServiceImpl().getChildComment(cid, page, size, sort));
    }
}
