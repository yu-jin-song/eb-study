package com.study.connection.board.vo;

import java.time.LocalDateTime;

public class BoardVO {

    private long id;
    private int categoryId;
    private String title;
    private String content;
    private String writer;
    private String password;
    private long views;
    private LocalDateTime createdAt;
    private LocalDateTime modifedAt;

    public BoardVO() {
        super();
    }

    // 게시글 목록 전체 조회 시 필요한 vo
    public BoardVO(long id, int categoryId, String title, String writer, String password, long views, LocalDateTime createdAt, LocalDateTime modifedAt) {
        super();
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.writer = writer;
        this.password = password;
        this.views = views;
        this.createdAt = createdAt;
        this.modifedAt = modifedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifedAt() {
        return modifedAt;
    }

    public void setModifedAt(LocalDateTime modifedAt) {
        this.modifedAt = modifedAt;
    }

    @Override
    public String toString() {
        return "BoardVO{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", password='" + password + '\'' +
                ", views=" + views +
                ", createdAt=" + createdAt +
                ", modifedAt=" + modifedAt +
                '}';
    }
}
