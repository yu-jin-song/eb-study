package com.study.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 댓글 정보를 담는 Value Object 클래스입니다.
 * 댓글의 ID, 해당 게시글 ID, 댓글 내용, 작성자, 생성 시간 등의 정보를 관리합니다.
 */
@Setter
@Getter
@Builder
public class CommentVO {
    private long id;
    private long boardId;
    private String comment;
    private String writer;
    private LocalDateTime createdAt;
}
