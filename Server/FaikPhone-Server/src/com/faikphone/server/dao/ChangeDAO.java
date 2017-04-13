package com.faikphone.server.dao;

import com.faikphone.server.model.Conn;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.Random;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class ChangeDAO {
    private static ChangeDAO instance = new ChangeDAO();
    private Statement st = null;
    private Connection connection = null;

    public static ChangeDAO getInstance() {
        return instance;
    }

    private ChangeDAO() {
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

    /**
     * RealPhone 에서 토큰을 등록할 때
     *
     * @param token
     * @return
     */
    public boolean insertRealPhoneToken(String token) {
        String sql = "insert into conn(realtoken, code) values (?, ?) ";

        String code = createRandomCode();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, code);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Faki Phone 에서 인증 성공 후 토큰을 등록할 때
     *
     * @param token
     * @param code
     * @return
     */
    public boolean insertFaikPhoneToken(String token, String code) {
        if (isTokenRegister(token) == false) {
            return false;
        }

        String sql = "update conn set faketoken = " + token + " where code = " + code;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * FaikPhone과의 Connetion을 해제 하는 메소드
     * @param token
     * @param state : true = realPhone, false = fakePhone
     * @return
     */
    public boolean resetConnection(String token, boolean state) {
        if (isTokenRegister(token)) {
            String sql = "update conn set faketoken = '' where ";
            sql += state ? "realtoken = '" + token + "'" : "faketoken = '" + token + "'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
            } catch (SQLException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 모든 정보를 reset하는 메소드
     * @param token
     * @param state : true = realPhone, false = fakePhone
     * @return
     */
    public boolean resetAll(String token, boolean state) {
        if(isTokenRegister(token)){
            String sql = state == true ? "delete from conn where realtoken = " + token : "delete from conn where faketoken = " + token ;
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
            } catch (SQLException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 인증 코드를 reset하는 메소드
     *
     * @param token
     * @return
     */
    public boolean resetCode(String token) {
        if (isTokenRegister(token) == false) {
            return false;
        }
        String code = createRandomCode();
        String sql = "update conn set code = '" + code + "' where realtoken = '" + token + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * fakeToken에 맞는 Conn 객체를 반환하는 메소드
     * @param fakeToken
     * @return
     */
    public Conn getConnFromFakeToken(String fakeToken) {
        try {
            ResultSet rs = selectResultSetFromFakeToken(fakeToken);
            return new Conn(rs.getString("realtoken"), rs.getString("code"), rs.getString("faketoken"));
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * realToken에 맞는 Conn 객체를 반환하는 메소드
     * @param realToken
     * @return
     */
    public Conn getConnFromRealToken(String realToken) {
        try {
            ResultSet rs = selectResultSetFromRealToken(realToken);
            return new Conn(rs.getString("realtoken"), rs.getString("code"), rs.getString("faketoken"));
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * realPhone의 토큰의 ResultSet을 반환하는 메소드
     * @param realToken
     * @return
     * @throws SQLException
     */
    public ResultSet selectResultSetFromRealToken(String realToken) throws SQLException {
        String sql = "select * from conn where realtoken = " + realToken;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs;
    }

    /**
     * fakePhone의 토큰의 ResultSet을 반환하는 메소드
     * @param fakeToken
     * @return
     * @throws SQLException
     */
    public ResultSet selectResultSetFromFakeToken(String fakeToken) throws SQLException {
        String sql = "select * from conn where faketoken = " + fakeToken;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs;
    }

    /**
     * code에 해당하는 ResultSet을 반환하는 메소드
     * @param code
     * @return
     * @throws SQLException
     */
    public ResultSet selectResultSetFromCode(String code) throws SQLException {
        String sql = "select * from conn where code = " + code;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs;
    }

    /**
     * 인증 코드가 올바른지 체크하는 메소드
     * @param code
     * @return
     */
    private boolean isCodeValid(String code){
        try{
            ResultSet rs = selectResultSetFromCode(code);
            if(rs.getString("code").equals(code)){
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     * RealPhone 토큰이 등록 되어 있는지를 체크하는 메소드
     *
     * @param realToken
     * @return
     */
    private boolean isTokenRegister(String realToken) {
        return getConnFromRealToken(realToken) == null;
    }

    /**
     * 인증 코드를 create 하는 메소ㅔ드
     *
     * @return code
     */
    private String createRandomCode() {
        String code = "";
        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            char engCode = (char) ((int) (Math.random() * 25) + 65);
            int numCode = (int) (Math.random() * 10);
            code = code + ((random.nextInt(2) == 1) ? engCode : numCode + "");
        }
        return code;
    }

}
