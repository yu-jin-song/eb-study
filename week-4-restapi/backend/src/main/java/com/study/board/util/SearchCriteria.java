package com.study.board.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 게시글 검색에 사용되는 조건과 요청 파라미터를 관리하는 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    /** 현재 페이지 */
    @Positive
    private int page = 1;

    /** 현재 페이지의 시작 레코드 위치 */
    @PositiveOrZero
    private int startIndex = 0;

    /** 한 페이지에 표시될 게시글 수 */
    @Positive
    private int pageSize = 10;

    @NotBlank
    private String selectedCategoryId = "-1";

    @NotNull
    private String searchKeyword = "";

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * LocalDateTime 타입으로 시작일자 얻어오는 메서드
     * - 시작일자가 없는 경우 현재 날짜로부터 1년 전으로 조정
     * - 시작시간 => 00:00
     *
     * @return 시작일자
     */
    public LocalDateTime getStartDateTime() {
        if (Objects.isNull(startDate)) {
            return LocalDate.now()
                    .minusYears(1)
                    .atStartOfDay();
        }
        return startDate.atStartOfDay();
    }

    /**
     * LocalDateTime 타입으로 종료일자 얻어오는 메서드
     * - 종료일자가 없는 경우 현재 날짜로 조정
     * - 종료시간 => 23:59:59
     *
     * @return 종료일자
     */
    public LocalDateTime getEndDateTime() {
        if (Objects.isNull(endDate)) {
            return LocalDate.now()
                    .plusDays(1)
                    .atStartOfDay()
                    .minusSeconds(1);
        }
        return endDate
                .plusDays(1)
                .atStartOfDay()
                .minusSeconds(1);
    }

    /**
     * 선택한 카테고리 ID 가져오기
     * - String 타입을 Integer 타입으로 변환
     * - 선택한 카테고리가 없는 경우 -1로 조정
     *
     * @return 선택된 카테고리 ID
     */
    public int getSelectedCategoryIdInt() {
        if (Objects.isNull(selectedCategoryId) || selectedCategoryId.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(selectedCategoryId);
    }

    /**
     * 리다이렉트 파라미터를 위한 URI 쿼리 스트링을 생성하는 메서드
     *
     * @return URI 인코딩이 적용된 쿼리 스트링
     */
    public String toUriString() {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", this.page)
                .queryParam("startIndex", this.startIndex)
                .queryParam("pageSize", this.pageSize)
                .queryParam("selectedCategoryId", getSelectedCategoryIdInt())
                .queryParam("searchKeyword", this.searchKeyword)
                .queryParam("startDate", this.startDate)
                .queryParam("endDate", this.endDate)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "page=" + page +
                ", startIndex=" + startIndex +
                ", pageSize=" + pageSize +
                ", selectedCategoryId='" + selectedCategoryId + '\'' +
                ", searchKeyword='" + searchKeyword + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
