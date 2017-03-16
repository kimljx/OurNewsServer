package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.CommentServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserWriteCommentAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String nid = request.getParameter("nid");
            String content = request.getParameter("content");
            String time = request.getParameter("time");
            String token = request.getParameter("token");
            String key = request.getParameter("key");

            sendJSON(new CommentServiceImpl().writeComment(uid, nid, content, time, token, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
