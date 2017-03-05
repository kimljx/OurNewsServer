package com.ournews.dao.impl;

import com.ournews.dao.UserDao;
import com.ournews.utils.Constant;
import com.ournews.utils.MD5Util;
import com.ournews.utils.ResultUtil;
import com.ournews.utils.SQLManager;
import net.sf.json.JSONObject;

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
    public String register(String loginName, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO user ( loginname , password , nickname ) VALUES ( ? , ? , ? )";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, loginName);
            if (preparedStatement.executeUpdate() == 1) {
                return ResultUtil.getSuccessJSON("").toString();
            }
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_IS_EXIST).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_IS_EXIST).toString();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String login(String loginName, String password, String time) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*),password,id,nickname,sex,photo FROM user WHERE loginname = \"" + loginName + "\"";
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

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", resultSet.getLong(3));
                            jsonObject.put("nick_name", resultSet.getString(4));
                            jsonObject.put("sex", resultSet.getString(5));
                            jsonObject.put("photo", resultSet.getString(6));
                            jsonObject.put("login_name", loginName);
                            jsonObject.put("token", token);

                            sql = "UPDATE user SET token =\"" + token + "\" WHERE loginname =\"" + loginName + "\"";
                            SQLManager.closePreparedStatement(preparedStatement);
                            preparedStatement = connection.prepareStatement(sql);
                            int updateNum = preparedStatement.executeUpdate();
                            if (updateNum == 1) {
                                return ResultUtil.getSuccessJSON(jsonObject.toString()).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.PASSWORD_ERROR).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_NO_EXIST).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
    }

    @Override
    public int tokenIsTrue(String id, String token) {
        int result = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*) FROM user WHERE id = \"" + id + "\" AND token = \"" + token + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        result = 2;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            result = 1;
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
        return result;
    }

    @Override
    public String changeInfo(String id, String token, String nickName, String sex, String photo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*),token FROM user WHERE id = \"" + id + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        String sqlToken = resultSet.getString(2);
                        if (sqlToken.equals(token)) {
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "UPDATE user SET ";
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
                            preparedStatement = connection.prepareStatement(sql);
                            if (preparedStatement.executeUpdate() == 1) {
                                return ResultUtil.getSuccessJSON("").toString();
                            } else {
                                return ResultUtil.getErrorJSON(Constant.CHANGE_INFO_ERROR).toString();
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.CHANGE_INFO_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
    }
}
