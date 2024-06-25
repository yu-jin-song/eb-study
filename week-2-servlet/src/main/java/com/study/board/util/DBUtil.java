package com.study.board.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 데이터베이스 연결과 관련된 유틸리티 메소드를 제공하는 클래스
 * JDBC 드라이버 로딩 및 데이터베이스 연결 생성, 자원 해제 기능을 포함
 */
public class DBUtil {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:13306/board";
    static final String USER = "joy";
    static final String PASS = "joy1234!";

    // JDBC 드라이버 로드
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 연결을 생성하고 반환
     *
     * @return 데이터베이스와의 연결
     * @throws SQLException 데이터베이스 연결 생성 중 오류 발생 시
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * 주어진 자원들을 안전하게 종료
     * 주로 데이터베이스 연결, statement, resultset 등을 닫는 데 사용
     *
     * @param resources 닫을 자원들
     */
    public static void release(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
