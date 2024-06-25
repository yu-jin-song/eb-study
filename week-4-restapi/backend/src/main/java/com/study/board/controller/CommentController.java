package com.study.board.controller;

import com.study.board.dto.CommentDTO;
import com.study.board.exception.NotFoundException;
import com.study.board.service.CommentService;
import com.study.board.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 댓글 관련 요청을 처리하는 컨트롤러
 */
@AllArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성 요청 처리
     *
     * @param comment 사용자로부터 입력 받은 댓글 데이터
     * @return HTTP 상태 - 성공 : 201
     */
    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute CommentDTO comment) {
        commentService.write(comment);
//        return ResponseEntity.status(HttpStatus.CREATED).build();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED);

//        return ResponseUtil.builder()
//                .status(HttpStatus.CREATED)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 특정 게시글의 모든 댓글 조회
     *
     * @param boardId 조회할 게시글의 ID
     * @return 게시글에 대한 댓글 목록
     * @throws NotFoundException 게시글에 대한 댓글이 없는 경우 발생
     */
    @GetMapping
    public ResponseEntity<?> findAllByBoardId(@RequestParam("boardId") long boardId) {
        List<CommentDTO> comments = commentService.getAllCommentsByBoardId(boardId);

        if(comments.isEmpty()) {
            throw new NotFoundException("댓글 목록");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);

//        return ResponseEntity.ok().body(comments);
//        return ResponseUtil.builder()
//                .status(HttpStatus.OK)
//                .body(comments)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }
}
