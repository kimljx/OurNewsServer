package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetNewContentAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String uid = request.getParameter("uid");
            String nid = request.getParameter("nid");

            sendJSON(new NewServiceImpl().getContent(uid, nid));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
