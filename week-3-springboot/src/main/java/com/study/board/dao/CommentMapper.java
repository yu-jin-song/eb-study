package com.study.board.dao;

import com.study.board.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 게시글의 댓글에 대한 데이터 접근을 위한 MyBatis 매퍼 인터페이스
 * 댓글 관련 데이터베이스 연산 정의
 */
@Mapper
public interface CommentMapper {
    /**
     * 특정 게시글 ID에 해당하는 모든 댓글의 리스트를 조회
     *
     * @param boardId 댓글을 조회할 게시글의 ID
     * @return 조회된 댓글들의 리스트
     */
    List<CommentDTO> getAllCommentsByBoardId(long boardId);

    /**
     * 새로운 댓글을 생성
     *
     * @param comment 생성할 댓글의 정보를 담은 DTO
     */
    void write(CommentDTO comment);
}
