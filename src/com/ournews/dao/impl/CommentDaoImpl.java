package com.ournews.dao.impl;

import com.ournews.dao.CommentDao;
import com.ournews.utils.Constant;
import com.ournews.utils.DateUtil;
import com.ournews.utils.ResultUtil;
import com.ournews.utils.SQLManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;

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
                                return ResultUtil.getSuccessJSON(new JSONObject()).toString();
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
        ResultSet resultSetNum = null;
        ResultSet resultSetChild = null;
        String sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    JSONObject json = new JSONObject();
                    json.put("comment_num", resultSet.getInt(1));
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT c.id,c.content,c.create_time,u.id,u.nick_name,u.sex,u.sign,u.birthday,u.photo " +
                            "FROM comment AS c LEFT JOIN user AS u ON c.uid = u.id WHERE c.nid = \"" + nid + "\" AND u.state = 1 AND c.state = 1";
                    if (sort.equals("1"))
                        sql = sql + " ORDER BY c.id DESC";
                    sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    JSONArray jsonArray = new JSONArray();
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            long cid = resultSet.getLong(1);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", cid);
                            jsonObject.put("content", resultSet.getString(2));
                            jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(3)));
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("id", resultSet.getLong(4));
                            userJSON.put("nick_name", resultSet.getString(5));
                            userJSON.put("sex", resultSet.getInt(6));
                            userJSON.put("sign", resultSet.getString(7));
                            userJSON.put("birthday", resultSet.getString(8));
                            userJSON.put("photo", resultSet.getString(9));
                            jsonObject.put("user", userJSON);

                            SQLManager.closePreparedStatement(preparedStatement);
                            String sqlNum = "SELECT count(1) FROM comment_like WHERE cid = \"" + cid + "\"";
                            preparedStatement = connection.prepareStatement(sqlNum);
                            resultSetNum = preparedStatement.executeQuery();
                            if (resultSetNum != null) {
                                if (resultSetNum.next()) {
                                    jsonObject.put("is_like", -1);
                                    jsonObject.put("like_num", resultSetNum.getInt(1));
                                    SQLManager.closeResultSet(resultSetNum);
                                    SQLManager.closePreparedStatement(preparedStatement);

                                    String sqlChild = "SELECT c.id,c.content,c.create_time,u.id,u.nick_name,u.sex,u.sign,u.birthday,u.photo" +
                                            " FROM comment_child AS c LEFT JOIN user AS u ON c.uid = u.id WHERE c.cid = \""
                                            + cid + "\" AND c.state = 1 AND u.state = 1 limit 0 , 3";
                                    preparedStatement = connection.prepareStatement(sqlChild);
                                    resultSetChild = preparedStatement.executeQuery();
                                    JSONArray childArray = new JSONArray();

                                    if (resultSetChild != null) {
                                        while (resultSetChild.next()) {
                                            JSONObject childJSON = new JSONObject();
                                            childJSON.put("id", resultSetChild.getLong(1));
                                            childJSON.put("content", resultSetChild.getString(2));
                                            childJSON.put("create_time", DateUtil.getTime(resultSetChild.getLong(3)));
                                            JSONObject childUserJSON = new JSONObject();
                                            childUserJSON.put("id", resultSetChild.getLong(4));
                                            childUserJSON.put("nick_name", resultSetChild.getString(5));
                                            childUserJSON.put("sex", resultSetChild.getInt(6));
                                            childUserJSON.put("sign", resultSetChild.getString(7));
                                            childUserJSON.put("birthday", resultSetChild.getString(8));
                                            childUserJSON.put("photo", resultSetChild.getString(9));
                                            childJSON.put("user", childUserJSON);
                                            childArray.add(childJSON);
                                        }
                                    }
                                    jsonObject.put("comment_children", childArray);

                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sqlNum = "SELECT count(1) FROM comment_child WHERE cid = \"" + cid + "\"";
                                    preparedStatement = connection.prepareStatement(sqlNum);
                                    resultSetNum = preparedStatement.executeQuery();
                                    if (resultSetNum != null) {
                                        if (resultSetNum.next()) {
                                            jsonObject.put("child_num", resultSetNum.getInt(1));
                                        }
                                    }
                                }
                            }
                            jsonArray.add(jsonObject);
                        }
                        json.put("comments", jsonArray);
                        return ResultUtil.getSuccessJSON(json).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetChild);
            SQLManager.closeResultSet(resultSetNum);
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String getCommentUseUid(String uid, String nid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSetNum = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetChild = null;
        String sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    JSONObject json = new JSONObject();
                    json.put("comment_num", resultSet.getInt(1));
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT c.id,c.content,c.create_time,u.id,u.nick_name,u.sex,u.sign,u.birthday,u.photo " +
                            "FROM comment AS c LEFT JOIN user AS u ON c.uid = u.id WHERE c.nid = \"" + nid + "\" AND u.state = 1 AND c.state = 1";
                    if (sort.equals("1"))
                        sql = sql + " ORDER BY c.id DESC";
                    sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    JSONArray jsonArray = new JSONArray();
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            long cid = resultSet.getLong(1);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", cid);
                            jsonObject.put("content", resultSet.getString(2));
                            jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(3)));
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("id", resultSet.getLong(4));
                            userJSON.put("nick_name", resultSet.getString(5));
                            userJSON.put("sex", resultSet.getInt(6));
                            userJSON.put("sign", resultSet.getString(7));
                            userJSON.put("birthday", resultSet.getString(8));
                            userJSON.put("photo", resultSet.getString(9));
                            jsonObject.put("user", userJSON);

                            SQLManager.closePreparedStatement(preparedStatement);
                            String sqlNum = "SELECT count(1) FROM comment_like WHERE cid = \"" + cid + "\"";
                            preparedStatement = connection.prepareStatement(sqlNum);
                            resultSetNum = preparedStatement.executeQuery();
                            if (resultSetNum != null) {
                                if (resultSetNum.next()) {
                                    jsonObject.put("like_num", resultSetNum.getInt(1));
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    String getUserLike = "SELECT count(1) FROM comment_like WHERE cid = \"" + cid + "\" AND uid = \"" + uid + "\"";
                                    preparedStatement = connection.prepareStatement(getUserLike);
                                    resultSetUser = preparedStatement.executeQuery();
                                    if (resultSetUser != null) {
                                        if (resultSetUser.next()) {
                                            jsonObject.put("is_like", resultSetUser.getInt(1));
                                            SQLManager.closeResultSet(resultSetNum);
                                            SQLManager.closePreparedStatement(preparedStatement);

                                            String sqlChild = "SELECT c.id,c.content,c.create_time,u.id,u.nick_name,u.sex,u.sign,u.birthday,u.photo" +
                                                    " FROM comment_child AS c LEFT JOIN user AS u ON c.uid = u.id WHERE c.cid = \""
                                                    + cid + "\" AND c.state = 1 AND u.state = 1 limit 0 , 3";
                                            preparedStatement = connection.prepareStatement(sqlChild);
                                            resultSetChild = preparedStatement.executeQuery();
                                            JSONArray childArray = new JSONArray();

                                            if (resultSetChild != null) {
                                                while (resultSetChild.next()) {
                                                    JSONObject childJSON = new JSONObject();
                                                    childJSON.put("id", resultSetChild.getLong(1));
                                                    childJSON.put("content", resultSetChild.getString(2));
                                                    childJSON.put("create_time", DateUtil.getTime(resultSetChild.getLong(3)));
                                                    JSONObject childUserJSON = new JSONObject();
                                                    childUserJSON.put("id", resultSetChild.getLong(4));
                                                    childUserJSON.put("nick_name", resultSetChild.getString(5));
                                                    childUserJSON.put("sex", resultSetChild.getInt(6));
                                                    childUserJSON.put("sign", resultSetChild.getString(7));
                                                    childUserJSON.put("birthday", resultSetChild.getString(8));
                                                    childUserJSON.put("photo", resultSetChild.getString(9));
                                                    childJSON.put("user", childUserJSON);
                                                    childArray.add(childJSON);
                                                }
                                            }
                                            jsonObject.put("comment_children", childArray);

                                            SQLManager.closePreparedStatement(preparedStatement);
                                            sqlNum = "SELECT count(1) FROM comment_child WHERE cid = \"" + cid + "\"";
                                            preparedStatement = connection.prepareStatement(sqlNum);
                                            resultSetNum = preparedStatement.executeQuery();
                                            if (resultSetNum != null) {
                                                if (resultSetNum.next()) {
                                                    jsonObject.put("child_num", resultSetNum.getInt(1));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            jsonArray.add(jsonObject);
                        }
                        json.put("comments", jsonArray);
                        return ResultUtil.getSuccessJSON(json).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetChild);
            SQLManager.closeResultSet(resultSetUser);
            SQLManager.closeResultSet(resultSetNum);
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String likeComment(String cid, String uid, String type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),state FROM comment WHERE id = \"" + cid + "\"";
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
                            if (Integer.valueOf(type) == 0) {
                                sql = "INSERT INTO comment_like ( uid , cid ) VALUES ( ? , ? )";
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1, uid);
                                preparedStatement.setString(2, cid);
                                if (preparedStatement.executeUpdate() == 1) {
                                    return ResultUtil.getSuccessJSON(new JSONObject()).toString();
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.HAS_LIKE_COMMENT).toString();
                                }
                            } else {
                                sql = "DELETE FROM comment_like WHERE uid = \"" + uid + "\" AND cid = \"" + cid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                if (preparedStatement.executeUpdate() == 1) {
                                    return ResultUtil.getSuccessJSON(new JSONObject()).toString();
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.NO_LIKE_COMMENT).toString();
                                }
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.COMMENT_NO_ONLINE).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.NO_COMMENT).toString();
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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),state FROM comment WHERE id = \"" + cid + "\"";
        try {
            connection = SQLManager.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        if (resultSet.getInt(2) == 1) {
                            String sql1 = "INSERT INTO comment_child ( cid , uid , content , create_time ) " +
                                    "VALUES ( \"" + cid + "\" , \"" + uid + "\" , \""
                                    + content + "\" , \"" + System.currentTimeMillis() + "\" )";
                            String sql2 = "INSERT INTO message ( uid , type , cid , create_time ) " +
                                    "VALUES ( \"" + uid + "\" , \"0\" , \""
                                    + cid + "\" , \"" + System.currentTimeMillis() + "\" )";
                            statement = connection.createStatement();
                            statement.addBatch(sql1);
                            statement.addBatch(sql2);
                            int[] result = statement.executeBatch();
                            if (result[0] == 0 || result[1] == 0) {
                                connection.rollback();
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            } else {
                                connection.commit();
                                new PushDaoImpl().pushChildCommentToUser(uid, cid, content);
                                return ResultUtil.getSuccessJSON(new JSONObject()).toString();
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.COMMENT_NO_ONLINE).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.NO_COMMENT).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            SQLManager.rollbackConnection(connection);
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closeStatement(statement);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String getChildComment(String cid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1) FROM comment_child WHERE cid = \"" + cid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    JSONObject json = new JSONObject();
                    json.put("child_num", resultSet.getInt(1));
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT c.id,c.content,c.create_time,u.id,u.nick_name,u.sex,u.sign,u.birthday,u.photo " +
                            "FROM comment_child AS c LEFT JOIN user AS u ON c.uid = u.id WHERE c.cid = \"" + cid + "\" AND u.state = 1 AND c.state = 1";
                    if (sort.equals("1"))
                        sql = sql + " ORDER BY c.id DESC";
                    sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
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
                            userJSON.put("sign", resultSet.getString(8));
                            userJSON.put("birthday", resultSet.getString(9));
                            jsonObject.put("user", userJSON);
                            jsonArray.add(jsonObject);
                        }
                        json.put("comment_children", jsonArray);
                        return ResultUtil.getSuccessJSON(json).toString();
                    }
                }
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
    public String getCommentMessage(String uid, String page, String size) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1)" +
                " FROM message AS m,comment_child AS cc,comment AS c,user AS u" +
                " WHERE m.uid = \"" + uid + "\" AND m.type =\"1\" AND m.state = \"1\"" +
                " AND m.cid = cc.id AND AND cc.cid = c.id cc.uid = u.id AND cc.state = 1 AND c.state= 1 AND u.state = 1";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("unread_num", resultSet.getInt(1));
                SQLManager.closeResultSet(resultSet);
                SQLManager.closePreparedStatement(preparedStatement);
                sql = "SELECT m.id,m.cid,m.state,cc.content,cc.create_time,c,id,c.content,u.id,u.nick_name.u.sex,u.sign,u.birthday,u.photo" +
                        " FROM message AS m,comment_child AS cc,comment AS c,user AS u" +
                        " WHERE m.uid = \"" + uid + "\" AND m.cid = cc.id AND cc.cid = c.id AND cc.uid = u.id" +
                        " cc.state = 1 AND c.state = 1 AND u.state = 1 limit ";
                sql = sql + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
                if (resultSet != null) {
                    JSONArray jsonArray = new JSONArray();
                    while (resultSet.next()) {
                        JSONObject messageJSON = new JSONObject();
                        messageJSON.put("id", resultSet.getLong(1));
                        messageJSON.put("state", resultSet.getInt(3));
                        messageJSON.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                        JSONObject commentChildJSON = new JSONObject();
                        commentChildJSON.put("id", resultSet.getLong(2));
                        commentChildJSON.put("content", resultSet.getString(4));
                        JSONObject commentJSON = new JSONObject();
                        commentJSON.put("id", resultSet.getLong(6));
                        commentJSON.put("content", resultSet.getString(7));
                        JSONObject userJSON = new JSONObject();
                        userJSON.put("id", resultSet.getLong(8));
                        userJSON.put("nick_name", resultSet.getString(9));
                        userJSON.put("sex", resultSet.getInt(10));
                        userJSON.put("sign", resultSet.getString(11));
                        userJSON.put("birthday", resultSet.getInt(12));
                        userJSON.put("photo", resultSet.getString(13));
                        commentChildJSON.put("user", userJSON);
                        commentJSON.put("child", commentChildJSON);
                        messageJSON.put("comment", commentJSON);
                        jsonArray.add(messageJSON);
                    }
                    jsonObject.put("message", jsonArray);
                    return ResultUtil.getSuccessJSON(jsonObject).toString();
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
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
}
