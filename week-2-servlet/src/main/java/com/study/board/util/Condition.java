package com.study.board.util;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 게시판의 조회 조건을 정의하는 클래스
 * 포함 : 시작 행, 종료 행, 시작 날짜, 종료 날짜, 카테고리 ID, 검색 키워드
 */
@Setter
@Getter
public class Condition {

    private int startRow;
    private int endRow;
    private Timestamp startDate;
    private Timestamp endDate;
    private int categoryId;
    private String keyword;

    /**
     * Condition 객체의 상태를 문자열로 반환합니다.
     *
     * @return Condition 객체의 현재 상태를 나타내는 문자열
     */
    @Override
    public String toString() {
        return "Condition{" +
                "startRow=" + startRow +
                ", endRow=" + endRow +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", categoryId=" + categoryId +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
