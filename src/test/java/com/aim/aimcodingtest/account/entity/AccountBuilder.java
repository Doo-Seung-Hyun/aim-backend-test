package com.aim.aimcodingtest.account.entity;

import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.entity.UserBuilder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountBuilder extends Account{
    private Long id = 1L;
    private User user = UserBuilder.builder().build();
    private String accountNumber = "123456789";
    private String accountName = "test-account";
    private Long balance = 10000L;
    private LocalDateTime lastTransactionTime = LocalDateTime.now();

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }
    public AccountBuilder id(long id) {
        this.id = id;
        return this;
    }
    public AccountBuilder balance(Long balance) {
        this.balance = balance;
        return this;
    }
    public AccountBuilder accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
    public Account build() {
        Account account = new Account();
        try {
            account.setId(id);
            setField(account, "user", user);
            setField(account, "accountNumber", accountNumber);
            setField(account, "accountName", accountName);
            setField(account, "balance", balance);
            setField(account, "lastTransactionTime", lastTransactionTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return account;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Account.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}