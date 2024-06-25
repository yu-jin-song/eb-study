package com.study.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 기본 커맨드 클래스입니다.
 * 이 클래스는 정의되지 않은 커맨드에 대한 요청을 처리합니다.
 * 일반적으로 사용자가 요청한 작업이 시스템에서 지원하지 않거나 존재하지 않는 경우 이 커맨드가 실행됩니다.
 */
public class DefaultCommand implements Command {
    /**
     * 정의되지 않은 커맨드에 대한 요청을 처리합니다.
     * 사용자가 요청한 작업이 존재하지 않을 경우, 404 Not Found 오류를 반환합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Requested action is not available.");
    }
}
