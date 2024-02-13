package com.study.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 게시판 게시글의 정보를 담는 Value Object 클래스입니다.
 * 게시글의 ID, 카테고리 ID, 제목, 내용, 작성자, 비밀번호, 조회수, 생성 및 수정 날짜 등의 정보를 관리합니다.
 */
@Setter
@Getter
@Builder
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

    /**
     * BoardVO 객체의 상태를 문자열로 반환합니다.
     *
     * @return BoardVO 객체의 현재 상태를 나타내는 문자열
     */
    @Override
    public String toString() {
        return "BoardDTO{" +
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
