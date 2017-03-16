package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.CommentServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public class UserWriteChildCommentAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String cid = request.getParameter("cid");
            String content = request.getParameter("content");
            String time = request.getParameter("time");
            String token = request.getParameter("token");
            String key = request.getParameter("key");

            sendJSON(new CommentServiceImpl().writeChildComment(uid, cid, content, time, token, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
