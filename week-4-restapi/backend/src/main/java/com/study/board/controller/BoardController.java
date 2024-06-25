package com.study.board.controller;

import com.study.board.dto.BoardDTO;
import com.study.board.dto.FileDTO;
import com.study.board.dto.PasswordCheckRequest;
import com.study.board.exception.NotFoundException;
import com.study.board.exception.UnauthorizedException;
import com.study.board.service.BoardService;
import com.study.board.service.FileService;
import com.study.board.util.ResponseUtil;
import com.study.board.util.SearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 게시판 관련 요청을 처리하는 컨트롤러
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/boards/free")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final MessageSource messageSource;

    /**
     * 게시글 수, 게시글 목록 조회하는 메서드
     * - 검색 조건에 따라 결과를 필터링 할 수 있음
     *
     * @param criteria 게시판 요청 파라미터 (검색 조건 포함)
     * @return HTTP 상태 - 생성 : 200, map - 게시글 수, 게시글 목록
     */
    @GetMapping
    public ResponseEntity<?> findAll(
            @Valid SearchCriteria criteria
    ) {
        List<BoardDTO> boards = boardService.getAllBoards(criteria);
        if(boards.isEmpty()) {
            throw new NotFoundException("게시글 목록");
        }

        int totalPostCount = boardService.getBoardCount(criteria);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("totalPostCount", totalPostCount);
        response.put("boards", boards);

        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 특정 ID를 가진 게시글을 상세 조회하는 메서드
     *
     * @param id 조회할 게시글의 ID
     * @return HTTP 상태 200, map - 게시글, 파일 목록
     */
    @GetMapping("/{seq}")
    public ResponseEntity<?> findById(@PathVariable("seq") long id) {
        boardService.increaseViewsCount(id);
        BoardDTO board = boardService.getBoardById(id);

        if(Objects.isNull(board)) {
//            return ResponseEntity.notFound().build();
            throw new NotFoundException("해당 게시글");
        }

        List<FileDTO> files = fileService.getAllFilesByBoardId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("board", board);
        response.put("files", files);

//        return ResponseEntity.ok().body(response);
//        return ResponseUtil.builder()
//                .status(HttpStatus.OK)
//                .body(response)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 새로운 게시글을 생성하는 메서드
     *
     * @param board         저장할 게시글 데이터
//     * @param bindingResult the binding result
     * @param uploadFiles   첨부 파일 리스트
     * @return HTTP 상태 201, 게시글 ID
     */
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @ModelAttribute BoardDTO board,
//            BindingResult bindingResult,
            @RequestParam(value = "uploadFile", required = false)
            List<MultipartFile> uploadFiles) {
        // TODO : 진행중
//        if (bindingResult.hasErrors()) {
////            return ResponseEntity.badRequest().build();
////            throw new BadRequestException("유효성 검증 실패");
//        }

        long id = boardService.write(board);
        if (!Objects.isNull(uploadFiles) && !uploadFiles.isEmpty()) {
            fileService.handleFileUpload(id, uploadFiles);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED);
        response.put("id", id);

//        return ResponseEntity.status(HttpStatus.CREATED).body(id);
//        return ResponseUtil.builder()
//                .status(HttpStatus.CREATED)
//                .body(id)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param board    수정할 게시글 데이터
     * @param newFiles 첨부 파일 리스트
     * @return HTTP 상태 201
     */
    @PutMapping
    public ResponseEntity<?> modify(
            @ModelAttribute BoardDTO board,
            @RequestParam(value = "uploadFile", required = false)
            List<MultipartFile> newFiles) {
        boolean isPasswordCorrect = boardService.checkPassword(
                board.getBoardId(), board.getPassword());
        if (!isPasswordCorrect) {
            throw new UnauthorizedException("Incorrect password");
        }

        boardService.modify(board);
        if (!Objects.isNull(newFiles) && !newFiles.isEmpty()) {
            fileService.handleFileUpload(board.getBoardId(), newFiles);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED);

//        return ResponseEntity.status(HttpStatus.CREATED).build();
//        return ResponseUtil.builder()
//                .status(HttpStatus.CREATED)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 게시글을 삭제하는 메서드
     *
     * @param boardId 삭제할 게시글의 ID
     * @return HTTP 상태 - 성공 : 204
     */
    @DeleteMapping("/{seq}")
    public ResponseEntity<?> delete(
            @PathVariable("seq") long boardId,
            @RequestHeader("X-Post-password") String password) {
        boolean isPasswordCorrect = boardService.checkPassword(boardId, password);
        if (!isPasswordCorrect) {
            throw new UnauthorizedException("Incorrect password");
        }

        boardService.delete(boardId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NO_CONTENT);

//        return ResponseEntity.noContent().build();
//        return ResponseUtil.builder()
//                .status(HttpStatus.NO_CONTENT)
//                .build();
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 게시글의 비밀번호를 확인하는 메서드
     *
     * @param request 클라이언트로 받은 JSON 요청 본문
     * @return HTTP 상태 - 일치 : 200
     */
    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(
            @Valid @RequestBody
            PasswordCheckRequest request,
            BindingResult bindingResult) {
        boolean isPasswordCorrect = boardService.checkPassword(
                request.getBoardId(), request.getPassword());

        if (isPasswordCorrect) {
//            return ResponseEntity.ok().build();
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK);

//            return ResponseUtil.builder()
//                    .status(HttpStatus.OK)
//                    .build();
            return ResponseUtil.getResponseEntity(response);
        }
        throw new UnauthorizedException("Incorrect password");
    }
}
