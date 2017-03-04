package com.ournews.dao.impl;

import com.ournews.bean.Comment;
import com.ournews.bean.New;
import com.ournews.bean.User;
import com.ournews.dao.NewDao;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import com.ournews.utils.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class NewDaoImpl implements NewDao {

    @Override
    public boolean addNews(String title, String cover, String abstractContent, String content, String createTime, String type) {
        boolean result = false;
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
            preparedStatement.setString(5, createTime);
            preparedStatement.setString(6, type);
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
    public Map<Integer, List<New>> getHomeNews(String selectType) {
        Map<Integer, List<New>> newTitles = new HashMap<>();
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
                List<New> news = new ArrayList<>();
                if (resultSet != null) {
                    resultSet.last();
                    rows = resultSet.getRow();
                    if (rows != 0) {
                        List<Integer> randoms = MyUtils.getNumberList(rows);
                        for (Integer i : randoms) {
                            resultSet.first();
                            for (int n = 0; n < i - 1; n++) {
                                resultSet.next();
                            }
                            New new1 = new New();
                            new1.setId(resultSet.getLong(1));
                            new1.setTitle(resultSet.getString(2));
                            new1.setCover(resultSet.getString(3));
                            new1.setAbstractContent(resultSet.getString(4));
                            new1.setCreateTime(resultSet.getString(5));
                            new1.setType(Integer.valueOf(selectType));
                            news.add(new1);
                        }
                    }
                }
                newTitles.put(Integer.valueOf(selectType), news);
            } else {
                for (int type = 1; type < 6; type++) {
                    preparedStatement = connection.prepareStatement(
                            sql + "type = \"" + type + "\" ORDER BY id DESC limit 0," + Constant.RANDOM_NUM);
                    resultSet = preparedStatement.executeQuery();
                    List<New> news = new ArrayList<>();
                    if (resultSet != null) {
                        resultSet.last();
                        rows = resultSet.getRow();
                        if (rows != 0) {
                            List<Integer> randoms = MyUtils.getNumberList(rows);
                            for (Integer i : randoms) {
                                resultSet.first();
                                for (int n = 0; n < i - 1; n++) {
                                    resultSet.next();
                                }
                                New new1 = new New();
                                new1.setId(resultSet.getLong(1));
                                new1.setTitle(resultSet.getString(2));
                                new1.setCover(resultSet.getString(3));
                                new1.setAbstractContent(resultSet.getString(4));
                                new1.setCreateTime(resultSet.getString(5));
                                new1.setType(type);
                                news.add(new1);
                            }
                        }
                        SQLManager.closeResultSet(resultSet);
                    }
                    newTitles.put(type, news);
                    SQLManager.closePreparedStatement(preparedStatement);
                }
                preparedStatement = connection.prepareStatement(sql + "type = \" 6 \" ORDER BY id DESC limit 0,4");
                resultSet = preparedStatement.executeQuery();
                List<New> news = new ArrayList<>();
                if (resultSet != null) {
                    int i = 0;
                    while (resultSet.next() && i < 4) {
                        New new1 = new New();
                        new1.setId(resultSet.getLong(1));
                        new1.setTitle(resultSet.getString(2));
                        new1.setCover(resultSet.getString(3));
                        new1.setAbstractContent(resultSet.getString(4));
                        new1.setCreateTime(resultSet.getString(5));
                        new1.setType(6);
                        news.add(new1);
                        i++;
                    }
                }
                newTitles.put(6, news);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
            SQLManager.closeResultSet(resultSet);
        }
        return newTitles;
    }

    @Override
    public List<New> getNewList(int type, int page, int size, int sort) {
        if (page < 1)
            page = 1;
        List<New> news = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id,title,cover,abstract,createtime FROM news WHERE state = \"1\" AND type = \"" + type + "\"";
        if (sort == 1)
            sql = sql + " ORDER BY id DESC";
        sql = sql + " limit " + (((page - 1) * size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                news = new ArrayList<>();
                while (resultSet.next()) {
                    New n = new New();
                    n.setId(resultSet.getLong(1));
                    n.setTitle(resultSet.getString(2));
                    n.setCover(resultSet.getString(3));
                    n.setAbstractContent(resultSet.getString(4));
                    n.setCreateTime(resultSet.getString(5));
                    n.setType(type);
                    news.add(n);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return news;
    }

    @Override
    public List<New> searchNew(String name, int page, int size, int sort) {
        if (page < 1)
            page = 1;
        List<New> news = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id,title,cover,abstract,createtime,type FROM news WHERE state = \"1\" AND type != \"6\"";
        char[] cs = name.toCharArray();
        for (char c : cs) {
            sql = sql + " AND title LIKE \"%" + c + "%\"";
        }
        if (sort == 1)
            sql = sql + " ORDER BY id DESC";
        sql = sql + " limit " + (((page - 1) * size)) + "," + size;
        System.out.println(sql);
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                news = new ArrayList<>();
                while (resultSet.next()) {
                    New n = new New();
                    n.setId(resultSet.getLong(1));
                    n.setTitle(resultSet.getString(2));
                    n.setCover(resultSet.getString(3));
                    n.setAbstractContent(resultSet.getString(4));
                    n.setCreateTime(resultSet.getString(5));
                    n.setType(resultSet.getInt(6));
                    news.add(n);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return news;
    }

    @Override
    public int userBrowseNew(long nid, long uid, String time) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(*) FROM user WHERE id = \"" + uid + "\"";
        int result = -2;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        result = -1;
                        sql = "REPLACE INTO history ( uid , nid , time ) VALUES ( ? ,? ,? ) ";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setLong(1, uid);
                        preparedStatement.setLong(2, nid);
                        preparedStatement.setString(3, time);
                        result = preparedStatement.executeUpdate();
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
        System.out.println(result);
        return result;
    }

    @Override
    public New getNewContent(long id) {
        New n = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT content FROM news WHERE id = \"" + id + "\" AND state =\"1\"";
        int rows = 0;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                resultSet.last();
                rows = resultSet.getRow();
                if (rows != 0) {
                    String content = null;
                    do {
                        content = resultSet.getString(1);
                    } while (resultSet.next());
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT count(*) FROM comment WHERE nid = \"" + id + "\" AND state = 1";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            n = new New();
                            n.setId(id);
                            n.setContent(content);
                            n.setCommentNum(resultSet.getInt(1));
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
        return n;
    }

    @Override
    public int collectNew(long uid, long nid, int type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql;
        if (type == 1) {
            sql = "INSERT INTO collection ( uid , nid ) VALUES ( ? , ? )";
        } else {
            sql = "INSERT INTO collection ( uid , nid ) VALUES ( ? , ? )";
        }
        int result = 0;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, uid);
            preparedStatement.setLong(2, nid);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return result;
    }

    @Override
    public int writeComment(long uId, long nId, String content, String createTime) {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM news WHERE state = 1 AND id = \"" + nId + "\"";
        int rows = 0;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                resultSet.last();
                rows = resultSet.getRow();
                resultSet.first();
                if (rows != 0) {
                    SQLManager.closeResultSet(resultSet);
                    SQLManager.closePreparedStatement(preparedStatement);
                    sql = "SELECT * FROM user WHERE id = \"" + uId + "\"";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.last();
                    rows = resultSet.getRow();
                    resultSet.first();
                    if (rows != 0) {
                        SQLManager.closeResultSet(resultSet);
                        SQLManager.closePreparedStatement(preparedStatement);
                        sql = "INSERT INTO comment ( nid , uid , content, createtime ) VALUES ( ? , ? , ? ,?)";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setLong(1, nId);
                        preparedStatement.setLong(2, uId);
                        preparedStatement.setString(3, content);
                        preparedStatement.setString(4, createTime);
                        result = preparedStatement.executeUpdate();
                    }
                } else {
                    result = 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return result;
    }

    @Override
    public List<Comment> getComment(long nid, int page, int size, int sort) {
        if (page < 1)
            page = 1;
        List<Comment> comments = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT comment.id,comment.content,comment.createtime,user.id,user.nickname,user.sex,user.photo FROM comment,user WHERE comment.nid = \""
                + nid + "\" AND comment.uid = user.id AND state = 1";
        if (sort == 1)
            sql = sql + " ORDER BY comment.id DESC";
        sql = sql + " limit " + (((page - 1) * size)) + "," + size;
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                comments = new ArrayList<>();
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setId(resultSet.getLong(1));
                    comment.setContent(resultSet.getString(2));
                    comment.setCreateTime(resultSet.getString(3));
                    User user = new User();
                    user.setId(resultSet.getLong(4));
                    user.setNickName(resultSet.getString(5));
                    user.setSex(resultSet.getInt(6));
                    user.setPhoto(resultSet.getString(7));
                    comment.setUser(user);
                    comments.add(comment);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            SQLManager.closeResultSet(resultSet);
            SQLManager.closePreparedStatement(preparedStatement);
            SQLManager.closeConnection(connection);
        }
        return comments;
    }
}
