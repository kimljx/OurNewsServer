package com.ournews.dao;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public interface UserDao {
    String register(String loginName, String password);

    String login(String loginName, String password, String time, String umengToken);

    String checkLogin(String id, String umengToken);

    int tokenIsTrue(String id, String token);

    String changeInfo(String id, String token, String nickName, String sex, String photo);
}
