package com.study.board.controller;

import com.study.board.dao.BoardDAO;
import com.study.board.dao.CategoryDAO;
import com.study.board.dao.CommentDAO;
import com.study.board.dao.FileDAO;
import com.study.board.vo.BoardVO;
import com.study.board.vo.CommentVO;
import com.study.board.vo.FileVO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 게시글 상세보기 요청을 처리하는 커맨드 클래스입니다.
 * 이 클래스는 사용자가 선택한 게시글의 상세 정보를 조회하고, 결과를 view.jsp 페이지에 전달합니다.
 */
public class BoardViewCommand implements Command {
    private final static Logger LOGGER
            = LoggerFactory.getLogger(BoardViewCommand.class);

    /**
     * 게시글 상세보기 요청을 처리합니다.
     * 사용자가 요청한 게시글 번호에 해당하는 게시글 정보와 댓글 목록을 조회하고,
     * 조회된 데이터를 view.jsp 페이지에 전달합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 상세 조회할 게시글 번호 추출
        String seq = request.getParameter("seq");

        // 상세 조회할 게시글 정보 가져오기
        BoardDAO boardDAO = new BoardDAO();
        BoardVO board = boardDAO.getBoard(Long.parseLong(seq));
        long views = boardDAO.updateViewsCnt(board);    // 조회수 갱신
        board.setViews(views);

        // 첨부 파일 가져오기
        FileDAO fileDAO = new FileDAO();
        List<FileVO> fileList = fileDAO.getFileList(Long.parseLong(seq));

        // 카테고리명 조회
        CategoryDAO categoryDAO = new CategoryDAO();
        int categoryId = board.getCategoryId();
        String categoryName = categoryDAO.getCategoryById(categoryId).getName();   // 현재 글의 카테고리명 조회

        // 댓글 가져오기
        CommentDAO commentDAO = new CommentDAO();
        List<CommentVO> commentList = commentDAO.getCommentList(board.getId());

        request.setAttribute("board", board);
        request.setAttribute("file_list", fileList);
        request.setAttribute("category_name", categoryName);
        request.setAttribute("comment_list", commentList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view.jsp?seq=" + seq);
        dispatcher.forward(request, response);
    }
}
