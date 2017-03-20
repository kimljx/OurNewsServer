package com.ournews.dao;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public interface UserDao {
    String getCode(String phone);

    String registerManager(String phone, String password, String code);

    String loginManager(String phone, String password, String time);

    String register(String loginName, String password);

    String login(String loginName, String password, String time);

    String checkLogin(String id);

    int tokenIsTrue(String id, String token);

    String changeInfo(String id, String token, String nickName, String sex, String sign, String birthday, String photo);
}
