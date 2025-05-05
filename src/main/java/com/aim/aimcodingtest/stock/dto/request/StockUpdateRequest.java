package com.aim.aimcodingtest.stock.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockUpdateRequest {
    private String code;

    private String name;

    private Long price;
}
