package com.example.stocktrading.request.stock;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class StockRequest {

    @NotBlank(message = "Stock symbol is required")
    private String symbol;

    @NotBlank(message = "Stock name is required")
    private String name;

    @NotNull(message = "Listed price is required")
    @DecimalMin(value = "0.01", message = "Listed price must be greater than 0")
    private BigDecimal listedPrice;

    // Getters and Setters

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getListedPrice() {
        return listedPrice;
    }

    public void setListedPrice(BigDecimal listedPrice) {
        this.listedPrice = listedPrice;
    }
}
