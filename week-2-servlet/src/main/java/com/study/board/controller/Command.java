package com.study.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 게시판 애플리케이션에서 각종 요청을 처리하는 커맨드의 인터페이스입니다.
 * 이 인터페이스를 구현하는 클래스는 특정 HTTP 요청을 처리하는 로직을 정의합니다.
 */
public interface Command {

    /**
     * HTTP 요청을 처리합니다.
     * 이 메소드는 구체적인 커맨드에 의해 구현되며, 서블릿 요청과 응답 객체를 사용하여 요청을 처리합니다.
     *
     * @param request  서블릿 요청 객체, 클라이언트의 요청 정보
     * @param response 서블릿 응답 객체, 클라이언트에게 전달할 응답 정보
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
