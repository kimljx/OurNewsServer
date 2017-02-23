package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class UserRegisterAction extends BaseAction {

    private String loginName;
    private String password;

    @Override
    public void action() {
        loginName = request.getParameter("loginname");
        password = request.getParameter("password");
        userDao = new UserDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password)) {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        } else {
            if (MyUtils.isLoginName(loginName) && MyUtils.isPassword(password)) {
                if (userDao.register(loginName, password)) {
                    setResult(true);
                } else {
                    setResult(false);
                    setErrorCode(Constant.LOGIN_NAME_IS_EXIST);
                }
            } else {
                setResult(false);
                setErrorCode(Constant.NAME_OR_PASSWORD_LENGTH_ERROR);
            }
        }
    }
}
