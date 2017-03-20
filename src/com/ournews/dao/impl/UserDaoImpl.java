package com.ournews.dao.impl;

import com.ournews.dao.UserDao;
import com.ournews.utils.*;
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
    public String getCode(String phone) {
        int code = CodeUtil.getCode(phone);
        if (code != -1) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            String sql = "INSERT INTO message_code ( phone , code , create_time ) VALUES ( ? , ? , ? )";
            try {
                connection = SQLManager.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, phone);
                preparedStatement.setInt(2, code);
                preparedStatement.setLong(3, System.currentTimeMillis());
                if (preparedStatement.executeUpdate() == 1) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone", phone);
                    jsonObject.put("code", code);
                    return ResultUtil.getSuccessJSON(jsonObject).toString();
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } finally {
                SQLManager.closePreparedStatement(preparedStatement);
                SQLManager.closeConnection(connection);
            }
        }
        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
    }

    @Override
    public String registerManager(String phone, String password, String code) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),code,create_time FROM message_code WHERE phone = \"" + phone + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        long time = System.currentTimeMillis() - resultSet.getLong(3);
                        if (Constant.IS_DEBUG || time < 5 * 60 * 1000) {
                            if (code.equals(resultSet.getString(2))) {
                                SQLManager.closeResultSet(resultSet);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "INSERT INTO user ( login_name , password , nick_name , manager ) VALUES ( ? , ? , ? ,? )";
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1, phone);
                                preparedStatement.setString(2, password);
                                preparedStatement.setString(3, phone);
                                preparedStatement.setInt(4, 1);
                                if (preparedStatement.executeUpdate() == 1) {
                                    SQLManager.closeResultSet(resultSet);
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sql = "SELECT count(1),id,nick_name,sex,sign,birthday,photo FROM user WHERE phone = \"" + phone + "\"";
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
                                                    jsonObject.put("login_name", phone);
                                                    jsonObject.put("nick_name", resultSet.getString(4));
                                                    jsonObject.put("sex", resultSet.getString(5));
                                                    jsonObject.put("sign", resultSet.getString(6));
                                                    jsonObject.put("birthday", resultSet.getString(7));
                                                    jsonObject.put("photo", resultSet.getString(8));
                                                    jsonObject.put("token", token);

                                                    sql = "UPDATE user SET token = \"" + token + "\" login_time = \"" + System.currentTimeMillis() + " WHERE login_name =\"" + phone + "\"";
                                                    SQLManager.closePreparedStatement(preparedStatement);
                                                    preparedStatement = connection.prepareStatement(sql);
                                                    int updateNum = preparedStatement.executeUpdate();
                                                    if (updateNum == 1) {
                                                        return ResultUtil.getSuccessJSON(jsonObject).toString();
                                                    }
                                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                                }
                                                return ResultUtil.getErrorJSON(Constant.PASSWORD_ERROR).toString();
                                            }
                                        }
                                        return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_NO_EXIST).toString();
                                    }
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.MESSAGE_CODE_ERROR).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.MESSAGE_CODE_TIME_OUT).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.PLEASE_GET_CODE_FIRST).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String loginManager(String phone, String password, String time) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),password,id,nick_name,sex,sign,birthday,photo FROM user WHERE phone = \"" + phone + "\"";
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
                            jsonObject.put("login_name", phone);
                            jsonObject.put("nick_name", resultSet.getString(4));
                            jsonObject.put("sex", resultSet.getString(5));
                            jsonObject.put("sign", resultSet.getString(6));
                            jsonObject.put("birthday", resultSet.getString(7));
                            jsonObject.put("photo", resultSet.getString(8));
                            jsonObject.put("token", token);

                            sql = "UPDATE user SET token = \"" + token + "\" login_time = \"" + System.currentTimeMillis() + " WHERE login_name =\"" + phone + "\"";
                            SQLManager.closePreparedStatement(preparedStatement);
                            preparedStatement = connection.prepareStatement(sql);
                            int updateNum = preparedStatement.executeUpdate();
                            if (updateNum == 1) {
                                return ResultUtil.getSuccessJSON(jsonObject).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.PASSWORD_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_NO_EXIST).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_IS_EXIST).toString();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String register(String loginName, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO user ( login_name , password , nick_name ) VALUES ( ? , ? , ? )";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, loginName);
            if (preparedStatement.executeUpdate() == 1) {
                return ResultUtil.getSuccessJSON(new JSONObject()).toString();
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
        String sql = "SELECT count(1),password,id,nick_name,sex,sign,birthday,photo FROM user WHERE login_name = \"" + loginName + "\"";
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
                            jsonObject.put("login_name", loginName);
                            jsonObject.put("nick_name", resultSet.getString(4));
                            jsonObject.put("sex", resultSet.getString(5));
                            jsonObject.put("sign", resultSet.getString(6));
                            jsonObject.put("birthday", resultSet.getString(7));
                            jsonObject.put("photo", resultSet.getString(8));
                            jsonObject.put("token", token);

                            sql = "UPDATE user SET token = \"" + token + "\" WHERE login_name =\"" + loginName + "\"";
                            SQLManager.closePreparedStatement(preparedStatement);
                            preparedStatement = connection.prepareStatement(sql);
                            int updateNum = preparedStatement.executeUpdate();
                            if (updateNum == 1) {
                                return ResultUtil.getSuccessJSON(jsonObject).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.PASSWORD_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_NO_EXIST).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
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
    public String checkLogin(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT state,login_name,nick_name,sex,sign,birthday,photo FROM user WHERE id = \"" + id + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", id);
                        jsonObject.put("login_name", resultSet.getString(2));
                        jsonObject.put("nick_name", resultSet.getString(3));
                        jsonObject.put("sex", resultSet.getInt(4));
                        jsonObject.put("sign", resultSet.getString(5));
                        jsonObject.put("birthday", resultSet.getString(6));
                        jsonObject.put("photo", resultSet.getString(7));
                        return ResultUtil.getSuccessJSON(jsonObject).toString();
                    }
                    return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
                }
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.LOGIN_NAME_IS_EXIST).toString();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public int tokenIsTrue(String id, String token) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),state,token FROM user WHERE id = \"" + id + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        if (resultSet.getInt(2) == 1) {
                            if (!Constant.IS_DEBUG)
                                if (!token.equals(resultSet.getString(3)))
                                    return 4;
                            return 5;
                        }
                        return 3;
                    }
                }
                return 2;
            }
            return 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 1;
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
    }

    @Override
    public String changeInfo(String id, String token, String nickName, String sex, String sign, String birthday, String photo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE user SET ";
        boolean had = false;
        if (nickName != null) {
            sql = sql + "nick_name=\"" + nickName + "\"";
            had = true;
        }
        if (sex != null) {
            if (had)
                sql = sql + ",";
            sql = sql + "sex=\"" + sex + "\"";
            had = true;
        }
        if (sign != null) {
            if (had)
                sql = sql + ",";
            sql = sql + "sign=\"" + sign + "\"";
            had = true;
        }
        if (birthday != null) {
            if (had)
                sql = sql + ",";
            sql = sql + "birthday=\"" + birthday + "\"";
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
            if (preparedStatement.executeUpdate() == 1) {
                return ResultUtil.getSuccessJSON(new JSONObject()).toString();
            }
            return ResultUtil.getErrorJSON(Constant.CHANGE_INFO_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }
}
