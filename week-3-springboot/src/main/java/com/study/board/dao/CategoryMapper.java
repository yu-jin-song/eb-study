package com.study.board.dao;

import com.study.board.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 카테고리 정보에 접근하기 위한 MyBatis 매퍼 인터페이스
 * 카테고리 관련 데이터베이스 연산 정의
 */
@Mapper
public interface CategoryMapper {
    /**
     * 모든 카테고리의 리스트를 조회
     *
     * @return 조회된 모든 카테고리의 리스트
     */
    List<CategoryDTO> getAllCategories();
}
