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
        String sql = "SELECT count(1),n.id,n.title,n.cover,n.abstract,n.create_time,n.type,n.state,m.id,m.nick_name," +
                "m.sex,m.sign,m.birthday,m.photo " +
                "FROM news AS n,manager_user AS m " +
                "WHERE n.id = \"" + nid + "\"";
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
                            JSONObject managerJSON = new JSONObject();
                            managerJSON.put("id", resultSet.getLong(9));
                            managerJSON.put("nick_name", resultSet.getString(10));
                            managerJSON.put("sex", resultSet.getInt(11));
                            managerJSON.put("sign", resultSet.getString(12));
                            managerJSON.put("birthday", resultSet.getInt(13));
                            managerJSON.put("photo", resultSet.getString(14));
                            newJSON.put("manager", managerJSON);

                            SQLManager.closeResultSet(resultSet);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSet = preparedStatement.executeQuery();
                            if (resultSet != null && resultSet.next()) {
                                newJSON.put("comment_num", resultSet.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

                            SQLManager.closeResultSet(resultSet);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSet = preparedStatement.executeQuery();
                            if (resultSet != null && resultSet.next()) {
                                newJSON.put("collection_num", resultSet.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

                            SQLManager.closeResultSet(resultSet);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSet = preparedStatement.executeQuery();
                            if (resultSet != null && resultSet.next()) {
                                newJSON.put("history_num", resultSet.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

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
