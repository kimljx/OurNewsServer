package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/24 0024.
 */
public class UserCollectNewAction extends BaseAction {

    @Override
    public void action() throws IOException {
        if (isPost()) {
            String nid = request.getParameter("nid");
            String uid = request.getParameter("uid");
            String token = request.getParameter("token");
            String type = request.getParameter("type");

            sendJSON(new NewServiceImpl().collectionNew(nid, uid, token, type));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
