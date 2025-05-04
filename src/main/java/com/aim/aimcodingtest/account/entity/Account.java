package com.aim.aimcodingtest.account.entity;

import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountNumber", "accountName"})
})
public class Account extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, unique = true, nullable = false)
    private String accountNumber;

    @Column(length = 100, nullable = true)
    private String accountName;

    private Long balance;

    private LocalDateTime lastTransactionTime;

    //입금
    public void deposit(long amount, LocalDateTime transactionTime) {
        if(amount <= 0)
            throw new ApiException(ErrorCode.INVALID_TRANSACTION_AMOUNT);
        this.balance += amount;
        this.lastTransactionTime = transactionTime;
    }

    //출금
    public void withdraw(long amount, LocalDateTime transactionTime) {
        if(amount <= 0)
            throw new ApiException(ErrorCode.INVALID_TRANSACTION_AMOUNT);
        if(amount > balance)
            throw new ApiException(ErrorCode.INSUFFICIENT_BALANCE);
        this.balance -= amount;
        this.lastTransactionTime = transactionTime;
    }
}