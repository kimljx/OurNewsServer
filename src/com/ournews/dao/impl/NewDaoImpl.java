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
    public String addNews(String mid, String title, String cover, String abstractContent, String content, String type, String push) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "INSERT INTO news ( mid , title , cover , abstract , content , create_time , type ) VALUES ( ? , ? , ? , ? , ? , ? )";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mid);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, cover);
            preparedStatement.setString(4, abstractContent);
            preparedStatement.setString(5, content);
            preparedStatement.setLong(6, System.currentTimeMillis());
            preparedStatement.setString(7, type);
            if (preparedStatement.executeUpdate() == 1) {
                MyUtils.zipImage(new File(ServletActionContext.getServletContext().getRealPath("upload"), cover));
                if (push.equals("1")) {
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT max(id) FROM news";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet != null && resultSet.next()) {
                        String nid = resultSet.getString(1);
                        new PushDaoImpl().pushNewToAll(nid);
                    }
                }
                return ResultUtil.getSuccessJSON(new JSONObject()).toString();
            }
            return ResultUtil.getErrorJSON(Constant.ADD_NEWS_ERROR).toString();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String getOwnNew(String mid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,n.type,n.state" +
                " FROM news AS n WHERE  n.mid = \"" + mid + "\"";
        if (sort.equals("1"))
            sql = sql + " ORDER BY n.id DESC";
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
                    jsonObject.put("state", resultSet.getInt(7));
                    jsonArray.add(jsonObject);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("news", jsonArray);
                return ResultUtil.getSuccessJSON(jsonObject).toString();
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
    public String getHomeNews(String selectType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSetNum = null;
        String sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo" +
                " FROM news AS n,manager_user AS m WHERE n.state = \"1\" AND ";
        int rows;
        try {
            connection = SQLManager.getConnection();
            if (selectType != null) {
                preparedStatement = connection.prepareStatement(
                        sql + "n.type = \"" + selectType + "\" ORDER BY n.id DESC limit 0," + Constant.RANDOM_NUM);
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
                            long nid = resultSet.getLong(1);
                            json.put("id", nid);
                            json.put("title", resultSet.getString(2));
                            json.put("cover", resultSet.getString(3));
                            json.put("abstract", resultSet.getString(4));
                            json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                            json.put("type", selectType);
                            JSONObject managerJSON = new JSONObject();
                            managerJSON.put("id", resultSet.getLong(6));
                            managerJSON.put("nick_name", resultSet.getString(7));
                            managerJSON.put("sex", resultSet.getInt(8));
                            managerJSON.put("sign", resultSet.getString(9));
                            managerJSON.put("birthday", resultSet.getInt(10));
                            managerJSON.put("photo", resultSet.getString(11));
                            json.put("manager", managerJSON);

                            SQLManager.closeResultSet(resultSetNum);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSetNum = preparedStatement.executeQuery();
                            if (resultSetNum != null && resultSetNum.next()) {
                                json.put("comment_num", resultSetNum.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

                            SQLManager.closeResultSet(resultSetNum);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSetNum = preparedStatement.executeQuery();
                            if (resultSetNum != null && resultSetNum.next()) {
                                json.put("collection_num", resultSetNum.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

                            SQLManager.closeResultSet(resultSetNum);
                            SQLManager.closePreparedStatement(preparedStatement);
                            sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                            preparedStatement = connection.prepareStatement(sql);
                            resultSetNum = preparedStatement.executeQuery();
                            if (resultSetNum != null && resultSetNum.next()) {
                                json.put("history_num", resultSetNum.getInt(1));
                            } else {
                                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                            }

                            jsonArray.add(json);
                        }
                        jsonObject.put("list", jsonArray);
                        JSONArray array = new JSONArray();
                        array.add(jsonObject);
                        JSONObject json = new JSONObject();
                        json.put("news", array);
                        return ResultUtil.getSuccessJSON(json).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else {
                JSONArray array = new JSONArray();
                for (int type = 1; type < 6; type++) {
                    preparedStatement = connection.prepareStatement(
                            sql + "n.type = \"" + type + "\" ORDER BY n.id DESC limit 0," + Constant.RANDOM_NUM);
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
                                long nid = resultSet.getLong(1);
                                json.put("id", nid);
                                json.put("title", resultSet.getString(2));
                                json.put("cover", resultSet.getString(3));
                                json.put("abstract", resultSet.getString(4));
                                json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                                json.put("type", type);
                                JSONObject managerJSON = new JSONObject();
                                managerJSON.put("id", resultSet.getLong(6));
                                managerJSON.put("nick_name", resultSet.getString(7));
                                managerJSON.put("sex", resultSet.getInt(8));
                                managerJSON.put("sign", resultSet.getString(9));
                                managerJSON.put("birthday", resultSet.getInt(10));
                                managerJSON.put("photo", resultSet.getString(11));
                                json.put("manager", managerJSON);

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                String sql1 = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                                preparedStatement = connection.prepareStatement(sql1);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    json.put("comment_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql1 = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql1);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    json.put("collection_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql1 = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql1);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    json.put("history_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

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
                preparedStatement = connection.prepareStatement(sql + "n.type = \" 6 \" ORDER BY n.id DESC limit 0,4");
                resultSet = preparedStatement.executeQuery();
                if (resultSet != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", 6);
                    JSONArray jsonArray = new JSONArray();
                    int i = 0;
                    while (resultSet.next() && i < 4) {
                        JSONObject json = new JSONObject();
                        long nid = resultSet.getLong(1);
                        json.put("id", nid);
                        json.put("title", resultSet.getString(2));
                        json.put("cover", resultSet.getString(3));
                        json.put("abstract", resultSet.getString(4));
                        json.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                        json.put("type", 6);
                        JSONObject managerJSON = new JSONObject();
                        managerJSON.put("id", resultSet.getLong(6));
                        managerJSON.put("nick_name", resultSet.getString(7));
                        managerJSON.put("sex", resultSet.getInt(8));
                        managerJSON.put("sign", resultSet.getString(9));
                        managerJSON.put("birthday", resultSet.getInt(10));
                        managerJSON.put("photo", resultSet.getString(11));
                        json.put("manager", managerJSON);

                        SQLManager.closeResultSet(resultSetNum);
                        SQLManager.closePreparedStatement(preparedStatement);
                        String sql1 = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                        preparedStatement = connection.prepareStatement(sql1);
                        resultSetNum = preparedStatement.executeQuery();
                        if (resultSetNum != null && resultSetNum.next()) {
                            json.put("comment_num", resultSetNum.getInt(1));
                        } else {
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }

                        SQLManager.closeResultSet(resultSetNum);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql1 = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                        preparedStatement = connection.prepareStatement(sql1);
                        resultSetNum = preparedStatement.executeQuery();
                        if (resultSetNum != null && resultSetNum.next()) {
                            json.put("collection_num", resultSetNum.getInt(1));
                        } else {
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }

                        SQLManager.closeResultSet(resultSetNum);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql1 = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                        preparedStatement = connection.prepareStatement(sql1);
                        resultSetNum = preparedStatement.executeQuery();
                        if (resultSetNum != null && resultSetNum.next()) {
                            json.put("history_num", resultSetNum.getInt(1));
                        } else {
                            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                        }

                        jsonArray.add(json);
                        i++;
                    }
                    jsonObject.put("list", jsonArray);
                    array.add(jsonObject);
                    JSONObject json = new JSONObject();
                    json.put("news", array);
                    return ResultUtil.getSuccessJSON(json).toString();
                }
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetNum);
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
        ResultSet resultSetNum = null;
        String sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo" +
                " FROM news AS n,manager_user AS m WHERE n.state = \"1\" AND n.type = \"" + type + "\"";
        if (sort.equals("1"))
            sql = sql + " ORDER BY n.id DESC";
        sql = sql + " limit " + ((Integer.valueOf(page) - 1) * Integer.valueOf(size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            JSONArray jsonArray = new JSONArray();
            if (resultSet != null) {
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    long nid = resultSet.getLong(1);
                    jsonObject.put("id", nid);
                    jsonObject.put("title", resultSet.getString(2));
                    jsonObject.put("cover", resultSet.getString(3));
                    jsonObject.put("abstract", resultSet.getString(4));
                    jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                    jsonObject.put("type", type);
                    JSONObject managerJSON = new JSONObject();
                    managerJSON.put("id", resultSet.getLong(6));
                    managerJSON.put("nick_name", resultSet.getString(7));
                    managerJSON.put("sex", resultSet.getInt(8));
                    managerJSON.put("sign", resultSet.getString(9));
                    managerJSON.put("birthday", resultSet.getInt(10));
                    managerJSON.put("photo", resultSet.getString(11));
                    jsonObject.put("manager", managerJSON);

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("comment_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("collection_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("history_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    jsonArray.add(jsonObject);
                }
            }
            JSONObject json = new JSONObject();
            json.put("news", jsonArray);
            return ResultUtil.getSuccessJSON(json).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetNum);
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
        ResultSet resultSetNum = null;
        String sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,n.type,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo" +
                " FROM news AS n,manager_user AS m WHERE state = \"1\" AND type != \"6\"";
        char[] cs = name.toCharArray();
        for (char c : cs) {
            sql = sql + " AND n.title LIKE \"%" + c + "%\"";
        }
        if (sort.equals("1"))
            sql = sql + " ORDER BY n.id DESC";
        sql = sql + " limit " + ((Integer.valueOf(page) - 1) * Integer.valueOf(size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                JSONArray jsonArray = new JSONArray();
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    long nid = resultSet.getLong(1);
                    jsonObject.put("id", nid);
                    jsonObject.put("title", resultSet.getString(2));
                    jsonObject.put("cover", resultSet.getString(3));
                    jsonObject.put("abstract", resultSet.getString(4));
                    jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                    jsonObject.put("type", resultSet.getInt(6));
                    JSONObject managerJSON = new JSONObject();
                    managerJSON.put("id", resultSet.getLong(7));
                    managerJSON.put("nick_name", resultSet.getString(8));
                    managerJSON.put("sex", resultSet.getInt(9));
                    managerJSON.put("sign", resultSet.getString(10));
                    managerJSON.put("birthday", resultSet.getInt(11));
                    managerJSON.put("photo", resultSet.getString(12));
                    jsonObject.put("manager", managerJSON);

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("comment_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("collection_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    SQLManager.closeResultSet(resultSetNum);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSetNum = preparedStatement.executeQuery();
                    if (resultSetNum != null && resultSetNum.next()) {
                        jsonObject.put("history_num", resultSetNum.getInt(1));
                    } else {
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }

                    jsonArray.add(jsonObject);
                }
                JSONObject json = new JSONObject();
                json.put("news", jsonArray);
                return ResultUtil.getSuccessJSON(json).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetNum);
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
                        sql = "SELECT state,content FROM news WHERE id = \"" + nid + "\"";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                if (resultSet.getInt(1) == 1) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("id", nid);
                                    jsonObject.put("content", resultSet.getString(2));
                                    SQLManager.closeResultSet(resultSet);
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\" AND uid =\"" + uid + "\"";
                                    preparedStatement = connection.prepareStatement(sql);
                                    resultSet = preparedStatement.executeQuery();
                                    if (resultSet != null) {
                                        if (resultSet.next()) {
                                            jsonObject.put("is_collected", resultSet.getInt(1));
                                            return ResultUtil.getSuccessJSON(jsonObject).toString();
                                        }
                                    }
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }
                                return ResultUtil.getErrorJSON(Constant.NEW_NO_ONLINE).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
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
    public String getNewContent(String nid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT state,content FROM news WHERE id = \"" + nid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", nid);
                        jsonObject.put("content", resultSet.getString(2));
                        jsonObject.put("is_collected", -1);
                        return ResultUtil.getSuccessJSON(jsonObject).toString();
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
    public String getNewContentUserForWeb(String uid, String nid) {
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
                        sql = "SELECT n.state,n.content,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo FROM news AS n " +
                                "LEFT JOIN manager_user AS m ON n.mid = m.id WHERE n.id = \"" + nid + "\"";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                if (resultSet.getInt(1) == 1) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("content", resultSet.getString(2));
                                    JSONObject managerJSON = new JSONObject();
                                    managerJSON.put("id", resultSet.getLong(3));
                                    managerJSON.put("nick_name", resultSet.getString(4));
                                    managerJSON.put("sex", resultSet.getInt(5));
                                    managerJSON.put("sign", resultSet.getString(6));
                                    managerJSON.put("birthday", resultSet.getInt(7));
                                    managerJSON.put("photo", resultSet.getString(8));
                                    SQLManager.closeResultSet(resultSet);
                                    SQLManager.closePreparedStatement(preparedStatement);
                                    sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                                    preparedStatement = connection.prepareStatement(sql);
                                    resultSet = preparedStatement.executeQuery();
                                    if (resultSet != null) {
                                        if (resultSet.next()) {
                                            jsonObject.put("comment_num", resultSet.getInt(1));
                                            SQLManager.closeResultSet(resultSet);
                                            SQLManager.closePreparedStatement(preparedStatement);
                                            sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                            preparedStatement = connection.prepareStatement(sql);
                                            resultSet = preparedStatement.executeQuery();
                                            if (resultSet != null) {
                                                if (resultSet.next()) {
                                                    jsonObject.put("history_num", resultSet.getInt(1));
                                                    SQLManager.closeResultSet(resultSet);
                                                    SQLManager.closePreparedStatement(preparedStatement);
                                                    sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                                    preparedStatement = connection.prepareStatement(sql);
                                                    resultSet = preparedStatement.executeQuery();
                                                    if (resultSet != null) {
                                                        if (resultSet.next()) {
                                                            jsonObject.put("collection_num", resultSet.getInt(1));
                                                            SQLManager.closeResultSet(resultSet);
                                                            SQLManager.closePreparedStatement(preparedStatement);
                                                            sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\" AND uid =\"" + uid + "\"";
                                                            preparedStatement = connection.prepareStatement(sql);
                                                            resultSet = preparedStatement.executeQuery();
                                                            if (resultSet != null) {
                                                                if (resultSet.next()) {
                                                                    jsonObject.put("id", nid);
                                                                    jsonObject.put("is_collected", resultSet.getInt(1));
                                                                    jsonObject.put("manager", managerJSON);
                                                                    return ResultUtil.getSuccessJSON(jsonObject).toString();
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
                                return ResultUtil.getErrorJSON(Constant.NEW_NO_ONLINE).toString();
                            }
                            return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
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
    public String getNewContentForWeb(String nid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT n.state,n.content,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo FROM news AS n " +
                "LEFT JOIN manager_user AS m ON n.mid = m.id WHERE n.id = \"" + nid + "\"";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", nid);
                        jsonObject.put("content", resultSet.getString(2));
                        JSONObject managerJSON = new JSONObject();
                        managerJSON.put("id", resultSet.getLong(3));
                        managerJSON.put("nick_name", resultSet.getString(4));
                        managerJSON.put("sex", resultSet.getInt(5));
                        managerJSON.put("sign", resultSet.getString(6));
                        managerJSON.put("birthday", resultSet.getInt(7));
                        managerJSON.put("photo", resultSet.getString(8));
                        SQLManager.closeResultSet(resultSet);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                jsonObject.put("comment_num", resultSet.getInt(1));
                                SQLManager.closeResultSet(resultSet);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSet = preparedStatement.executeQuery();
                                if (resultSet != null) {
                                    if (resultSet.next()) {
                                        jsonObject.put("history_num", resultSet.getInt(1));
                                        SQLManager.closeResultSet(resultSet);
                                        SQLManager.closePreparedStatement(preparedStatement);
                                        sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                        preparedStatement = connection.prepareStatement(sql);
                                        resultSet = preparedStatement.executeQuery();
                                        if (resultSet != null) {
                                            if (resultSet.next()) {
                                                jsonObject.put("is_collected", -1);
                                                jsonObject.put("collection_num", resultSet.getInt(1));
                                                jsonObject.put("manager", managerJSON);
                                                return ResultUtil.getSuccessJSON(jsonObject).toString();
                                            }
                                        }
                                    }
                                }
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
    public String collectNew(String nid, String uid, String type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),state FROM news WHERE id = \"" + nid + "\"";
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
                                sql = "INSERT INTO collection ( uid , nid ) VALUES ( ? , ? )";
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1, uid);
                                preparedStatement.setString(2, nid);
                                if (preparedStatement.executeUpdate() == 1) {
                                    return ResultUtil.getSuccessJSON(new JSONObject()).toString();
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.HAS_COLLECTION).toString();
                                }
                            } else {
                                sql = "DELETE FROM collection WHERE uid = \"" + uid + "\" AND nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                if (preparedStatement.executeUpdate() == 1) {
                                    return ResultUtil.getSuccessJSON(new JSONObject()).toString();
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.NO_COLLECTION).toString();
                                }
                            }
                        }
                        return ResultUtil.getErrorJSON(Constant.NEW_NO_ONLINE).toString();
                    }
                    return ResultUtil.getErrorJSON(Constant.NEW_NO_EXIST).toString();
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
    public String getCollections(String id, String uid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSetNum = null;
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
                        sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,n.type,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo " +
                                "FROM news AS n,collection AS c,manager_user AS m " +
                                "WHERE c.uid = \"" + uid + "\" AND c.nid = n.id AND n.state = \"1\"";
                        if (sort.equals("1"))
                            sql = sql + " ORDER BY c.id DESC";
                        sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            JSONArray jsonArray = new JSONArray();
                            while (resultSet.next()) {
                                JSONObject jsonObject = new JSONObject();
                                long nid = resultSet.getLong(1);
                                jsonObject.put("id", nid);
                                jsonObject.put("title", resultSet.getString(2));
                                jsonObject.put("cover", resultSet.getString(3));
                                jsonObject.put("abstract", resultSet.getString(4));
                                jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                                jsonObject.put("type", resultSet.getInt(6));
                                JSONObject managerJSON = new JSONObject();
                                managerJSON.put("id", resultSet.getLong(7));
                                managerJSON.put("nick_name", resultSet.getString(8));
                                managerJSON.put("sex", resultSet.getInt(9));
                                managerJSON.put("sign", resultSet.getString(10));
                                managerJSON.put("birthday", resultSet.getInt(11));
                                managerJSON.put("photo", resultSet.getString(12));
                                jsonObject.put("manager", managerJSON);

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("comment_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("collection_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("history_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                jsonArray.add(jsonObject);
                            }
                            JSONObject json = new JSONObject();
                            json.put("news", jsonArray);
                            return ResultUtil.getSuccessJSON(json).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.OTHER_USER_NO_EXIST).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetNum);
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }

    @Override
    public String getHistory(String id, String uid, String page, String size, String sort) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSetNum = null;
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
                        sql = "SELECT n.id,n.title,n.cover,n.abstract,n.create_time,n.type,m.id,m.nick_name,m.sex,m.sign,m.birthday,m.photo " +
                                "FROM news AS n,history AS h,manager_user AS m " +
                                "WHERE h.uid = \"" + uid + "\" AND h.nid = n.id AND n.state = \"1\"";
                        if (sort.equals("1"))
                            sql = sql + " ORDER BY h.id DESC";
                        sql = sql + " limit " + (((Integer.valueOf(page) - 1) * Integer.valueOf(size))) + "," + size;
                        preparedStatement = connection.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            JSONArray jsonArray = new JSONArray();
                            while (resultSet.next()) {
                                JSONObject jsonObject = new JSONObject();
                                long nid = resultSet.getLong(1);
                                jsonObject.put("id", nid);
                                jsonObject.put("title", resultSet.getString(2));
                                jsonObject.put("cover", resultSet.getString(3));
                                jsonObject.put("abstract", resultSet.getString(4));
                                jsonObject.put("create_time", DateUtil.getTime(resultSet.getLong(5)));
                                jsonObject.put("type", resultSet.getInt(6));
                                JSONObject managerJSON = new JSONObject();
                                managerJSON.put("id", resultSet.getLong(7));
                                managerJSON.put("nick_name", resultSet.getString(8));
                                managerJSON.put("sex", resultSet.getInt(9));
                                managerJSON.put("sign", resultSet.getString(10));
                                managerJSON.put("birthday", resultSet.getInt(11));
                                managerJSON.put("photo", resultSet.getString(12));
                                jsonObject.put("manager", managerJSON);

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM comment WHERE nid = \"" + nid + "\" AND state = 1";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("comment_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM collection WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("collection_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                SQLManager.closeResultSet(resultSetNum);
                                SQLManager.closePreparedStatement(preparedStatement);
                                sql = "SELECT count(1) FROM history WHERE nid = \"" + nid + "\"";
                                preparedStatement = connection.prepareStatement(sql);
                                resultSetNum = preparedStatement.executeQuery();
                                if (resultSetNum != null && resultSetNum.next()) {
                                    jsonObject.put("history_num", resultSetNum.getInt(1));
                                } else {
                                    return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                                }

                                jsonArray.add(jsonObject);
                            }
                            JSONObject json = new JSONObject();
                            json.put("news", jsonArray);
                            return ResultUtil.getSuccessJSON(json).toString();
                        }
                        return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
                    }
                }
                return ResultUtil.getErrorJSON(Constant.OTHER_USER_NO_EXIST).toString();
            }
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } finally {
            SQLManager.closeResultSet(resultSetNum);
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
    }
}
