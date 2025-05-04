package com.aim.aimcodingtest.portfolio.entity;

import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.stock.entity.Stock;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "portfolio_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PortfolioItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_advice_id", nullable = false)
    private PortfolioAdvice portfolioAdvice;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    private int quantity;

    private Long stockPrice;
}