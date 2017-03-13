package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.CommentServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/13.
 */
public class UserLickCommentAction extends BaseAction {
    @Override
    public void action() throws IOException {
        String cid = request.getParameter("cid");
        String nid = request.getParameter("nid");
        String token = request.getParameter("token");
        String type = request.getParameter("type");

        sendJSON(new CommentServiceImpl().lickComment(cid, nid, token, type));
    }
}
