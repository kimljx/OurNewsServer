package com.ournews.action.user;

import com.ournews.action.base.BaseAction;

/**
 * Created by Misutesu on 2017/1/24 0024.
 */
public class UserCollectNewAction extends BaseAction {

    private String id;
    private String uid;
    private String type;

    @Override
    public void action() {
        id = request.getParameter("id");
        uid = request.getParameter("uid");
        type = request.getParameter("type");
    }
}
