package com.example.stocktrading.repository;

import com.example.stocktrading.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserIdAndStockId(String userId, String stockId);
    List<Portfolio> findAllByUserId(String userId);
}