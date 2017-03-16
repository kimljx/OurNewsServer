package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.PushServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/11.
 */
public class ManagerPushNewToAllAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String nid = request.getParameter("nid");
            String time = request.getParameter("time");

            sendJSON(new PushServiceImpl().pushNewToAll(nid, time));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
