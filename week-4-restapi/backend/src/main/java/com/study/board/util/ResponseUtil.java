package com.study.board.util;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Builder
@Slf4j
public class ResponseUtil {
    public static ResponseEntity<?> getResponseEntity(
            Map<String, Object> response
    ) {
        ResponseEntity.BodyBuilder responseBuilder =
                ResponseEntity.status(HttpStatus.OK);

        if (response.containsKey("status")) {
            HttpStatus httpStatus = (HttpStatus) response.get("status");
            response.remove("status");
            responseBuilder = ResponseEntity.status(httpStatus);
        }

        if (response.containsKey("headers")) {
            HttpHeaders httpHeaders = (HttpHeaders) response.get("headers");
            response.remove("headers");
            responseBuilder.headers(httpHeaders);
        }

        if (!response.isEmpty()) {
            return responseBuilder.body(response);
        }

        return responseBuilder.build();
    }
}
