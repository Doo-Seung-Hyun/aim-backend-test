package com.aim.aimcodingtest.account.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WithdrawResponse {
    private String accountNumber;
    private long balanceAfterWithdraw;
    private long withdrawAmount;
    private String transactionId;
    private LocalDateTime withdrawTime;

    public WithdrawResponse complete(String transactionId, LocalDateTime withdrawTime){
        this.transactionId = transactionId;
        this.withdrawTime = withdrawTime;
        return this;
    }
}
