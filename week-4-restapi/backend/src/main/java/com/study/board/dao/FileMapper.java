package com.study.board.dao;

import com.study.board.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 게시글에 첨부된 파일들을 관리하기 위한 MyBatis 매퍼 인터페이스
 * - 파일 관련 데이터베이스 연산 정의
 */
@Mapper
public interface FileMapper {

    /**
     * 여러 파일 정보를 데이터베이스에 일괄적으로 삽입
     *
     * @param files 삽입할 파일 정보의 리스트
     * @param boardId 파일이 첨부된 게시글의 ID
     */
    void insertFiles(@Param("files") List<FileDTO> files, @Param("boardId") long boardId);

    /**
     * 특정 파일 ID를 가진 파일 정보를 데이터베이스에서 삭제
     *
     * @param fileId 삭제할 파일의 ID
     */
    void delete(long fileId);

    /**
     * 특정 게시글에 첨부된 모든 파일의 리스트를 조회
     *
     * @param boardId 파일을 조회할 게시글의 ID
     * @return 조회된 파일들의 리스트
     */
    List<FileDTO> getAllFilesByBoardId(long boardId);


    /**
     * 특정 파일 ID를 가진 파일 정보를 조회
     *
     * @param fileId 파일 ID
     * @return 파일 정보
     */
    FileDTO getFileById(long fileId);


    /**
     * 특정 게시글에 첨부된 파일 수를 조회
     *
     * @param boardId 특정 게시글 ID
     * @return 파일 수
     */
    byte getFileCount(long boardId);
}
