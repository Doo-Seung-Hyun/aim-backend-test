package com.aim.aimcodingtest.portfolio.dto.request;

import com.aim.aimcodingtest.portfolio.enums.PortfolioType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfolioAdviceRequest {
    @NotBlank(message = "계좌번호는 필수 입력 값입니다")
    private String accountNumber;

    @NotNull(message = "포트폴리오 유형은 필수 입력 값입니다")
    @Enumerated(value = EnumType.STRING)
    private PortfolioType portfolioType;
}
