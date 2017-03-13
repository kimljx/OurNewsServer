package com.ournews.service;

/**
 * Created by Misutesu on 2017/3/4 0004.
 */
public interface UserService {
    String register(String loginName, String password, String time, String key);

    String login(String loginName, String password, String time);

    String checkLogin(String id, String token);

    String changeInfo(String id, String token, String nickName, String sex, String photo);
}
