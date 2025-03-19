package com.example.stocktrading.response.portofolio;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioResponse {
    private BigDecimal realizePnL;
    private List<StockPortfolio> stocks;

    public BigDecimal getRealizePnL() {
        return realizePnL;
    }

    public void setRealizePnL(BigDecimal realizePnL) {
        this.realizePnL = realizePnL;
    }

    public List<StockPortfolio> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockPortfolio> stocks) {
        this.stocks = stocks;
    }
}
