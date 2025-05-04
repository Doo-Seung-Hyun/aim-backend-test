package com.aim.aimcodingtest.stock.dto.response;

import com.aim.aimcodingtest.stock.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockInfoResponse {
    private String code;
    private String name;
    private Long price;

    public static StockInfoResponse fromStock(Stock stock) {
        return new StockInfoResponse(stock.getCode(), stock.getName(), stock.getPrice());
    }
}
