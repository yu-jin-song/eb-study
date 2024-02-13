package com.study.board.dao;

import com.study.board.controller.BoardWriteProcessCommand;
import com.study.board.util.DBUtil;
import com.study.board.vo.FileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 관련 데이터 액세스 객체입니다.
 * 파일 데이터와 관련된 데이터베이스 연산을 수행합니다.
 */
public class FileDAO {
    private final static Logger LOGGER
            = LoggerFactory.getLogger(FileDAO.class);

    /**
     * 특정 게시글에 첨부된 파일 목록을 조회합니다.
     *
     * @param boardId 파일을 조회할 게시글의 ID
     * @return 조회된 파일 목록
     */
    public List<FileVO> getFileList(long boardId) {
        List<FileVO> fileList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM tb_file WHERE board_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                FileVO file = FileVO.builder()
                        .id(rs.getLong("file_id"))
                        .boardId(rs.getLong("board_id"))
                        .originalName(rs.getString("original_name"))
                        .savedName(rs.getString("saved_name"))
                        .savedPath(rs.getString("saved_path"))
                        .ext(rs.getString("ext"))
                        .build();

                fileList.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }

        return fileList;
    }

    /**
     * 게시글에 첨부된 파일 목록을 데이터베이스에 저장합니다.
     *
     * @param fileList 저장할 파일 목록
     * @param boardId 파일이 첨부된 게시글의 ID
     */
    public void insertFileList(List<FileVO> fileList, long boardId){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO tb_file (" +
                    "board_id, original_name, saved_name, saved_path, ext, size)" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            pstmt = conn.prepareStatement(sql);

            for (FileVO file : fileList) {
                pstmt.setLong(1, boardId);
                pstmt.setString(2, file.getOriginalName());
                pstmt.setString(3, file.getSavedName());
                pstmt.setString(4, file.getSavedPath());
                pstmt.setString(5, file.getExt());
                pstmt.setLong(6, file.getSize());
                pstmt.addBatch();
                LOGGER.info(
                        "boardId = " + boardId + ", origName = " + file.getOriginalName() + ", savedName = " + file.getSavedName()
                                + ", savedPath = " + file.getSavedPath() + ", ext = " + file.getExt() + ", size = " + file.getSize()
                );
            }

            pstmt.executeBatch(); // 배치 실행
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }
    }

    /**
     * 게시글에 첨부된 파일을 데이터베이스에 저장합니다.
     *
     * @param file 저장할 파일
     * @param boardId 파일이 첨부된 게시글의 ID
     */
    public void insertFile(FileVO file, long boardId){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO tb_file (" +
                    "board_id, original_name, saved_name, saved_path, ext, size)" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, boardId);
            pstmt.setString(2, file.getOriginalName());
            pstmt.setString(3, file.getSavedName());
            pstmt.setString(4, file.getSavedPath());
            pstmt.setString(5, file.getExt());
            pstmt.setLong(6, file.getSize());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }
    }

    public void deleteFile(long id) {
        LOGGER.debug("###=========deleteFile start, fileId = " + id);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM tb_file WHERE file_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, pstmt, conn);
        }
    }
}
