package com.aim.aimcodingtest.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "사용자 ID가 중복됩니다");

    private final HttpStatus httpStatus;
    private final String message;
}
