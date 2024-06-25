package com.study.board.dao;

import com.study.board.util.DBUtil;
import com.study.board.vo.CommentVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글과 관련된 데이터베이스 작업을 처리하는 DAO 클래스입니다.
 * 댓글 조회, 생성 등의 데이터베이스 연산을 수행합니다.
 */
public class CommentDAO {

    /**
     * 특정 게시글에 대한 모든 댓글을 조회합니다.
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 댓글의 목록
     */
    public List<CommentVO> getCommentList(long boardId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<CommentVO> commentList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM tb_comment WHERE board_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CommentVO vo = CommentVO.builder()
                        .id(rs.getLong("comment_id"))
                        .boardId(rs.getLong("board_id"))
                        .comment(rs.getString("comment"))
                        .writer(rs.getString("writer"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
//                vo.setId(rs.getLong("comment_id"));
//                vo.setBoardId(rs.getLong("board_id"));
//                vo.setComment(rs.getString("comment"));
//                vo.setWriter(rs.getString("writer"));
//                vo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                commentList.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return commentList;
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param vo 생성할 댓글 정보를 담은 CommentVO 객체
     */
    public void insertComment(CommentVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO tb_comment(" +
                    "board_id, writer, comment) " +
                    "VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, vo.getBoardId());
            pstmt.setString(2, vo.getWriter());
            pstmt.setString(3, vo.getComment());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(pstmt, conn);
        }
    }
}
