package com.study.board.util;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Request 관련 유효성 검사 클래스
 */
public class RequestUtil {

    public String getParameter(HttpServletRequest request, String parameter, Object value) {
        String result = "";

        if (Integer.class.isInstance(value) || Long.class.isInstance(value)) {

        }
        if(String.class.isInstance(value)) {

        }

        return result;
    }

//    public
}
