package com.ournews.dao.impl;

import com.ournews.dao.NewDao;
import com.ournews.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class NewDaoImpl implements NewDao {

    @Override
    public String addNews(String title, String cover, String abstractContent, String content, String type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO news ( title , cover , abstract , content , createtime , type ) VALUES ( ? , ? , ? , ? , ? , ? )";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, cover);
            preparedStatement.setString(3, abstractContent);
            preparedStatement.setString(4, content);
            preparedStatement.setLong(5, System.currentTimeMillis());
            preparedStatement.setString(6, type);
            if (preparedStatement.executeUpdate() == 1) {
                MyUtils.zipImage(new File(ServletActionContext.getServletContext().getRealPath("upload"), cover));
                return ResultUtil.getSuccessJSON("").toString();
            }
            return ResultUtil.getErrorJSON(Constant.ADD_NEWS_ERROR).toString();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String getHomeNews(String selectType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id,title,cover,abstract,createtime FROM news WHERE state = \"1\" AND ";
        int rows = 0;
        try {
            connection = SQLManager.getConnection();
            if (selectType != null) {
                preparedStatement = connection.prepareStatement(
                        sql + "type = \"" + selectType + "\" ORDER BY id DESC limit 0," + Constant.RANDOM_NUM);
                resultSet = preparedStatement.executeQuery();
                if (resultSet != null) {
                    resultSet.last();
                    rows = resultSet.getRow();
                    if (rows != 0) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", selectType);
                        JSONArray jsonArray = new JSONArray();

                        List<Integer> randoms = MyUtils.getNumberList(rows);
                        for (Integer i : randoms) {
                            resultSet.first();
                            for (int n = 0; n < i - 1; n++) {
                                resultSet.next();
                            }
                            JSONObject json = new JSONObject();
                            json.put("id", resultSet.getLong(1));
                            json.put("title", resultSet.getString(2));
                            json.put("cover", resultSet.getString(3));
                            json.put("abstract", resultSet.getString(4));
                            json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                            json.put("type", selectType);
                            jsonArray.add(json);
                        }
                        jsonObject.put("list", jsonArray);
                        JSONArray array = new JSONArray();
                        array.add(jsonObject);
                        return ResultUtil.getSuccessJSON(array.toString()).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else {
                JSONArray array = new JSONArray();
                for (int type = 1; type < 6; type++) {
                    preparedStatement = connection.prepareStatement(
                            sql + "type = \"" + type + "\" ORDER BY id DESC limit 0," + Constant.RANDOM_NUM);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet != null) {
                        resultSet.last();
                        rows = resultSet.getRow();
                        if (rows != 0) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", type);
                            JSONArray jsonArray = new JSONArray();

                            List<Integer> randoms = MyUtils.getNumberList(rows);
                            for (Integer i : randoms) {
                                resultSet.first();
                                for (int n = 0; n < i - 1; n++) {
                                    resultSet.next();
                                }
                                JSONObject json = new JSONObject();
                                json.put("id", resultSet.getLong(1));
                                json.put("title", resultSet.getString(2));
                                json.put("cover", resultSet.getString(3));
                                json.put("abstract", resultSet.getString(4));
                                json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                                json.put("type", type);
                                jsonArray.add(json);
                            }
                            jsonObject.put("list", jsonArray);
                            array.add(jsonObject);
                        } else {
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                }
                preparedStatement = connection.prepareStatement(sql + "type = \" 6 \" ORDER BY id DESC limit 0,4");
                resultSet = preparedStatement.executeQuery();
                if (resultSet != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", 6);
                    JSONArray jsonArray = new JSONArray();
                    int i = 0;
                    while (resultSet.next() && i < 4) {
                        JSONObject json = new JSONObject();
                        json.put("id", resultSet.getLong(1));
                        json.put("title", resultSet.getString(2));
                        json.put("cover", resultSet.getString(3));
                        json.put("abstract", resultSet.getString(4));
                        json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                        json.put("type", 6);
                        jsonArray.add(json);
                        i++;
                    }
                    jsonObject.put("list", jsonArray);
                    array.add(jsonObject);
                    return ResultUtil.getSuccessJSON(array.toString()).toString();
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            }
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
    public String getNewList(String type, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id,title,cover,abstract,createtime FROM news WHERE state = \"1\" AND type = \"" + type + "\"";
        if (sort.equals("1"))
            sql = sql + " ORDER BY id DESC";
        sql = sql + " limit " + ((Integer.valueOf(page) - 1) * Integer.valueOf(size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            JSONArray jsonArray = new JSONArray();
            if (resultSet != null) {
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", resultSet.getLong(1));
                    jsonObject.put("title", resultSet.getString(2));
                    jsonObject.put("cover", resultSet.getString(3));
                    jsonObject.put("abstract", resultSet.getString(4));
                    jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                    jsonObject.put("type", type);
                    jsonArray.add(jsonObject);
                }
            }
            return ResultUtil.getSuccessJSON(jsonArray.toString()).toString();
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
    public String getSearchNew(String name, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id,title,cover,abstract,createtime,type FROM news WHERE state = \"1\" AND type != \"6\"";
        char[] cs = name.toCharArray();
        for (char c : cs) {
            sql = sql + " AND title LIKE \"%" + c + "%\"";
        }
        if (sort.equals("1"))
            sql = sql + " ORDER BY id DESC";
        sql = sql + " limit " + ((Integer.valueOf(page) - 1) * Integer.valueOf(size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                JSONArray jsonArray = new JSONArray();
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", resultSet.getLong(1));
                    jsonObject.put("title", resultSet.getString(2));
                    jsonObject.put("cover", resultSet.getString(3));
                    jsonObject.put("abstract", resultSet.getString(4));
                    jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                    jsonObject.put("type", resultSet.getInt(6));
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
    public String getNewContentUser(String uid, String nid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1) FROM user WHERE id = \"" + uid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        SQLManager.closeResultSet(resultSet);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "REPLACE INTO history ( uid , nid , time ) VALUES ( ? ,? ,? ) ";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, uid);
                        preparedStatement.setString(2, nid);
                        preparedStatement.setLong(3, System.currentTimeMillis());
                        preparedStatement.executeUpdate();
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "SELECT content FROM news WHERE id = \"" + nid + "\" AND state = 1";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                String content = resultSet.getString(1);
                                SQLManager.closeResultSet(resultSet);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSet = preparedStatement.executeQuery();
                                if (resultSet != null) {
                                    if (resultSet.next()) {
                                        int commentNum = resultSet.getInt(1);
                                        SQLManager.closeResultSet(resultSet);
                                        SQLManager.closePreparedStatement(preparedStatement);
                                        sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                        preparedStatement = connection.prepareStatement(sql);
                                        resultSet = preparedStatement.executeQuery();
                                        if (resultSet != null) {
                                            if (resultSet.next()) {
                                                int historyNum = resultSet.getInt(1);
                                                SQLManager.closeResultSet(resultSet);
                                                SQLManager.closePreparedStatement(preparedStatement);
                                                sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                                preparedStatement = connection.prepareStatement(sql);
                                                resultSet = preparedStatement.executeQuery();
                                                if (resultSet != null) {
                                                    if (resultSet.next()) {
                                                        int collectionNum = resultSet.getInt(1);
                                                        JSONObject jsonObject = new JSONObject();
                                                        jsonObject.put("id", nid);
                                                        jsonObject.put("content", content);
                                                        jsonObject.put("comment_num", commentNum);
                                                        jsonObject.put("history_num", historyNum);
                                                        jsonObject.put("collection_num", collectionNum);
                                                        return ResultUtil.getSuccessJSON(jsonObject.toString()).toString();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
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
    public String getNewContent(String nid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT content FROM news WHERE id = \"" + nid + "\" AND state = 1";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    String content = resultSet.getString(1);
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet != null) {
                        if (resultSet.next()) {
                            int commentNum = resultSet.getInt(1);
                            SQLManager.closeResultSet(resultSet);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSet = preparedStatement.executeQuery();
                            if (resultSet != null) {
                                if (resultSet.next()) {
                                    int historyNum = resultSet.getInt(1);
                                    SQLManager.closeResultSet(resultSet);
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                    preparedStatement = connection.prepareStatement(sql);
                                    resultSet = preparedStatement.executeQuery();
                                    if (resultSet != null) {
                                        if (resultSet.next()) {
                                            int collectionNum = resultSet.getInt(1);
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("id", nid);
                                            jsonObject.put("content", content);
                                            jsonObject.put("comment_num", commentNum);
                                            jsonObject.put("history_num", historyNum);
                                            jsonObject.put("collection_num", collectionNum);
                                            return ResultUtil.getSuccessJSON(jsonObject.toString()).toString();
                                        }
                                    }
                                }
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
            }
            return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
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
    public String collectNew(String uid, String nid, String type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "REPLACE INTO collection ( uid , nid ) VALUES ( ? ,? ) ";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uid);
            preparedStatement.setString(2, nid);
            if (preparedStatement.executeUpdate() == 1) {
                return ResultUtil.getSuccessJSON("").toString();
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

    @Override
    public String writeComment(String uId, String nId, String content) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT state FROM news WHERE id = \"" + nId + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        SQLManager.closeResultSet(resultSet);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "SELECT count(1) FROM user WHERE id = \"" + uId + "\"";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                if (resultSet.getInt(1) != 0) {
                                    SQLManager.closeResultSet(resultSet);
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sql = "INSERT INTO comment ( nid , uid , content , createtime ) VALUES ( ? , ? , ? , ? )";
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
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
                    }
                    return ResultUtil.getErrorJSON(Constant.NEW_NO_ONLINE).toString();
                }
            }
            return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
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
        String sql = "SELECT comment.id,comment.content,comment.createtime,user.id,user.nickname,user.sex,user.photo FROM comment,user WHERE comment.nid = \""
                + nid + "\" AND comment.uid = user.id AND state = 1";
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
            }
            return ResultUtil.getSuccessJSON(jsonArray.toString()).toString();
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
