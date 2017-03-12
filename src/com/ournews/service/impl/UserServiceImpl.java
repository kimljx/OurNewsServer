package com.ournews.service.impl;

import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.service.UserService;
import com.ournews.utils.Constant;
import com.ournews.utils.MD5Util;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;

/**
 * Created by Misutesu on 2017/3/4 0004.
 */
public class UserServiceImpl implements UserService {
    @Override
    public String register(String loginName, String password, String time, String key) {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password) || !MyUtils.isNumber(time)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isLoginName(loginName)) {
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_LENGTH_ERROR).toString();
        } else if (!MyUtils.isPassword(password)) {
            return ResultUtil.getErrorJSON(Constant.PASSWORD_LENGTH_ERROR).toString();
        } else if ((System.currentTimeMillis() - Long.valueOf(time)) > Constant.CONNECT_OUT_TIME) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(Constant.KEY + time).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new UserDaoImpl().register(loginName, password);
    }

    @Override
    public String login(String loginName, String password, String time, String umengToken) {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password) || !MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isLoginName(loginName)) {
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_LENGTH_ERROR).toString();
        } else if (MyUtils.isConnectTimeOut(Long.valueOf(time))) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        }
        if (MyUtils.isNull(umengToken))
            umengToken = "";
        return new UserDaoImpl().login(loginName, password, time, umengToken);
    }

    @Override
    public String checkLogin(String id, String token, String umengToken) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
        }
        if (MyUtils.isNull(umengToken))
            umengToken = "";
        return new UserDaoImpl().checkLogin(id, umengToken);
    }

    @Override
    public String changeInfo(String id, String token, String nickName, String sex, String photo) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token) || token.length() != 32) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (MyUtils.isNull(nickName) && MyUtils.isNull(sex) && MyUtils.isNull(photo)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else {
            int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
            }
            if (MyUtils.isNull(nickName))
                nickName = null;
            if (MyUtils.isNull(sex))
                sex = null;
            if (MyUtils.isNull(photo))
                photo = null;
            return new UserDaoImpl().changeInfo(id, token, nickName, sex, photo);
        }
    }
}
