package com.faikphone.server.dao;

import java.sql.*;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class ChangeDAO {
    private static ChangeDAO instance = new ChangeDAO();
    private Statement st = null;
    private Connection connection = null;

    public static ChangeDAO getInstance(){
        return instance;
    }

    private ChangeDAO(){
        System.out.println("DB 연결 성공");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/auth", "root", "4112665aA");
            st = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String insertRealPhoneToken(String token){
        String sql = "insert into conn(realtoken, code) values (?, ?) ";
        String code = null;
        for(int i = 0 ; i < 12; i++){
            code.
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, token);
            preparedStatement.setString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
