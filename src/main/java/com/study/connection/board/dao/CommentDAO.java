package com.study.connection.board.dao;

import com.study.connection.board.vo.CommentVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:13306/board";
    static final String USER = "joy";
    static final String PASS = "joy1234!";

//    static {
//        Class.forName(DRIVER);
//    }
    public CommentDAO() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    // 댓글 전체 조회
    public List<CommentVO> getCommentList() {

        String sql = "SELECT * FROM tb_comment";
        List<CommentVO> commentList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CommentVO vo = new CommentVO();
                vo.setId(rs.getLong("comment_id"));
                vo.setBoardId(rs.getLong("board_id"));
                vo.setComment(rs.getString("comment"));
                vo.setWriter(rs.getString("writer"));
                vo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                commentList.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commentList;
    }

    // 댓글 생성(작성)
    public void insertComment(CommentVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO tb_comment(" +
                    "comment_id, board_id, writer, comment, created_at)" +
                    "VALUES (null, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, vo.getBoardId());
            pstmt.setString(2, vo.getWriter());
            pstmt.setString(3, vo.getComment());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 리소스 해제
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
