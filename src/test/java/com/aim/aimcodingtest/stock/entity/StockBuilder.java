package com.aim.aimcodingtest.stock.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class StockBuilder extends Stock {
    private String code = "005930";
    private String name = "삼성전자";
    private Long price = 50000L;

    public static StockBuilder builder() {
        return new StockBuilder();
    }
    public Stock build() {
        Stock stock = new Stock();
        stock.setCode(code);
        stock.setName(name);
        stock.setPrice(price);
        return stock;
    }
}