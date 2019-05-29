package com.example.administrator.smarthome1.util;

import android.database.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectMysql {
    private static Connection conn;
    private static final String URL ="jdbc:mysql://10.20.4.164/mk1";
    private static final String USERNAME = "root";
    private static final String PASSWORD="mk143741";
    public static Connection getConn(){
        try{
            // 加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动程序出错");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public static void closeConn(){
        if(conn!=null){
            try{
                conn.close();
            }catch(java.sql.SQLException e){
                e.printStackTrace();
            }
        }
    }
}
