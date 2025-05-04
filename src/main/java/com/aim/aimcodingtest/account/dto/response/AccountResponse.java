package com.aim.aimcodingtest.account.dto.response;

import com.aim.aimcodingtest.account.entity.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AccountResponse {
    private long id;
    private String accountNumber;
    private String accountName;
    private String username;
    private long balance;
    private LocalDateTime createdAt;

    public static AccountResponse fromAccount(Account account) {
        return new AccountResponse(account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getUser().getUsername(),
                account.getBalance(),
                account.getCreatedAt());
    }
}
