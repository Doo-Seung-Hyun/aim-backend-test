package com.aim.common.dto.response;

import com.aim.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    int httpStatusCode;
    String httpStatusMessage;
    String errorCode;
    String message;
    String path;

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .httpStatusCode(errorCode.getHttpStatus().value())
                .httpStatusMessage(errorCode.getHttpStatus().getReasonPhrase())
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }
}
