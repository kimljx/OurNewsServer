package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.NewServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetNewCommentAction extends BaseAction {

    @Override
    public void action() throws IOException {
        String nid = request.getParameter("nid");
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String sort = request.getParameter("sort");

        sendJSON(new NewServiceImpl().getComment(nid, page, size, sort));
    }
}
