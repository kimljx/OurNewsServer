package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/21.
 */
public class UserGetNewContentForWebAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String nid = request.getParameter("nid");

            sendJSON(new NewServiceImpl().getContent(uid, nid, 2));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
