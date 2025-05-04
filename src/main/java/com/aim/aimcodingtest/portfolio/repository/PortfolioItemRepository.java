package com.aim.aimcodingtest.portfolio.repository;

import com.aim.aimcodingtest.portfolio.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {
    @Query("select pi from PortfolioItem pi join fetch pi.stock s " +
            "where pi.portfolioAdvice.id = :id")
    List<PortfolioItem> findByPortfolioAdviceId(@Param("id") Long id);
}
