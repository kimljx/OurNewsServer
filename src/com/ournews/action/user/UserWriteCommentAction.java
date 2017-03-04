package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserWriteCommentAction extends BaseAction {

    private String uId;
    private String nId;
    private String content;
    private String createTime;

    @Override
    public void action() {
        uId = request.getParameter("uid");
        nId = request.getParameter("nid");
        content = request.getParameter("content");
        createTime = request.getParameter("createtime");
        newDao = new NewDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNumber(uId) && MyUtils.isNumber(nId) && !MyUtils.isNull(content) && content.length() <= 200 && !MyUtils.isNull(createTime)) {
            int result = newDao.writeComment(Long.valueOf(uId), Long.valueOf(nId), content, createTime);
            if (result == -1) {
                setResult(false);
                setErrorCode(Constant.SERVER_ERROR);
            } else if (result == 0) {
                setResult(false);
                setErrorCode(Constant.USER_OR_NEW_NO_HAVE);
            } else {
                setResult(true);
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
