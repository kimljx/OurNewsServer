package com.ournews.dao.impl;

import com.ournews.dao.VersionDao;
import com.ournews.utils.Constant;
import com.ournews.utils.DateUtil;
import com.ournews.utils.ResultUtil;
import com.ournews.utils.SQLManager;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/5/3.
 */
public class VersionDaoImpl implements VersionDao {
    @Override
    public String getVersionInfo() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT count(1),now_version,min_version,update_time,file_name,file_size,description FROM version WHERE id = 101";
        try {
            connection = SQLManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("now_version", resultSet.getInt(2));
                        jsonObject.put("min_version", resultSet.getInt(3));
                        jsonObject.put("update_time", DateUtil.getTime(resultSet.getLong(4)));
                        jsonObject.put("file_name", resultSet.getString(5));
                        jsonObject.put("file_size", resultSet.getLong(6));
                        jsonObject.put("description", resultSet.getString(7));
                        return ResultUtil.getSuccessJSON(jsonObject).toString();
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
}
