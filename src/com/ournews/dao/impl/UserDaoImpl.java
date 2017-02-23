package com.ournews.dao.impl;

import com.ournews.bean.User;
import com.ournews.dao.UserDao;
import com.ournews.utils.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public User login(String loginName, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*),id,nickname,sex,photo FROM user WHERE loginname = \"" + loginName
                + "\" AND password = \"" + password + "\"";
        User user = null;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        user = new User();
                        user.setId(resultSet.getLong(2));
                        user.setNickName(resultSet.getString(3));
                        user.setSex(resultSet.getInt(4));
                        user.setPhoto(resultSet.getString(5));
                        user.setLoginName(loginName);
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
