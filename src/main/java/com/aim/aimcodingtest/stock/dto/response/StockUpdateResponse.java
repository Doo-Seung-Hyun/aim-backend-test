package com.aim.aimcodingtest.stock.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StockUpdateResponse {
    private String code;
    private String name;
    private Long price;

    private boolean isUpdated;
    private LocalDateTime updatedAt;
}
