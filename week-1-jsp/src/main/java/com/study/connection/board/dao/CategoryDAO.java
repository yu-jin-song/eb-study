package com.study.connection.board.dao;

import com.study.connection.board.vo.CategoryVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:13306/board";
    static final String USER = "joy";
    static final String PASS = "joy1234!";

    public CategoryDAO() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    // 카테고리 목록 조회(전체)
    public List<CategoryVO> getCategoryList() {
        String sql = "SELECT * FROM tb_category";
        List<CategoryVO> categoryList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CategoryVO vo = new CategoryVO();
                vo.setId(rs.getInt("category_id"));
                vo.setName(rs.getString("name"));
                categoryList.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    // 카테고리 조회(1개)
    public CategoryVO getCategoryById(long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String name = "";
        CategoryVO vo = new CategoryVO();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM tb_category WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                vo.setId(rs.getInt("category_id"));
                vo.setName(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 리소스 해제
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return vo;
    }
}
