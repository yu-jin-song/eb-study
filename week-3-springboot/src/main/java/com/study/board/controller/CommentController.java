package com.study.board.controller;

import com.study.board.dto.CommentDTO;
import com.study.board.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 댓글 관련 요청을 처리하는 컨트롤러
 */
@Controller
public class CommentController {
    private final CommentService commentService;

    /**
     * 필요한 서비스를 주입받아 컨트롤러 초기화
     *
     * @param commentService 댓글 관련 서비스
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 생성 요청 처리
     *
     * @param comment 사용자로부터 입력 받은 댓글 데이터
     * @return 해당 댓글이 속한 게시글의 상세 보기 페이지로 리다이렉트하는 경로
     */
    @PostMapping("/comments")
    public String write(@ModelAttribute CommentDTO comment) {
        commentService.write(comment);
        return "redirect:/view/" + comment.getBoardId();
    }
}
