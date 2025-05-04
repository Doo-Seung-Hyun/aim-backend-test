package com.aim.aimcodingtest.portfolio.controller;

import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.portfolio.dto.request.PortfolioAdviceRequest;
import com.aim.aimcodingtest.portfolio.dto.response.PortfolioAdviceResponse;
import com.aim.aimcodingtest.portfolio.service.PortfolioService;
import com.aim.aimcodingtest.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    @PostMapping("/advice")
    public ResponseEntity<ApiResponse<PortfolioAdviceResponse>> requestAdvice(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PortfolioAdviceRequest request) {
        ApiResponse<PortfolioAdviceResponse> response = ApiResponse.success(
            portfolioService.createAdvice(user, request.getAccountNumber(), request.getPortfolioType())
        );
        return response.toResponseEntity();
    }
}
