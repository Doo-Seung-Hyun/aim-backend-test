package com.aim.aimcodingtest.stock.repository;

import com.aim.aimcodingtest.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    public Optional<Stock> findByCode(String code);
}
