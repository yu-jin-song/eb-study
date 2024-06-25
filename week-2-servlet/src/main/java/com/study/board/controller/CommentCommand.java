package com.study.board.controller;

import com.study.board.dao.CommentDAO;
import com.study.board.util.HttpHeaders;
import com.study.board.vo.CommentVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 댓글 관련 요청을 처리하는 커맨드 클래스입니다.
 * 이 클래스는 사용자의 댓글 작성 요청을 받아 데이터베이스에 저장합니다.
 */
public class CommentCommand implements Command {
    /**
     * 댓글 작성 요청을 처리합니다.
     * 사용자가 입력한 댓글 데이터를 받아 데이터베이스에 저장하고,
     * 저장이 완료된 후, 해당 댓글이 포함된 게시글의 상세보기 페이지로 리다이렉트합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 댓글 입력 정보 추출
        request.setCharacterEncoding(HttpHeaders.CHARSET_UTF_8);

        String boardId = request.getParameter("board_id");
        String writer = request.getParameter("writer");
        String comment = request.getParameter("comment");

        // 2. DB INSERT
//        CommentVO vo = new CommentVO();
        CommentVO vo = CommentVO.builder()
                .boardId(Long.parseLong(boardId))
                .writer(writer)
                .comment(comment)
                .build();
//        vo.setBoardId(Long.parseLong(boardId));
//        vo.setWriter(writer);
//        vo.setComment(comment);

        CommentDAO commentDAO = new CommentDAO();
        commentDAO.insertComment(vo);

        // 3. 해당 댓글이 존재하는 게시글로 이동
        response.sendRedirect("view.do?seq=" + boardId);
    }
}
