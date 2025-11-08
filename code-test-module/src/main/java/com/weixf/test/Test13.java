package com.weixf.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 *
 * @since 2022-07-13
 */
public class Test13 {

    public static void main(String[] args) throws Exception {


        CustomCallable cRunnacle = new CustomCallable();
        FutureTask<String> futureTask = new FutureTask<String>(cRunnacle);
        Thread thread = new Thread(futureTask, "子线程");
        thread.start(); // 子线程执行

        System.out.println("主线程做自己的事情--start");
        System.out.println("获取子线程返回结果：" + futureTask.get());// 获取返回结果是会阻塞
        System.out.println("主线程做自己的事情--end");
    }

    public static void ExecuteSql() {
        String username = "root";
        String password = "root";
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/test?useServerPrepStmts=true&writeBatchedStatements=true";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            String sql = "insert into test(name,sex,remark) values (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < 10000000; i++) {
                preparedStatement.setString(1, "name");
                preparedStatement.setInt(2, 1);
                preparedStatement.setString(3, "remark");
                preparedStatement.addBatch();
                if (i % 100 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static final class CustomCallable implements Callable<String> {
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() + ":执行 start");
            Thread.sleep(10000); // 子线程停留2秒
            System.out.println(Thread.currentThread().getName() + ":执行 end");
            return "Hello world";
        }
    }
}
