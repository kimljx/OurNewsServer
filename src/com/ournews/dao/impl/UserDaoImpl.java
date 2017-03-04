package com.ournews.dao.impl;

import com.ournews.bean.User;
import com.ournews.dao.UserDao;
import com.ournews.utils.Constant;
import com.ournews.utils.MD5Util;
import com.ournews.utils.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class UserDaoImpl implements UserDao {

    @Override
    public boolean register(String loginName, String password) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO user ( loginname , password ,nickname ) VALUES ( ? , ? ,? )";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, loginName);
            result = preparedStatement.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return result;
    }

    @Override
    public User login(String loginName, String password, String time) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*),password,id,nickname,sex,photo FROM user WHERE loginname = \"" + loginName + "\"";
        User user = null;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        String md5 = MD5Util.getMD5(Constant.KEY + resultSet.getString(2) + time);
                        if (password.equals(md5)) {
                            String token = UUID.randomUUID().toString().replace("-", "");
                            sql = "UPDATE user SET token =\"" + token + "\" WHERE loginname =\"" + loginName + "\"";
                            SQLManager.closePreparedStatement(preparedStatement);
                            preparedStatement = connection.prepareStatement(sql);
                            int updateNum = preparedStatement.executeUpdate();
                            if (updateNum == 1) {
                                user = new User();
                                user.setId(resultSet.getLong(3));
                                user.setNickName(resultSet.getString(4));
                                user.setSex(resultSet.getInt(5));
                                user.setPhoto(resultSet.getString(6));
                                user.setLoginName(loginName);
                                user.setToken(token);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
        return user;
    }

    @Override
    public boolean changeInfo(String id, String nickName, String sex, String photo) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE user SET ";
        boolean had = false;
        if (nickName != null) {
            sql = sql + "nickname=\"" + nickName + "\"";
            had = true;
        }
        if (sex != null) {
            if (had)
                sql = sql + ",";
            sql = sql + "sex=\"" + sex + "\"";
            had = true;
        }
        if (photo != null) {
            if (had)
                sql = sql + ",";
            sql = sql + "photo=\"" + photo + "\"";
        }
        sql = sql + " WHERE id = \"" + id + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return result;
    }
}
