package com.study.board.dao;

import com.mysql.cj.util.StringUtils;
import com.study.board.util.Condition;
import com.study.board.util.DBUtil;
import com.study.board.vo.BoardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판의 데이터 액세스를 처리하는 DAO 클래스입니다.
 * 게시글 관련 데이터베이스 연산을 수행합니다.
 */
public class BoardDAO {

    Logger LOGGER = LoggerFactory.getLogger(BoardDAO.class);

    /**
     * 주어진 조건에 맞는 게시글의 개수를 구합니다.
     *
     * @param condition 검색 조건을 포함하는 Condition 객체
     * @return 조건에 맞는 게시글의 총 개수
     */
    public int getBoardCount(Condition condition) {
        int cnt = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM tb_board";

            int categoryId = condition.getCategoryId();
            // 특정 카테고리를 선택한 경우
            if (categoryId != -1) {
//                sql += " WHERE category_id = " + categoryId;
                sql += " WHERE category_id = ?";
            }

            String keyword = condition.getKeyword();
            // 검색 키워드가 존재하는 경우
            if (!StringUtils.isNullOrEmpty(keyword)) {
                sql += (categoryId != -1) ? " AND" : " WHERE";
//                sql += " title LIKE '%" + keyword + "' "
//                        + "OR writer LIKE '%" + keyword + "' "
//                        + "OR content LIKE '%" + keyword + "' ";
                sql += " title LIKE '%?' "
                        + "OR writer LIKE '%?' "
                        + "OR content LIKE '%?' ";
            }

            pstmt = conn.prepareStatement(sql);
            if (categoryId != -1) {
                pstmt.setInt(1, categoryId);
            }
            if (!StringUtils.isNullOrEmpty(keyword)) {
                pstmt.setString(2, keyword);
            }
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cnt = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return cnt;
    }

    /**
     * 조건에 맞는 게시글 목록을 조회합니다.
     *
     * @param condition 검색 조건을 포함하는 Condition 객체
     * @return 조건에 맞는 게시글 목록
     */
    public List<BoardVO> getBoardList(Condition condition) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<BoardVO> boardList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();

            // 페이징 처리
            String sql = "SELECT * FROM "
                    + "(SELECT board_id, category_id, title, "
                    +  "writer, views, created_at, modified_at FROM "
                    + "(SELECT * FROM tb_board ";

            // 특정 카테고리를 선택한 경우
            int categoryId = condition.getCategoryId();
            if (categoryId != -1) {
                sql += "WHERE category_id = ? ";
            }

            // 검색 키워드가 존재하는 경우
            String keyword = condition.getKeyword();
            if (!StringUtils.isNullOrEmpty(keyword)
                    && !StringUtils.isEmptyOrWhitespaceOnly(keyword)) {

                sql += (categoryId != -1) ? "AND" : "WHERE";
                sql += " title LIKE '%?' "
                        + "OR writer LIKE '%?' "
                        + "OR content LIKE '%?' ";
            }

            sql += "ORDER BY board_id DESC) AS sq) AS sub_table WHERE board_id BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);

            // 검색 조건 여부에 따른 prestatement 객체 값 설정
            int lastParameterIndex = 0;
            if (categoryId != -1) {
                LOGGER.debug("검색 조건에 카테고리가 포함되어 있습니다.");
                pstmt.setInt(1, categoryId);
                lastParameterIndex = 1;

                if (!StringUtils.isNullOrEmpty(keyword)
                        && !StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
                    LOGGER.debug("검색 조건에 키워드가 포함되어 있습니다. keyword = " + keyword);
                    pstmt.setString(2, keyword);
                    lastParameterIndex = 2;
                }
            }
            else if (!StringUtils.isNullOrEmpty(keyword)
                    && !StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
                LOGGER.debug("검색 조건에 카테고리가 포함되어 있지 않습니다.");
                LOGGER.debug("keyword = " + keyword);
                pstmt.setString(1, keyword);
                lastParameterIndex = 1;
            }

            int startRow = condition.getStartRow();
            int endRow = condition.getEndRow();


            if (lastParameterIndex == 2) {
                pstmt.setInt(3, startRow);
                pstmt.setInt(4, endRow);
            }
            else if (lastParameterIndex == 1) {
                pstmt.setInt(2, startRow);
                pstmt.setInt(3, endRow);
            } else {
                pstmt.setInt(1, startRow);
                pstmt.setInt(2, endRow);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 반복할 때마다 BoardVO 객체를 생성 및 데이터 저장
                BoardVO vo = BoardVO.builder()
                        .id(rs.getLong("board_id"))
                        .categoryId(rs.getInt("category_id"))
                        .title(rs.getString("title"))
                        .writer(rs.getString("writer"))
                        .views(rs.getLong("views"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .modifedAt(rs.getTimestamp("modified_at").toLocalDateTime())
                        .build();

                boardList.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return boardList;
    }

//public List<BoardVO> getBoardList(int startRow, int endRow) {
//    public List<BoardVO> getBoardList(
//            int startRow, int endRow, Timestamp startDate,
//            Timestamp endDate, int categoryId, String keyword
//    ) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        // 페이징 처리
//        String sql = "SELECT * FROM "
//                + "(SELECT board_id, category_id, title, writer, views, created_at, modified_at FROM "
//                + "(SELECT * FROM tb_board WHERE ";
//
//        // 특정 카테고리를 선택한 경우
//        if (categoryId != -1) {
//            sql += "category_id = " + categoryId;
//        }
//
//        // 검색 키워드가 존재하는 경우
//        if (!StringUtils.isNullOrEmpty(keyword)
//                && !StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
//            sql += "AND title LIKE '%" + keyword + "' "
//                    + "OR writer LIKE '%" + keyword + "' "
//                    + "OR content LIKE '%" + keyword + "' ";
//        }
//
//        sql += "created_at BETWEEN ? AND ? ORDER BY board_id DESC) AS sq"
//                + ") AS sub_table WHERE board_id BETWEEN ? AND ?";
//
//
//        List<BoardVO> boardList = new ArrayList<>();
//
//        try {
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setTimestamp(1, startDate);
//            pstmt.setTimestamp(2, endDate);
//            pstmt.setInt(3, startRow);
//            pstmt.setInt(4, endRow);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                // 반복할 때마다 BoardVO 객체를 생성 및 데이터 저장
//                BoardVO vo = new BoardVO();
//
//                vo.setId(rs.getLong("board_id"));
//                vo.setCategoryId(rs.getInt("category_id"));
//                vo.setTitle(rs.getString("title"));
//                vo.setWriter(rs.getString("writer"));
//                vo.setViews(rs.getLong("views"));
//                vo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
//                vo.setModifedAt(rs.getTimestamp("modified_at").toLocalDateTime());
//
//                boardList.add(vo);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 리소스 해제
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return boardList;
//    }

//    public List<BoardVO> getBoardList(int startRow, int endRow) {
//    public List<BoardVO> getBoardList(
//            int startRow, int endRow, Timestamp startDate,
//            Timestamp endDate
//    ) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        // 페이징 처리
//        String sql = "SELECT * FROM "
//                + "(SELECT board_id, category_id, title, writer, views, created_at, modified_at FROM "
//                + "(SELECT * FROM tb_board WHERE ";
//
//        // 특정 카테고리를 선택한 경우
////        if (categoryId != -1) {
////            sql += "category_id = " + categoryId;
////        }
//
////        // 검색 키워드가 존재하는 경우
////        if (!StringUtils.isNullOrEmpty(keyword)
////                && !StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
////            sql += "AND title LIKE '%" + keyword + "' "
////                    + "OR writer LIKE '%" + keyword + "' "
////                    + "OR content LIKE '%" + keyword + "' ";
////        }
//
//        sql += "created_at BETWEEN ? AND ? ORDER BY board_id DESC) AS sq"
//                + ") AS sub_table WHERE board_id BETWEEN ? AND ?";
//
//
//        List<BoardVO> boardList = new ArrayList<>();
//
//        try {
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setTimestamp(1, startDate);
//            pstmt.setTimestamp(2, endDate);
//            pstmt.setInt(3, startRow);
//            pstmt.setInt(4, endRow);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                // 반복할 때마다 BoardVO 객체를 생성 및 데이터 저장
//                BoardVO vo = new BoardVO();
//
//                vo.setId(rs.getLong("board_id"));
//                vo.setCategoryId(rs.getInt("category_id"));
//                vo.setTitle(rs.getString("title"));
//                vo.setWriter(rs.getString("writer"));
//                vo.setViews(rs.getLong("views"));
//                vo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
//                vo.setModifedAt(rs.getTimestamp("modified_at").toLocalDateTime());
//
//                boardList.add(vo);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 리소스 해제
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return boardList;
//    }


//    public List<BoardVO> getBoardList(int startRow, int endRow) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        // 페이징 처리
//        String sql = "SELECT * FROM "
//                + "(SELECT board_id, category_id, title, writer, views, created_at, modified_at FROM "
//                + "(SELECT * FROM tb_board ORDER BY board_id DESC) AS sq"
//                + ") AS sub_table WHERE board_id BETWEEN ? AND ?";
//
//
//        List<BoardVO> boardList = new ArrayList<>();
//
//        try {
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, startRow);
//            pstmt.setInt(2, endRow);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                // 반복할 때마다 BoardVO 객체를 생성 및 데이터 저장
//                BoardVO vo = new BoardVO();
//
//                vo.setId(rs.getLong("board_id"));
//                vo.setCategoryId(rs.getInt("category_id"));
//                vo.setTitle(rs.getString("title"));
//                vo.setWriter(rs.getString("writer"));
//                vo.setViews(rs.getLong("views"));
//                vo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
//                vo.setModifedAt(rs.getTimestamp("modified_at").toLocalDateTime());
//
//                boardList.add(vo);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 리소스 해제
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return boardList;
//    }

    /**
     * 지정된 게시글의 상세 정보를 조회합니다.
     *
     * @param id 조회할 게시글의 고유 ID
     * @return 조회된 게시글의 상세 정보
     */
    public BoardVO getBoard(long id) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        BoardVO board = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM tb_board WHERE board_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                board = BoardVO.builder()
                        .id(rs.getLong("board_id"))
                        .categoryId(rs.getInt("category_id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .writer(rs.getString("writer"))
                        .views(rs.getLong("views"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .modifedAt(rs.getTimestamp("modified_at").toLocalDateTime())
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return board;
    }

    /**
     * 게시글의 조회수를 갱신합니다.
     *
     * @param vo 조회수를 갱신할 게시글 정보를 담은 BoardVO 객체
     * @return 갱신된 조회수
     */
    public long updateViewsCnt(BoardVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        long views = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE tb_board SET views = ? WHERE board_id = ?";
            pstmt = conn.prepareStatement(sql);

            views = vo.getViews() + 1;
            pstmt.setLong(1, views);
            pstmt.setLong(2, vo.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(pstmt, conn);
        }

        return views;
    }

    /**
     * 새로운 게시글을 생성합니다.
     *
     * @param vo 생성할 게시글 정보를 담은 BoardVO 객체
     * @return 생성된 게시글의 고유 ID
     */
    public long insertBoard(BoardVO vo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        long generatedKey = -1L;    // 게시글 생성 후 pk값 반환하기 위함

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO tb_board(" +
                    "category_id, title, content, writer, password)" +
                    "VALUES (?, ?, ?, ?, ?)";
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
                }
            } else {
                throw new Exception("Insert Board date failed..");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(generatedKeys, pstmt, conn);
        }

        return generatedKey;
    }

    /**
     * 기존 게시글을 수정합니다.
     *
     * @param vo 수정할 게시글 정보를 담은 BoardVO 객체
     */
    public void updateBoard(BoardVO vo){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DBUtil.getConnection();
            String sql = "UPDATE tb_board SET title=?, content=?, writer=? WHERE board_id=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, vo.getTitle());
            pstmt.setString(2, vo.getContent());
            pstmt.setString(3, vo.getWriter());
            pstmt.setLong(4, vo.getId());

            pstmt.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            DBUtil.release(pstmt, conn);
        }
    }
}

