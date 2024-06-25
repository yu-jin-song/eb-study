package com.study.board.controller;

import com.study.board.dto.CategoryDTO;
import com.study.board.exception.NotFoundException;
import com.study.board.service.CategoryService;
import com.study.board.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회 메서드
     *
     * @return 카테고리 목록을 담은 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<CategoryDTO> categories = categoryService.getAllCategories();

        if(categories.isEmpty()) {
            throw new NotFoundException("카테고리 목록");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);

//        return ResponseEntity.ok().body(categories);
//        return ResponseUtil.builder()
//                .status(HttpStatus.OK)
//                .body(categories)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }
}
