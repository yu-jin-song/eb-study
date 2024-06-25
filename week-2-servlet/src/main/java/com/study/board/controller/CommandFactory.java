package com.study.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 게시판 애플리케이션에서 HTTP 요청에 대응하는 Command 객체를 관리하고 반환하는 팩토리 클래스입니다.
 * 이 클래스는 요청 URL에 따라 적절한 Command 객체를 찾아 반환합니다.
 */
public class CommandFactory {

    private static Map<String, Command> commandMap = new HashMap<>();

    static {
        // // Board 관련 Command 등록
        commandMap.put("/list.do", new BoardListCommand());
        commandMap.put("/view.do", new BoardViewCommand());
        commandMap.put("/writeForm.do", new BoardWriteFormCommand());
        commandMap.put("/writeProcess.do", new BoardWriteProcessCommand());
        commandMap.put("/modifyForm.do", new BoardModifyFormCommand());
        commandMap.put("/modifyProcess.do", new BoardModifyProcessCommand());

        // Category 관련 Command 등록
        commandMap.put("/category.do", new CategoryCommand());

        // File 관련 Command 등록
        commandMap.put("/file.do", new FileDownloadCommand());
    }

    /**
     * 요청된 URL에 해당하는 Command 객체를 반환합니다.
     * 만약 해당 URL에 대응하는 Command가 없으면 기본 Command 객체를 반환합니다.
     *
     * @param request 클라이언트로부터 받은 HttpServletRequest 객체
     * @return 요청된 URL에 해당하는 Command 객체. 해당하는 객체가 없으면 DefaultCommand 객체를 반환
     */
    public static Command getCommand(HttpServletRequest request) {
        String action = request.getServletPath();
        return  commandMap.getOrDefault(action, new DefaultCommand());
    }
}
