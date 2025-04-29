package com.aim.aimcodingtest.common.exception;

import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.common.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> errorResponse(ApiException apiException, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(apiException.getErrorCode(), request.getRequestURI());
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse);

        return apiResponse.toResponseEntity();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> notValidDtoException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<ErrorResponse.ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                ErrorCode.INVALID_INPUT,
                request.getRequestURI(),
                validationErrors
        );
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse);

        return apiResponse.toResponseEntity();
    }
}
