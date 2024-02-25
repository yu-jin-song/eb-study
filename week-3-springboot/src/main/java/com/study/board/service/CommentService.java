package com.study.board.service;

import com.study.board.dao.CommentMapper;
import com.study.board.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 관련 서비스를 제공하는 클래스
 */
@Service
public class CommentService {

    private final CommentMapper commentMapper;

    /**
     * 댓글 서비스 생성자
     * CommentMapper를 주입받아 초기화
     *
     * @param commentMapper 댓글 정보에 접근하기 위한 MyBatis 매퍼
     */
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    /**
     * 특정 게시글에 대한 댓글 목록 조회
     * 주어진 게시글 ID에 해당하는 모든 댓글을 데이터베이스에서 가져오는 메소드
     *
     * @param boardId 댓글을 조회할 게시글의 ID
     * @return 조회된 댓글 목록을 담고 있는 List 객체
     */
    public List<CommentDTO> getAllCommentsByBoardId(long boardId) {
        return commentMapper.getAllCommentsByBoardId(boardId);
    }

    /**
     * 새로운 댓글 생성
     * 주어진 Comment 객체를 데이터베이스에 저장하는 메소드
     * 트랜잭션을 사용하여 댓글 생성 과정을 관리
     *
     * @param comment 생성할 댓글 정보를 담고 있는 Comment 객체
     */
    public void write(CommentDTO comment) {
        commentMapper.write(comment);
    }
}
