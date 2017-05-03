package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.VersionServiceImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/3.
 */
public class ManagerGetVersionInfoAction extends BaseAction {
    @Override
    public void action() throws IOException {
        if (isPost()) {
            String time = request.getParameter("time");
            String key = request.getParameter("key");

            sendJSON(new VersionServiceImpl().getVersionInfo(time, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
