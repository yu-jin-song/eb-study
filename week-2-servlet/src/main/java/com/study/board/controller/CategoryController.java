package com.study.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * 카테고리 정보를 관리하는 서블릿 클래스입니다.
 * 이 클래스는 카테고리 관련 요청을 처리하며, 카테고리 목록을 조회하여 사용자에게 보여줍니다.
 */
@WebServlet("/category.do")
public class CategoryController extends HttpServlet {

    /**
     * HTTP GET 요청을 처리합니다.
     * 카테고리 관련 작업을 위한 GET 요청을 Command 객체를 통해 처리합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 서블릿 예외 발생
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외 발생
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * HTTP 요청을 처리합니다.
     * 요청을 CommandFactory에 전달하여 적절한 Command 객체를 받고, 해당 Command의 execute 메소드를 호출합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 서블릿 예외 발생
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외 발생
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getCommand(request);
        command.execute(request, response);
    }
}
