package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;

import java.io.IOException;

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
        newPresenter = new NewDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNumber(id) && MyUtils.isNumber(uid) && MyUtils.isNumber(type, 1, 2)) {

        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
