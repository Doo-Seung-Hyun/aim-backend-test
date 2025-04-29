package com.aim.aimcodingtest.common.dto.response;

import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.common.exception.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    int httpStatusCode;
    String httpStatusMessage;
    String errorCode;
    String message;
    String path;
    List<ValidationError> validationErrors;

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .httpStatusCode(errorCode.getHttpStatus().value())
                .httpStatusMessage(errorCode.getHttpStatus().getReasonPhrase())
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }

    public static ErrorResponse ofValidation(ErrorCode errorCode, String path, List<ValidationError> validationErrors) {
        return ErrorResponse.builder()
                .httpStatusCode(errorCode.getHttpStatus().value())
                .httpStatusMessage(errorCode.getHttpStatus().getReasonPhrase())
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .path(path)
                .validationErrors(validationErrors)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
