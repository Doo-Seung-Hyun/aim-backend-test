package com.aim.aimcodingtest.account.controller;

import com.aim.aimcodingtest.account.dto.request.DepositRequest;
import com.aim.aimcodingtest.account.dto.request.WithdrawRequest;
import com.aim.aimcodingtest.account.dto.response.AccountResponse;
import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.dto.response.WithdrawResponse;
import com.aim.aimcodingtest.account.service.AccountServiceImpl;
import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;

    /**
     * 계좌 찾기 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountInfo(@PathVariable long id) {
        ApiResponse<AccountResponse> response= ApiResponse.success(
                accountService.findAccount(id)
        );
        return response.toResponseEntity();
    }

    /**
     * 내 계좌 불러오기 API
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getMyAccounts(
            @AuthenticationPrincipal User user,
            @PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable) {
        ApiResponse<Page<AccountResponse>> response= ApiResponse.success(
                accountService.getAllAccountsByUsername(user.getUsername(), pageable)
        );
        return response.toResponseEntity();
    }

    /**
     * 입금 API
     */
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositResponse>> deposit(@RequestBody DepositRequest depositRequest) {
        ApiResponse<DepositResponse> response= ApiResponse.success(
                accountService.deposit(depositRequest.getAccountNumber(), depositRequest.getAmount())
        );
        return response.toResponseEntity();
    }

    /**
     * 출금 API
     */
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponse>> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        ApiResponse<WithdrawResponse> response= ApiResponse.success(
                accountService.withdraw(withdrawRequest.getAccountNumber(), withdrawRequest.getAmount())
        );
        return response.toResponseEntity();
    }
}
