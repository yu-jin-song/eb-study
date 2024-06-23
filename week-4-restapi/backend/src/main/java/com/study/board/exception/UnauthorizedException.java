package com.study.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 권한이 없을 때 발생하는 예외
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    /**
     * 주어진 메시지로 UnauthorizedException을 생성
     *
     * @param message 예외 메시지
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
