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
    public String getCode(String phone, String time, String key) {
        if (MyUtils.isNull(phone) || MyUtils.isNull(time) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isPhone(phone)) {
            return ResultUtil.getErrorJSON(Constant.PHONE_NUMBER_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(Constant.KEY + phone + time).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new UserDaoImpl().getCode(phone);
    }

    @Override
    public String registerManager(String phone, String code, String time, String key) {
        if (MyUtils.isNull(phone) || !MyUtils.isNumber(code) || MyUtils.isNull(time) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isPhone(phone)) {
            return ResultUtil.getErrorJSON(Constant.PHONE_NUMBER_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(Constant.KEY + code + time).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new UserDaoImpl().registerManager(phone, code);
    }

    @Override
    public String loginManager(String phone, String code, String time, String key) {
        if (MyUtils.isNull(phone) || MyUtils.isNull(code) || MyUtils.isNull(time)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isPhone(phone)) {
            return ResultUtil.getErrorJSON(Constant.PHONE_NUMBER_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(Constant.KEY + code + time).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new UserDaoImpl().loginManager(phone, code);
    }

    @Override
    public String changeManagerInfo(String id, String token, String nickName, String sex, String sign, String birthday, String photo) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (MyUtils.isNull(nickName) && MyUtils.isNull(sex)
                && MyUtils.isNull(sign) && MyUtils.isNull(birthday)
                && MyUtils.isNull(photo)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else {
            if ((!MyUtils.isNull(sex) && !MyUtils.isNumber(sex, 1, 2))
                    || (!MyUtils.isNull(sign) && sign.length() <= 50)
                    || (!MyUtils.isNull(birthday) && !MyUtils.isBirthday(birthday))) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            int isTrueToken = new UserDaoImpl().managerTokenIsTrue(id, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_TIME_OUT).toString();
            }
            if (MyUtils.isNull(nickName))
                nickName = null;
            if (MyUtils.isNull(sex))
                sex = null;
            if (MyUtils.isNull(sign))
                sign = null;
            if (MyUtils.isNull(birthday))
                birthday = null;
            if (MyUtils.isNull(photo))
                photo = null;
            return new UserDaoImpl().changeManagerInfo(id, nickName, sex, sign, birthday, photo);
        }
    }

    @Override
    public String register(String loginName, String password, String time, String key) {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password) || !MyUtils.isNull(time) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isLoginName(loginName)) {
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_LENGTH_ERROR).toString();
        } else if (!MyUtils.isPassword(password)) {
            return ResultUtil.getErrorJSON(Constant.PASSWORD_LENGTH_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(Constant.KEY + time).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new UserDaoImpl().register(loginName, password);
    }

    @Override
    public String login(String loginName, String password, String time) {
        if (MyUtils.isNull(loginName) || MyUtils.isNull(password)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isLoginName(loginName)) {
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_LENGTH_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        }
        return new UserDaoImpl().login(loginName, password, time);
    }

    @Override
    public String checkLogin(String id, String token) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        }
        return new UserDaoImpl().checkLogin(id);
    }

    @Override
    public String changeInfo(String id, String token, String nickName, String sex, String sign, String birthday, String photo) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (MyUtils.isNull(nickName) && MyUtils.isNull(sex)
                && MyUtils.isNull(sign) && MyUtils.isNull(birthday)
                && MyUtils.isNull(photo)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else {
            if ((!MyUtils.isNull(sex) && !MyUtils.isNumber(sex, 1, 2))
                    || (!MyUtils.isNull(sign) && sign.length() <= 50)
                    || (!MyUtils.isNull(birthday) && !MyUtils.isBirthday(birthday))) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            }
            if (MyUtils.isNull(nickName))
                nickName = null;
            if (MyUtils.isNull(sex))
                sex = null;
            if (MyUtils.isNull(sign))
                sign = null;
            if (MyUtils.isNull(birthday))
                birthday = null;
            if (MyUtils.isNull(photo))
                photo = null;
            return new UserDaoImpl().changeInfo(id, nickName, sex, sign, birthday, photo);
        }
    }
}
