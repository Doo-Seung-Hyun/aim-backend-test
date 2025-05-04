package com.aim.aimcodingtest.account.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
public class DepositResponse {
    private String accountNumber;
    private long balanceAfterDeposit;
    private long depositAmount;
    private String transactionId;
    private LocalDateTime depositTime;

    public DepositResponse complete(String transactionId, LocalDateTime depositTime) {
        this.transactionId = transactionId;
        this.depositTime = depositTime;
        return this;
    }
}
