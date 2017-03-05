package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetHomeNewsAction extends BaseAction {

    @Override
    public void action() throws IOException {
        String selectType = request.getParameter("type");

        sendJSON(new NewServiceImpl().getHomeNew(selectType));
    }
}
