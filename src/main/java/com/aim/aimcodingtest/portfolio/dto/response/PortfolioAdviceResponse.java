package com.aim.aimcodingtest.portfolio.dto.response;

import com.aim.aimcodingtest.portfolio.entity.PortfolioAdvice;
import com.aim.aimcodingtest.portfolio.entity.PortfolioItem;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PortfolioAdviceResponse {
    private String accountNumber;
    private Long balance;
    private String portfolioType;
    private Long maxAllowableInvestment;
    private Long totalInvestmentAmount;
    private List<PortfolioItemResponse> portfolioItems;

    public static PortfolioAdviceResponse of(PortfolioAdvice portfolioAdvice) {
        PortfolioAdviceResponse response = PortfolioAdviceResponse.builder()
                .accountNumber(portfolioAdvice.getAccountNumber())
                .balance(portfolioAdvice.getBalance())
                .portfolioType(portfolioAdvice.getPortfolioType().name())
                .maxAllowableInvestment( (long)(portfolioAdvice.getBalance() * portfolioAdvice.getPortfolioType().getMaxRatio()))
                .totalInvestmentAmount(0L)
                .portfolioItems(new ArrayList<>())
                .build();

        for(PortfolioItem item : portfolioAdvice.getPortfolioItems()){
            PortfolioItemResponse itemResponse = PortfolioItemResponse.of(item);
            response.totalInvestmentAmount += itemResponse.getInvestmentAmount();
            response.portfolioItems.add(itemResponse);
        }

        return response;
    }
}
