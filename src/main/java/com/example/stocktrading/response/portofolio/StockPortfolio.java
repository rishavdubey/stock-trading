package com.example.stocktrading.response.portofolio;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StockPortfolio {
    private String stockSymbol;
    private int quantity;
    private BigDecimal buyPriceLatest;
    private BigDecimal currentPrice;
    private Map<BigDecimal, Integer> buyPriceAndQuantity = new HashMap<>();

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBuyPriceLatest() {
        return buyPriceLatest;
    }

    public void setBuyPriceLatest(BigDecimal buyPriceLatest) {
        this.buyPriceLatest = buyPriceLatest;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Map<BigDecimal, Integer> getBuyPriceAndQuantity() {
        return buyPriceAndQuantity;
    }

    public void setBuyPriceAndQuantity(Map<BigDecimal, Integer> buyPriceAndQuantity) {
        this.buyPriceAndQuantity = buyPriceAndQuantity;
    }
}
