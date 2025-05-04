package com.aim.aimcodingtest.portfolio.entity;

import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.portfolio.enums.PortfolioType;
import com.aim.aimcodingtest.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolio_advice")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioAdvice extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PortfolioType portfolioType;

    private Long balance;

    @OneToMany(mappedBy = "portfolioAdvice", cascade = CascadeType.ALL)
    private List<PortfolioItem> portfolioItems = new ArrayList<>();

    public static PortfolioAdvice create(User user, String accountNumber, PortfolioType portfolioType, Long balance) {
        PortfolioAdvice advice = new PortfolioAdvice();
        advice.setUser(user);
        advice.setAccountNumber(accountNumber);
        advice.setPortfolioType(portfolioType);
        advice.setBalance(balance);
        return advice;
    }

    public void addPortfolioItem(PortfolioItem portfolioItem) {
        this.portfolioItems.add(portfolioItem);
        portfolioItem.setPortfolioAdvice(this);
    }
}
