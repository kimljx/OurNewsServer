package com.ournews.dao.impl;

import com.ournews.dao.CommentDao;
import com.ournews.utils.Constant;
import com.ournews.utils.DateUtil;
import com.ournews.utils.ResultUtil;
import com.ournews.utils.SQLManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public class CommentDaoImpl implements CommentDao {
    @Override
    public String writeComment(String uId, String nId, String content) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),state FROM news WHERE id = \"" + nId + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        if (resultSet.getInt(2) == 1) {
                            SQLManager.closeResultSet(resultSet);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "INSERT INTO comment ( nid , uid , content , create_time ) VALUES ( ? , ? , ? , ? )";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, nId);
                            preparedStatement.setString(2, uId);
                            preparedStatement.setString(3, content);
                            preparedStatement.setLong(4, System.currentTimeMillis());
                            if (preparedStatement.executeUpdate() == 1) {
                                return ResultUtil.getSuccessJSON("").toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.NEW_NO_ONLINE).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
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
    public String getComment(String nid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT comment.id,comment.content,comment.create_time,user.id,user.nick_name,user.sex,user.photo FROM comment,user WHERE comment.nid = \""
                + nid + "\" AND comment.uid = user.id AND user.state = 1 AND comment.state = 1";
        if (sort.equals("1"))
            sql = sql + " ORDER BY comment.id DESC";
        sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            JSONArray jsonArray = new JSONArray();
            if (resultSet != null) {
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", resultSet.getLong(1));
                    jsonObject.put("content", resultSet.getString(2));
                    jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(3)));

                    JSONObject userJSON = new JSONObject();
                    userJSON.put("id", resultSet.getLong(4));
                    userJSON.put("nick_name", resultSet.getString(5));
                    userJSON.put("sex", resultSet.getInt(6));
                    userJSON.put("photo", resultSet.getString(7));

                    jsonObject.put("user", userJSON);
                    jsonArray.add(jsonObject);
                }
                return ResultUtil.getSuccessJSON(jsonArray.toString()).toString();
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
    public String writeChildComment(String uid, String cid, String content) {
        return null;
    }
}
