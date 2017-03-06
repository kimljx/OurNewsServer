package com.ournews.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager {

    private static final String USER = "root";
    private static final String PASSWORD = "8189252.";
    private static final String URL = "jdbc:mariadb://localhost:3306/OurNews";
    private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";

//    private static final String USER = "root";
//    private static final String PASSWORD = "qq8189252.";
//    private static final String URL = "jdbc:mysql://localhost:3306/ournews?useUnicode=true&characterEncoding=utf-8&useSSL=false";
//    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER_CLASS_NAME);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
