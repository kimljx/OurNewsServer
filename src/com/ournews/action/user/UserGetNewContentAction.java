package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.bean.New;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.DateUtil;
import com.ournews.utils.MyUtils;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetNewContentAction extends BaseAction {

    private String id;
    private String uid;

    @Override
    public void action() {
        id = request.getParameter("id");
        uid = request.getParameter("uid");
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
        if (MyUtils.isNumber(id)) {
            if ((!MyUtils.isNull(uid) && MyUtils.isNumber(uid)) || MyUtils.isNull(uid)) {
                if (MyUtils.isNull(uid)) {
                    getNewContent();
                } else {
                    int result = newDao.userBrowseNew(Long.valueOf(id), Long.valueOf(uid), DateUtil.getNowTime());
                    if (result != -2) {
                        getNewContent();
                    } else {
                        setResult(false);
                        setErrorCode(Constant.USER_NO_HAVE);
                    }
                }
            } else {
                setResult(false);
                setErrorCode(Constant.VALUES_ERROR);
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }

    private void getNewContent() {
        New n = newDao.getNewContent(Long.valueOf(id));
        if (n != null) {
            JSONObject newJSON = new JSONObject();
            newJSON.put("id", id);
            newJSON.put("content", n.getContent());
            newJSON.put("comment_num", n.getCommentNum());
            jsonObject.put("new", newJSON);
            setResult(true);
        } else {
            setResult(false);
            setErrorCode(Constant.NEW_NO_HAVE);
        }
    }
}
