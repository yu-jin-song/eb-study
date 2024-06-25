package com.study.connection.board.dao;

import com.study.connection.board.vo.BoardVO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:13306/board";
    static final String USER = "joy";
    static final String PASS = "joy1234!";

    public BoardDAO() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    // 게시글 전체 개수 구하기
    public int getTotalBoardCount() {
        int cnt = 0;
        String sql = "SELECT COUNT(*) FROM tb_board";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                cnt = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cnt;
    }

    // 게시글 전체 목록 조회

    /**
     *
     * @param startRow
     * @param endRow
     * @return
     */
    public List<BoardVO> getBoardList(int startRow, int endRow) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 페이징 처리를 위한 sql
        String sql = "SELECT * FROM "
                + "(SELECT board_id, category_id, title, writer, views, created_at, modified_at FROM "
                + "(SELECT * FROM tb_board ORDER BY board_id DESC) AS sq) AS sub_table WHERE board_id BETWEEN ? AND ?";
        List<BoardVO> boardList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, startRow);
            pstmt.setInt(2, endRow);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 반복할 때마다 BoardVO 객체를 생성 및 데이터 저장
                long id = rs.getLong("board_id");
                int categoryId = rs.getInt("category_id");
                String title = rs.getString("title");
                String writer = rs.getString("writer");
                long views = rs.getLong("views");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime modifiedAt = rs.getTimestamp("modified_at").toLocalDateTime();

                BoardVO vo = new BoardVO(id, categoryId, title, writer, "", views, createdAt, modifiedAt);
                boardList.add(vo);
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

        return boardList;
    }

    // 게시글 상세 조회(1개)
    public BoardVO getBoard(BoardVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        BoardVO board = new BoardVO();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM tb_board WHERE board_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, vo.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                board.setId(rs.getLong("board_id"));
                board.setCategoryId(rs.getInt("category_id"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setViews(rs.getLong("views"));
                board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                board.setModifedAt(rs.getTimestamp("modified_at").toLocalDateTime());
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

        return board;
    }

    // 조회수 갱신
    public long updateViewsCnt(BoardVO vo) {
        String sql = "UPDATE tb_board SET views = ? WHERE board_id = ?";
        long views = 0;

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            views = vo.getViews() + 1;
            pstmt.setLong(1, views);
            pstmt.setLong(2, vo.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return views;
    }

    // 게시글 생성
    public long insertBoard(BoardVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        long generatedKey = -1L;    // 게시글 생성 후 pk값 반환하기 위함

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO tb_board(" +
                    "category_id, title, content, " +
                    "writer, password, created_at, modified_at) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, vo.getCategoryId());
            pstmt.setString(2, vo.getTitle());
            pstmt.setString(3, vo.getContent());
            pstmt.setString(4, vo.getWriter());
            pstmt.setString(5,vo.getPassword());

            int rowCnt = pstmt.executeUpdate();

            if(rowCnt > 0) {
                generatedKeys = pstmt.getGeneratedKeys();

                if(generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                    System.out.println("자동 생성된 키 값 : " + generatedKey);
                } else {
                    System.out.println("자동 생성된 키 값이 없습니다.");
                }
            } else {
                System.out.println("데이터 삽입에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 리소스 해제
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return generatedKey;
    }

    // 게시글 수정
    public void updateBoard(BoardVO vo){
        Connection conn = null;
        PreparedStatement pstmt=null;

        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE tb_board SET title=?, content=?, writer=?, modified_at=NOW() WHERE board_id=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, vo.getTitle());
            pstmt.setString(2, vo.getContent());
            pstmt.setString(3, vo.getWriter());
            pstmt.setLong(4, vo.getId());

            pstmt.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            try{
                if(pstmt!=null)
                    pstmt.close();
                if(conn!=null)
                    conn.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
    }
}
