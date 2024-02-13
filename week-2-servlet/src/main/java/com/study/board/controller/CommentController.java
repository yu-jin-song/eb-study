package com.study.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 댓글 관련 HTTP 요청을 처리하는 서블릿 클래스입니다.
 * 이 클래스는 댓글 작성, 수정, 삭제 등의 요청을 Command 패턴을 통해 처리합니다.
 */
@WebServlet("/comment.do")
public class CommentController extends HttpServlet {

    /**
     * HTTP POST 요청을 처리합니다.
     * 댓글 관련 작업(생성, 수정, 삭제 등)을 위한 POST 요청을 Command 객체를 통해 처리합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * HTTP 요청을 처리합니다.
     * 요청을 CommandFactory에 전달하여 적절한 Command 객체를 받고, 해당 Command의 execute 메소드를 호출합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getCommand(request);
        command.execute(request, response);
    }
}
