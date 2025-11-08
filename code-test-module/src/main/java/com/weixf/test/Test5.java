package com.weixf.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Test5 {
    public static void main(String[] args) {
        ExecuteSql();
    }

    public static void ExecuteSql() {
        // 1.准备数据库连接的基本数据
        /*用户名*/
        String username = "sa";
        /*密码*/
        String password = "sa";
        /*驱动包名*/
        String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        /*连接地址*/
        String jdbcUrl = "jdbc:sqlserver://10.70.0.102:1433;databaseName=";

        try {
            // 2.加载驱动
            Class.forName(driverClass);
            // 3.获取数据库连接，填入连接地址、用户名、密码
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            // 4.准备SQL语句，并且使用预编译SQL
            String sql = "select * from AirReport";
            PreparedStatement ps = connection.prepareStatement(sql);
            // 5.执行SQL语句，并获取结果集
            ResultSet resultSet = ps.executeQuery();
            // 6.获取列数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int size = metaData.getColumnCount();
            // 7.将结果集读取出来
            // 判断结果集是否为空
            while (resultSet.next()) {
                // 8.按列索引获取数据
                List<Object> objectList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Object object = resultSet.getObject(i + 1);
                    objectList.add(object);
                }
                System.out.println(objectList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
