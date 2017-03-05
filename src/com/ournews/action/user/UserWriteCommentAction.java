package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserWriteCommentAction extends BaseAction {

    @Override
    public void action() throws IOException {
        String uid = request.getParameter("uid");
        String nid = request.getParameter("nid");
        String content = request.getParameter("content");
        String time = request.getParameter("time");
        String key = request.getParameter("key");
        System.out.println(uid);
        System.out.println(nid);
        System.out.println(content);
        System.out.println(time);
        System.out.println(key);

        sendJSON(new NewServiceImpl().writeComment(uid, nid, content, time, key));
    }
}
