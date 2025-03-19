package com.example.stocktrading.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "portfolios")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String stockId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal buyPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ElementCollection
    private Map<BigDecimal, Integer> buyPriceAndQuantity = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Map<BigDecimal, Integer> getBuyPriceAndQuantity() {
        return buyPriceAndQuantity;
    }

    public void setBuyPriceAndQuantity(Map<BigDecimal, Integer> buyPriceAndQuantity) {
        this.buyPriceAndQuantity = buyPriceAndQuantity;
    }
}
