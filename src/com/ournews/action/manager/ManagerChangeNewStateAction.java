package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/9.
 */
public class ManagerChangeNewStateAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String id = request.getParameter("id");
            String token = request.getParameter("token");
            String nid = request.getParameter("nid");
            String state = request.getParameter("state");

            sendJSON(new NewServiceImpl().changeNewState(id, token, nid, state));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
