package com.study.board.exception;

import com.study.board.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * 애플리케이션의 전역 예외 처리를 제공하는 클래스
 */
@AllArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * NotFoundException 처리
     *
     * @param error   NotFoundException 인스턴스
     * @param request WebRequest 객체
     * @return NOT_FOUND HTTP 상태 코드를 가진 ResponseEntity 반환
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(
            NotFoundException error, WebRequest request
    ) {
        String errorMessage = messageSource.getMessage(
                "common.error.notFound",
                new Object[]{error.getMessage()},
                LocaleContextHolder.getLocale()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND);
//        response.put("message", error.getMessage());
        response.put("message", errorMessage);

        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * UnauthorizedException 처리
     *
     * @param error   UnauthorizedException 인스턴스
     * @param request WebRequest 객체
     * @return UNAUTHORIZED HTTP 상태 코드를 가진 ResponseEntity 반환
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(
            UnauthorizedException error, WebRequest request
    ) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        return ResponseUtil.builder()
//                .status(HttpStatus.UNAUTHORIZED)
//                .build();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED);

        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * BadRequestException 처리
     *
     * @param error   BadRequestException 인스턴스
     * @param request WebRequest 객체
     * @return BAD_REQUEST HTTP 상태 코드를 가진 ResponseEntity 반환
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(
            BadRequestException error, WebRequest request
    ) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        return ResponseUtil.builder()
//                .status(HttpStatus.BAD_REQUEST)
//                .build();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST);

        return ResponseUtil.getResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        BindingResult bindingResult = ex.getBindingResult();

//        Map<String, String> errors = new HashMap<>();
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> {
//            errors.put(
//                    error.getField(),
//                    error.getDefaultMessage()
//            );
            errors.add(error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("errors", errors);

        return ResponseUtil.getResponseEntity(response);
    }


    /**
     * 다른 모든 예외를 처리
     *
     * @param error   예외 인스턴스
     * @param request WebRequest 객체
     * @return INTERNAL_SERVER_ERROR HTTP 상태 코드를 가진 ResponseEntity 반환
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
            Exception error, WebRequest request
    ) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .build();
//        return ResponseUtil.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        String message = messageSource.getMessage(
                "common.error.fail.server",
                null,
                Locale.getDefault()
        );
        response.put("message", message);

        return ResponseUtil.getResponseEntity(response);
    }
}