package com.ournews.dao.impl;

import com.ournews.dao.PushDao;
import com.ournews.utils.*;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/11.
 */
public class PushDaoImpl implements PushDao {
    @Override
    public String pushNewToAll(String nid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),id,title,cover,abstract,create_time,type,state FROM news WHERE id = \"" + nid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        if (resultSet.getInt(8) == 1) {
                            JSONObject newJSON = new JSONObject();
                            newJSON.put("id", resultSet.getLong(2));
                            newJSON.put("title", resultSet.getString(3));
                            newJSON.put("cover", resultSet.getString(4));
                            newJSON.put("abstract", resultSet.getString(5));
                            newJSON.put("create_time", DateUtil.getTime(resultSet.getLong(6)));
                            newJSON.put("type", resultSet.getString(7));
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "new");
                            jsonObject.put("data", newJSON);
                            return PushUtil.pushNewToAll(newJSON.getString("title"), jsonObject.toString());
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
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
    }

    @Override
    public void pushChildCommentToUser(String uid, String cid, String content) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT user.id FROM comment,user WHERE comment.id = \"" + cid + "\" AND comment.uid = user.id";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    String id = resultSet.getString(1);
                    if (!id.equals(uid)) {
                        SQLManager.closeResultSet(resultSet);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "SELECT nick_name FROM user WHERE id = \"" + uid + "\"";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                String otherUserName = resultSet.getString(1);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("type", "child_comment");
                                jsonObject.put("data", new JSONObject());
                                PushUtil.pushChildCommentToUser(id, otherUserName, jsonObject.toString());
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }
}
