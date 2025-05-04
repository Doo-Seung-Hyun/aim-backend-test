package com.aim.aimcodingtest.portfolio.repository;

import com.aim.aimcodingtest.portfolio.entity.PortfolioAdvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioAdviceRepository extends JpaRepository<PortfolioAdvice, Long> {
}
