package com.aim.aimcodingtest.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

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
        return new ApiResponse<>(true, HttpStatus.OK.value(), LocalDateTime.now(), response);
    }
    public static <T> ApiResponse<T> success(HttpStatus httpStatus,     T response) {
        return new ApiResponse<>(true, httpStatus.value(), LocalDateTime.now(), response);
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
