package com.study.board.service;

import com.study.board.dao.CategoryMapper;
import com.study.board.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 관련 서비스를 제공하는 클래스
 */
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 카테고리 서비스 생성자
     * CategoryMapper를 주입받아 초기화
     *
     * @param categoryMapper 카테고리 정보에 접근하기 위한 MyBatis 매퍼
     */
    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    /**
     * 카테고리 전체 목록 조회
     * 데이터베이스에서 카테고리 목록을 가져오는 메소드.
     *
     * @return 카테고리 목록을 담고 있는 List 객체
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.getAllCategories();
    }
}
