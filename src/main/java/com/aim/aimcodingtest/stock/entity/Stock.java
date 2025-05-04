package com.aim.aimcodingtest.stock.entity;

import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.stock.dto.request.StockCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock")
public class Stock extends BaseEntity {

    @Column(name = "stock_code", length = 20, nullable = false, unique = true)
    private String code;

    @Column(name = "stock_name", length = 100, nullable = false)
    private String name;

    @Column(name = "stock_price", nullable = false)
    private Long price;

    public static Stock of(StockCreateRequest request) {
        Stock stock = new Stock();
        stock.setCode(request.getCode());
        stock.setName(request.getName());
        stock.setPrice(request.getPrice());
        return stock;
    }

}