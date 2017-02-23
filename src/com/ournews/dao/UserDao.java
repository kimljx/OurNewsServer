package com.ournews.dao;

import com.ournews.bean.User;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public interface UserDao {
    boolean register(String loginName, String password);

    User login(String loginName, String password);

    boolean changeInfo(String id, String nickName, String sex, String photo);
}
