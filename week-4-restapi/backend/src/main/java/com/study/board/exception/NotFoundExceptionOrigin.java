package com.study.board.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundExceptionOrigin extends RuntimeException {

    private final transient MessageSource messageSource;

    /**
     * 주어진 메시지로 NotFoundException을 생성
     *
     * @param argument 예외 메시지
     */
    public NotFoundExceptionOrigin(
            String argument,
            MessageSource messageSource
    ) {
//        super(messageKey);
//        this.messageKey = messageKey;
//        this.messageSource = messageSource;
//        this.args = args;
//        super(String.format("common.error.notFound", argument));
        super(messageSource.getMessage(
                "common.error.notFound",
                new Object[]{argument},
                LocaleContextHolder.getLocale()
        ));
        this.messageSource = messageSource;
    }

    //    @Override
//    public String getMessage() {
//        return messageSource.getMessage(
//                super.getMessage(),
//                null,
//                LocaleContextHolder.getLocale()
//        );
//    }
}
