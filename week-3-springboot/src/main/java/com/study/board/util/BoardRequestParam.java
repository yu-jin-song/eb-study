package com.study.board.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * 게시글 관련 RequestParam을 관리하는 DTO 클래스
 */
@Getter
@Setter
public class BoardRequestParam {
    private int page = 1;
    private String selectedCategoryId = "-1";
    private String searchKeyword = "";
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    /**
     * LocalDateTime 타입으로 시작일자 얻어오는 메서드
     * - 시작일자가 없는 경우 현재 날짜로부터 1년 전으로 조정
     * - 시작시간 -> 00:00
     *
     * @return 시작일자
     */
    public LocalDateTime getStartDate() {
        if (Objects.isNull(startDate)) {
            startDate = LocalDate.now().minusYears(1);
        }
        return startDate.atStartOfDay();
    }

    /**
     * LocalDateTime 타입으로 종료일자 얻어오는 메서드
     * - 종료일자가 없는 경우 현재 날짜로 조정
     * - 종료시간 -> 23:59:59
     *
     * @return the end date
     */
    public LocalDateTime getEndDate() {
        if (Objects.isNull(endDate)) {
            endDate = LocalDate.now();
        }
        return endDate.plusDays(1).atStartOfDay().minusSeconds(1);
    }

    /**
     * 현재 페이지 얻어오기
     * - 페이지가 0인 경우 1로 조정
     *
     * @return 현재 페이지
     */
    public int getPage() {
        if (page == 0) {
            page = 1;
        }
        return page;
    }

    /**
     * 선택한 카테고리 ID 가져오기
     * - String 타입을 Integer 타입으로 변환
     * - 선택한 카테고리가 없는 경우 -1로 조정
     *
     * @return 선택된 카테고리 ID
     */
    public int getSelectedCategoryId() {
        if (Objects.isNull(selectedCategoryId) || selectedCategoryId.isEmpty()) {
            selectedCategoryId = "-1";
        }
        return Integer.parseInt(selectedCategoryId);
    }

    @Override
    public String toString() {
        return "BoardRequestParam{" +
                "page=" + page +
                ", selectedCategoryId=" + selectedCategoryId +
                ", searchKeyword='" + searchKeyword + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
