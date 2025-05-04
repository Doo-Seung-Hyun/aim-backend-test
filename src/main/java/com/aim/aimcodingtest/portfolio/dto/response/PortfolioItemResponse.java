package com.aim.aimcodingtest.portfolio.dto.response;

import com.aim.aimcodingtest.portfolio.entity.PortfolioItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Builder
public class PortfolioItemResponse implements Serializable {
    private String stockCode;
    private String stockName;
    private int quantity;
    private Long stockPrice;
    private Long investmentAmount;

    public static PortfolioItemResponse of(PortfolioItem portfolioItem) {
        return PortfolioItemResponse.builder()
                .stockCode(portfolioItem.getStock().getCode())
                .stockName(portfolioItem.getStock().getName())
                .quantity(portfolioItem.getQuantity())
                .stockPrice(portfolioItem.getStockPrice())
                .investmentAmount(portfolioItem.getStockPrice() * portfolioItem.getQuantity())
                .build();
    }
}