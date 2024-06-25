package com.study.board.controller;

import com.study.board.dao.CategoryDAO;
import com.study.board.vo.CategoryVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 카테고리 정보를 처리하는 커맨드 클래스입니다.
 * 이 클래스는 카테고리 관련 요청을 처리하며, 카테고리 목록을 조회하여 사용자에게 보여줍니다.
 */
public class CategoryCommand implements Command {
    /**
     * 카테고리 요청을 처리합니다.
     * 사용자의 요청에 따라 카테고리 목록을 조회하고, 결과를 list.jsp 페이지에 전달합니다.
     * 카테고리 정보는 CategoryDAO를 통해 데이터베이스에서 가져옵니다.
     *
     * @param request  클라이언트로부터 받은 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보낼 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 서블릿 예외 발생
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외 발생
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 카테고리 목록을 조회하는 로직
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryVO> categoryList = categoryDAO.getCategoryList();
        request.setAttribute("category_list", categoryList);

        // 조회 결과를 list.jsp 페이지로 포워딩
        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }
}
