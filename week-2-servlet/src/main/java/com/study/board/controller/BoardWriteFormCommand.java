package com.study.board.controller;

import com.study.board.dao.CategoryDAO;
import com.study.board.vo.CategoryVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 게시글 작성 폼 페이지를 표시하는 커맨드 클래스입니다.
 * 이 클래스는 게시글 작성에 필요한 카테고리 목록을 조회하고, 결과를 write.jsp 페이지에 전달합니다.
 */
public class BoardWriteFormCommand implements Command {
  /**
   * 게시글 작성 폼 요청을 처리합니다.
   * 사용자가 게시글을 작성할 수 있는 폼 페이지를 요청할 때, 필요한 카테고리 목록을 조회하고,
   * 조회된 데이터를 write.jsp 페이지에 전달합니다.
   *
   * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
   * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
   * @throws ServletException 요청 처리 중 발생하는 예외
   * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
   */
  public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   CategoryDAO categoryDAO = new CategoryDAO();
   List<CategoryVO> categoryList = categoryDAO.getCategoryList();

   request.setAttribute("category_list", categoryList);

   RequestDispatcher dispatcher = request.getRequestDispatcher("/write.jsp");
   dispatcher.forward(request, response);
  }
}