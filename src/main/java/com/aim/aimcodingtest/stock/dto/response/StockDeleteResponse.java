package com.aim.aimcodingtest.stock.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StockDeleteResponse {
    private String code;
    private String name;
    private LocalDateTime deletedAt;
}
