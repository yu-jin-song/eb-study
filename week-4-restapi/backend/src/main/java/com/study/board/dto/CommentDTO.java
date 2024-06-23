package com.study.board.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 댓글 정보를 나타내는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private long commentId;
    private long boardId;
    private String comment;
    private String writer;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", boardId=" + boardId +
                ", comment='" + comment + '\'' +
                ", writer='" + writer + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
