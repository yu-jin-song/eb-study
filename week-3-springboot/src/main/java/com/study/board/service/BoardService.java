package com.study.board.service;

import com.study.board.dao.FileMapper;
import com.study.board.dto.BoardDTO;
import com.study.board.dao.BoardMapper;
import com.study.board.util.Condition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 게시글 관련 서비스를 제공하는 클래스
 */
@AllArgsConstructor
@Service
@Slf4j
public class BoardService {

    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

    /**
     * 저장된 게시글의 총 개수를 조회하는 메서드
     *
     * @return 게시글의 총 개수
     */
    public int getBoardCount(Condition condition) {
        return boardMapper.getBoardCount(condition);
    }

    /**
     * 조건에 맞는 게시글 목록을 조회하는 메서드
     *
     * @param condition 검색 조건을 포함하는 Condition 객체
     * @return 조건에 맞는 게시글 목록
     */
    public List<BoardDTO> getAllBoards(Condition condition) {
        List<BoardDTO> boards = boardMapper.getAllBoards(condition);
        boards.forEach(board -> {
            String title = board.getTitle();

            // 80자 넘는 경우
            int titleLength = title.getBytes(StandardCharsets.UTF_8).length;
            if (titleLength > 80) {
                int endIndex = Math.min(title.length(), 27);
                board.setTitle(title.substring(0, endIndex) + "...");
            }

            // 첨부파일 존재 여부 확인
            byte fileCount = fileMapper.getFileCount(board.getBoardId());
            boolean hasFiles = fileCount > 0;
            board.setHasFiles(hasFiles);
        });
        return boards;
    }

    /**
     * 특정 ID를 가진 게시글 조회 메서드
     *
     * @param boardId 게시글의 ID
     * @return 조회된 게시글
     */
    public BoardDTO getBoardById(long boardId) {
        BoardDTO board = boardMapper.getBoardById(boardId);
        return board;
    }

    /**
     * 조회수 갱신 메서드
     *
     * @param boardId 조회하려는 게시글 ID
     */
    public void increaseViewsCount(long boardId) {
        boardMapper.increaseViewsCount(boardId);
    }

    /**
     * 새 게시글 생성 메서드
     * 트랜잭션을 사용하여 게시글 생성 과정을 관리
     *
     * @param board 생성할 게시글 정보
     * @return 생성된 게시글의 ID
     */
    public long write(BoardDTO board) {
        boardMapper.write(board);
        return board.getBoardId();
    }

    /**
     * 게시글 수정 메서드
     * 트랜잭션을 사용하여 게시글 수정 과정을 관리
     *
     * @param board 수정할 게시글 정보
     * @return 수정된 게시글의 ID
     */
    public long modify(BoardDTO board) {
        boardMapper.modify(board);
        return board.getBoardId();
    }

    /**
     * 특정 ID를 가진 게시글 삭제 메서드
     * 트랜잭션을 사용하여 게시글 삭제 과정을 관리
     *
     * @param boardId 삭제할 게시글의 ID
     */
    public void delete(long boardId) {
        boardMapper.delete(boardId);
    }

    /**
     * 게시글의 비밀번호를 확인하는 메서드
     *
     * @param boardId  수정 / 삭제하려는 게시글 ID
     * @param password 입력받은 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean checkPassword(long boardId, String password) {
        String storedPassword = boardMapper.getPasswordById(boardId);
        String inputPassword = boardMapper.getPasswordHash(password);

        return storedPassword.equals(inputPassword);
    }
}
