package com.aim.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private int httpStatusCode;
    private LocalDateTime timestamp;
    private T response;

    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>(false, HttpStatus.OK.value(), LocalDateTime.now(), response);
    }
    public static <T> ApiResponse<ErrorResponse> error(ErrorResponse errorResponse) {
        int HttpStatusCode =  errorResponse.getHttpStatusCode();
        return new ApiResponse<>(false, HttpStatusCode, LocalDateTime.now(), errorResponse);
    }
    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.httpStatusCode)
                .body(this);
    }
}
