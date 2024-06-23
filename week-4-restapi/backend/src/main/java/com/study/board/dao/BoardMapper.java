package com.study.board.dao;

import com.study.board.dto.BoardDTO;
import com.study.board.util.SearchCriteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 게시판 정보에 접근하기 위한 MyBatis 매퍼 인터페이스
 * - 게시글 관련 데이터베이스 연산 정의
 */
@Mapper
public interface BoardMapper {

    /**
     * 주어진 조건에 맞는 게시글의 리스트를 조회
     *
     * @param condition 게시글 조회 조건
     * @return 조건에 맞는 게시글의 리스트
     */
    List<BoardDTO> getAllBoards(SearchCriteria condition);

    /**
     * 특정 ID를 가진 게시글의 정보를 조회
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 게시글의 정보
     */
    BoardDTO getBoardById(long boardId);

    /**
     * 특정 게시글의 조회수를 업데이트
     *
     * @param boardId 조회수를 업데이트할 게시글의 ID
     */
    void increaseViewsCount(long boardId);

    /**
     * 주어진 조건에 맞는 게시글의 수를 조회
     *
     * @param condition 게시글 조회 조건
     * @return 조건에 맞는 게시글의 수
     */
    int getBoardCount(SearchCriteria condition);

    /**
     * 새로운 게시글을 생성
     *
     * @param board 생성할 게시글의 정보
     */
    void write(BoardDTO board);

    /**
     * 특정 게시글을 업데이트
     *
     * @param board 업데이트할 게시글의 정보
     */
    void modify(BoardDTO board);

    /**
     * 특정 ID를 가진 게시글을 삭제
     *
     * @param boardId 삭제할 게시글의 ID
     */
    void delete(long boardId);

    /**
     * 특정 게시글의 암호화된 비밀번호 조회
     *
     * @param boardId 게시글 ID
     * @return 암호화된 비밀번호
     */
    String getPasswordEncryptById(long boardId);

    /**
     * 암호화된 비밀번호 조회
     *
     * @param password 입력받은 비밀번호
     * @return 암호화된 비밀번호
     */
    String getPasswordEncrypt(String password);
}
