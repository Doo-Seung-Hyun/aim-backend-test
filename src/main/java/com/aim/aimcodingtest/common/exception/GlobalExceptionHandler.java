package com.aim.aimcodingtest.common.exception;

import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.common.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> errorResponse(ApiException apiException, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(apiException.getErrorCode(), request.getRequestURI());
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse);

        return apiResponse.toResponseEntity();
    }
}
