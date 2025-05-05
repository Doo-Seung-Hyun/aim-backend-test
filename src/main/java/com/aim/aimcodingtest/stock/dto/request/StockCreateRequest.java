package com.aim.aimcodingtest.stock.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockCreateRequest {
    @NotBlank(message = "증권코드는 필수 입력 값입니다")
    @Size(max = 20, message = "증권코드의 길이는 최대 20자입니다")
    private String code;

    @NotBlank(message = "증권명은 필수 입력 값입니다")
    @Size(max = 100, message = "증권명의 길이는 최대 100자입니다")
    private String name;

    @Min(value = 1, message = "증권가격은 최소 1원 이상이어야 합니다")
    private Long price;
}
