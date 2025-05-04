package com.aim.aimcodingtest.account.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositRequest {
    @NotBlank(message = "계좌번호는 필수 입력 값입니다")
    private String accountNumber;

    @Min(value = 1, message = "금액은 최소 1원 이상이어야 합니다")
    private Long amount;
}
