package com.aim.aimcodingtest.account.entity;

import com.aim.aimcodingtest.account.enums.ErrorReason;
import com.aim.aimcodingtest.account.enums.TransactionType;
import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.common.exception.ApiException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity {
    @Column(length = 50, unique = true, nullable = false)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(length = 50, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Long balanceBefore;

    @Column(nullable = false)
    private Long transactionAmount;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private Long balanceAfter;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @Setter(AccessLevel.PRIVATE)
    private ErrorReason errorReason;

    public static Transaction createDepositTransaction(String accountNumber, long amount) {
        String transactionId = UUID.randomUUID().toString();
        return Transaction.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .transactionType(TransactionType.DEPOSIT)
                .transactionAmount(amount)
                .transactionTime(LocalDateTime.now())
                .build();
    }

    public Transaction success(Account account) {
        this.account = account;
        this.balanceBefore = account.getBalance() - this.transactionAmount;
        this.balanceAfter = account.getBalance();
        return this;
    }

    public Transaction error(Account account, Exception exception) {
        if(account != null) {
            this.account = account;
            this.balanceBefore = account.getBalance();
            this.balanceAfter = account.getBalance();

        }
        this.setErrorReason(mapToErrorReason(exception));
        return this;
    }

    public static ErrorReason mapToErrorReason(Exception exception) {
        if (exception == null)
            return null;

        ErrorReason errorReason = ErrorReason.ETC_FAILED;
        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            errorReason = switch (apiException.getErrorCode()) {
                case INVALID_ACCOUNT_NUMBER -> ErrorReason.INVALID_ACCOUNT;
                case NOT_AUTHENTICATED -> ErrorReason.NOT_AUTHORIZED;
                case INVALID_TRANSACTION_AMOUNT -> ErrorReason.INVALID_AMOUNT;
                case INSUFFICIENT_BALANCE -> ErrorReason.INSUFFICIENT_BALANCE;
                default -> ErrorReason.ETC_FAILED;
            };
        }
        return errorReason;
    }
}