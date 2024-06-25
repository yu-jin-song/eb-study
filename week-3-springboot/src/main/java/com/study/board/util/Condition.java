package com.study.board.util;

import lombok.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * 게시글 검색 조건을 관리하는 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {
    private int startIndex = 1;
    private int pageSize = 10;
    private int page = 1;   // 현재 페이지
    private LocalDateTime startDate;
//    private String startDate;
    private LocalDateTime endDate;
//    private String endDate;
    private int selectedCategoryId;
    private String searchKeyword;

    /**
     * 시작일자 yyyy-MM-dd 형식으로 가져오는 메서드
     *
     * @return 시작일자
     */
    public String getFromDate() {
        return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 종료일자 yyyy-MM-dd 형식으로 가져오는 메서드
     *
     * @return 종료일자
     */
    public String getToDate() {
        return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 리다이렉트 파라미터를 위한 쿼리스트링 반환 메서드
     *
     * @return 쿼리스트링
     */
    public String toQueryString() {
        return "page=" + this.page +
                "&selectedCategoryId=" + this.selectedCategoryId +
                "&searchKeyword=" + URLEncoder.encode(this.searchKeyword, StandardCharsets.UTF_8) +
                "&startDate=" + getFromDate() +
                "&endDate=" + getToDate();
    }

    /**
     * Condition 객체의 상태를 문자열로 반환
     *
     * @return Condition 객체의 현재 상태를 나타내는 문자열
     */
    @Override
    public String toString() {
        return "Condition{" +
                "page=" + page +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", selectedCategoryId=" + selectedCategoryId +
                ", searchKeyword='" + searchKeyword + '\'' +
                '}';
    }
}
