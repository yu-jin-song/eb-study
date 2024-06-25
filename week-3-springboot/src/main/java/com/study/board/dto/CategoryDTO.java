package com.study.board.dto;

import lombok.*;

/**
 * 카테고리 정보를 나타내는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private int categoryId;
    private String name;

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }
}
