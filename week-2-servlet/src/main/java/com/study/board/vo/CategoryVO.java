package com.study.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 정보를 담는 Value Object 클래스입니다.
 * 카테고리의 ID와 이름을 관리합니다.
 */
@Setter
@Getter
@Builder
public class CategoryVO {
    private int id;
    private String name;
}
