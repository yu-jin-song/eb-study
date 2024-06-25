package com.study.board.controller;

import com.study.board.dao.BoardDAO;
import com.study.board.dao.CategoryDAO;
import com.study.board.dao.FileDAO;
import com.study.board.vo.BoardVO;
import com.study.board.vo.FileVO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 게시글 수정 폼 페이지를 표시하는 커맨드 클래스입니다.
 * 이 클래스는 사용자가 게시글을 수정하기 위해 필요한 정보를 조회하고, 결과를 modify.jsp 페이지에 전달합니다.
 */
public class BoardModifyFormCommand implements Command {

    /**
     * 게시글 수정 폼 요청을 처리합니다.
     * 사용자가 요청한 게시글의 정보와 카테고리 목록을 조회하고,
     * 조회된 데이터를 modify.jsp 페이지에 전달합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 수정할 게시글 번호 추출
        String id = request.getParameter("seq");

        // 수정할 게시글 정보 가져오기
        BoardDAO boardDAO = new BoardDAO();
        BoardVO board = boardDAO.getBoard(Long.parseLong(id));

        // 첨부파일 정보 가져오기
        FileDAO fileDAO = new FileDAO();
        List<FileVO> fileList = fileDAO.getFileList(Long.parseLong(id));

        // 카테고리명 가져오기
        CategoryDAO categoryDAO = new CategoryDAO();
        int categoryId = board.getCategoryId();
        String categoryName = categoryDAO.getCategoryById(categoryId).getName();

        request.setAttribute("board", board);
        request.setAttribute("file_list", fileList);
        request.setAttribute("category_name", categoryName);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/modify.jsp");
        dispatcher.forward(request, response);
    }
}
