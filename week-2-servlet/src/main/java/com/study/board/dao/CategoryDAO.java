package com.study.board.dao;

import com.study.board.util.DBUtil;
import com.study.board.vo.CategoryVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 정보에 대한 데이터 액세스 객체입니다.
 * 이 클래스는 카테고리 데이터와 관련된 데이터베이스 연산을 수행합니다.
 */
public class CategoryDAO {

    /**
     * 모든 카테고리 목록을 조회합니다.
     *
     * @return 조회된 카테고리 목록
     */
    public List<CategoryVO> getCategoryList() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<CategoryVO> categoryList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM tb_category";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            CategoryVO vo = null;

            while (rs.next()) {
                vo = CategoryVO.builder()
                        .id(rs.getInt("category_id"))
                        .name(rs.getString("name"))
                        .build();

//                vo.setId(rs.getInt("category_id"));
//                vo.setName(rs.getString("name"));

                categoryList.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return categoryList;
    }

    /**
     * 주어진 ID에 해당하는 카테고리 정보를 조회합니다.
     *
     * @param id 조회할 카테고리의 ID
     * @return 조회된 카테고리 정보, 조회되지 않으면 빈 CategoryVO 객체 반환
     */
    public CategoryVO getCategoryById(long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        CategoryVO vo = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM tb_category WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                vo = CategoryVO.builder()
                        .id(rs.getInt("category_id"))
                        .name(rs.getString("name"))
                        .build();

//                vo.setId(rs.getInt("category_id"));
//                vo.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return vo;
    }
}
