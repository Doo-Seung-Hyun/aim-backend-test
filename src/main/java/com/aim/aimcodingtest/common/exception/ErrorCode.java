package com.aim.aimcodingtest.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다"),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "사용자 ID가 중복됩니다"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다"),
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자의 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
