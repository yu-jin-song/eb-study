package com.study.connection.board.vo;

import java.time.LocalDateTime;

public class CommentVO {

    private long id;
    private long boardId;
    private String comment;
    private String writer;
    private LocalDateTime createdAt;

    public CommentVO() {
        super();
    }

    // 댓글 작성을 위한 vo
    public CommentVO(long boardId, String comment, String writer, LocalDateTime createdAt) {
        super();
        this.boardId = boardId;
        this.comment = comment;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long board_id) {
        this.boardId = boardId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "id=" + id +
                ", board_id=" + boardId +
                ", comment='" + comment + '\'' +
                ", writer='" + writer + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }


}
