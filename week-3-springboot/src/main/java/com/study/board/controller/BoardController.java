package com.study.board.controller;

import com.study.board.dto.*;
import com.study.board.service.BoardService;
import com.study.board.service.CategoryService;
import com.study.board.service.CommentService;
import com.study.board.service.FileService;
import com.study.board.util.BoardRequestParam;
import com.study.board.util.Condition;
import com.study.board.util.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 관련 요청을 처리하는 컨트롤러
 */
@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    /**
     * 컨트롤러의 주요 서비스 컴포넌트를 초기화
     *
     * @param boardService 게시판 서비스
     * @param fileService 파일 서비스
     * @param categoryService 카테고리 서비스
     * @param commentService 댓글 서비스
     */
    public BoardController(
            BoardService boardService,
            FileService fileService,
            CategoryService categoryService,
            CommentService commentService
    ) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }


    /**
     * 게시글 목록 조회하는 메서드
     * 검색 조건에 따라 결과를 필터링 할 수 있음
     *
     * @param params 게시판 요청 파라미터 (검색 조건 포함)
     * @param model 뷰에 전달할 모델 객체
     * @return 게시글 목록 페이지의 뷰 이름
     */
    @GetMapping("/list")
    public String findAll(BoardRequestParam params, Model model) {
        //검색 조건 추출
        Condition condition = new Condition();

        // 시작 ~ 종료 일자
        condition.setStartDate(params.getStartDate());
        condition.setEndDate(params.getEndDate());

        // 카테고리 및 키워드
        condition.setSelectedCategoryId(params.getSelectedCategoryId());
        condition.setSearchKeyword(params.getSearchKeyword());

        // 페이징 처리 및 게시글 목록 조회
        // TODO : 화면단에서 처리
        // 총 게시물 수
        int totalPostCount = boardService.getBoardCount(condition);

        // 생성인자로 총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalPostCount, params.getPage());
        model.addAttribute("pagination", pagination);

        // DB 조회 시작 index
        condition.setStartIndex(pagination.getStartIndex());
        // 한 페이지 당 출력할 게시물 수
        condition.setPageSize(pagination.getPageSize());
        model.addAttribute("condition", condition);

        List<BoardDTO> boards = new ArrayList<>();
        if (totalPostCount > 0) {
            boards = boardService.getAllBoards(condition);
        }
        model.addAttribute("posts", boards);


        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "list";
    }

    /**
     * 특정 ID를 가진 게시글을 상세 조회하는 메서드
     *
     * @param boardId 조회할 게시글의 ID
     * @param params 게시판 요청 파라미터
     * @param model 뷰에 전달할 모델 객체
     * @return 게시글 상세 페이지의 뷰 이름
     */
    @GetMapping("/view/{seq}")
    public String findById(
            @PathVariable("seq") long boardId,
            BoardRequestParam params,
            Model model) {

        boardService.increaseViewsCount(boardId);
        BoardDTO board = boardService.getBoardById(boardId);
        model.addAttribute("post", board);

        List<FileDTO> files = fileService.getAllFilesByBoardId(boardId);
        model.addAttribute("files", files);

        List<CommentDTO> comments = commentService.getAllCommentsByBoardId(boardId);
        model.addAttribute("comments", comments);

        Condition condition = Condition.builder()
                .page(params.getPage())
                .selectedCategoryId(params.getSelectedCategoryId())
                .searchKeyword(params.getSearchKeyword())
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .build();
        model.addAttribute("condition", condition);

        return "view";
    }

    /**
     * 게시글 작성 폼 페이지 출력 메서드
     *
     * @param params 게시판 요청 파라미터
     * @param model 뷰에 전달할 모델 객체
     * @return 게시글 작성 폼 페이지의 뷰 이름
     */
    @GetMapping("/write")
    public String writeForm(BoardRequestParam params, Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Condition condition = Condition.builder()
                .page(params.getPage())
                .selectedCategoryId(params.getSelectedCategoryId())
                .searchKeyword(params.getSearchKeyword())
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .build();
        model.addAttribute("condition", condition);

        return "write";
    }

    /**
     * 새로운 게시글을 생성하는 메서드
     *
     * @param board 저장할 게시글 데이터
     * @param uploadFiles 첨부 파일 리스트
     * @param params 게시판 요청 파라미터
     * @return 저장된 게시글의 상세 페이지로의 리다이렉트 경로
     * @throws IOException 파일 처리 중 발생 가능한 예외
     */
    @PostMapping("/write")
    public String write(
            @Valid @ModelAttribute BoardDTO board,
            BindingResult bindingResult,
            @RequestParam("uploadFile") List<MultipartFile> uploadFiles,
            BoardRequestParam params,
            Model model) throws IOException {

        Condition condition = Condition.builder()
                .page(params.getPage())
                .selectedCategoryId(params.getSelectedCategoryId())
                .searchKeyword(params.getSearchKeyword())
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .build();

        // TODO : 진행중
        // 유효성 검사 실패 시 다시 작성 폼 보여줌
        if (bindingResult.hasErrors()) {
            model.addAttribute("condition", condition);
            return "write";
        }

        long id = boardService.write(board);

        fileService.handleFileUpload(id, uploadFiles, null);

        return "redirect:/view/" + id + "?" + condition.toQueryString();
    }

    /**
     * 게시글 수정 폼 페이지로 이동하는 메서드
     *
     * @param boardId 수정할 게시글의 ID
     * @param params 게시판 요청 파라미터
     * @param model 뷰에 전달할 모델 객체
     * @return 게시글 수정 폼 페이지의 뷰 이름
     */
    @GetMapping("/modify")
    public String modifyForm(
            @RequestParam("seq") long boardId,
            BoardRequestParam params,
            Model model) {
        BoardDTO board = boardService.getBoardById(boardId);
        model.addAttribute("post", board);

        List<FileDTO> files = fileService.getAllFilesByBoardId(boardId);
        model.addAttribute("files", files);

        Condition condition = Condition.builder()
                .page(params.getPage())
                .selectedCategoryId(params.getSelectedCategoryId())
                .searchKeyword(params.getSearchKeyword())
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .build();
        model.addAttribute("condition", condition);

        return "modify";
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param board 수정할 게시글 데이터
     * @param newFiles 첨부 파일 리스트
     * @param params 게시판 요청 파라미터
     * @return 수정된 게시글의 상세 페이지로의 리다이렉트 경로
     * @throws IOException 파일 처리 중 발생 가능한 예외
     */
    @PutMapping("/modify")
    public String modify(
            @ModelAttribute BoardDTO board,
            @RequestParam("uploadFile") List<MultipartFile> newFiles,
            @RequestParam(value = "existedFileName", required = false) List<String> existedFiles,
            BoardRequestParam params) throws IOException {

        long id = boardService.modify(board);
        fileService.handleFileUpload(id, newFiles, existedFiles);

        // 리다이렉트 파라미터
        Condition condition = Condition.builder()
                .page(params.getPage())
                .selectedCategoryId(params.getSelectedCategoryId())
                .searchKeyword(params.getSearchKeyword())
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .build();

        return "redirect:/view/" + id + "?" + condition.toQueryString();
    }

    /**
     * 게시글을 삭제하는 메서드
     *
     * @param boardId 삭제할 게시글의 ID
     * @return HTTP 상태 - 성공 : 200 / 실패 : 500
     */
    @DeleteMapping("/delete/{seq}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("seq") long boardId) {
        try {
            boardService.delete(boardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 게시글의 비밀번호를 확인하는 메서드
     *
     * @param request 클라이언트로 받은 JSON 요청 본문
     * @return HTTP 상태 - 일치 : 200 / 불일치 : 403 / 에러 : 500
     */
    @PostMapping("/check-password")
    @ResponseBody
    public ResponseEntity<?> checkPassword(@RequestBody PasswordCheckRequest request) {
        try {
            boolean isPasswordCorrect = boardService.checkPassword(
                    request.getBoardId(), request.getPassword());

            if (isPasswordCorrect) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
