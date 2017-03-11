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
        String sql = "SELECT count(1),id,title,cover,abstract,createtime,type,state FROM news WHERE id = \"" + nid + "\"";
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
                            return PushUtil.pushToAndroid("OurNews", newJSON.getString("title"), jsonObject.toString());
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
}
