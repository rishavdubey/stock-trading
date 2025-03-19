package com.example.stocktrading.service;

import com.example.stocktrading.response.portofolio.PortfolioResponse;
import com.example.stocktrading.response.portofolio.StockPortfolio;
import com.example.stocktrading.entity.Portfolio;
import com.example.stocktrading.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService;

    public Optional<Portfolio> getPortfolioByUserIdAndStockId(String username, String stockSymbol) {
        return portfolioRepository.findByUserIdAndStockId(username, stockSymbol);
    }

    public void updatePortfolio(Portfolio portfolio) {
        portfolioRepository.save(portfolio);
    }

    public PortfolioResponse getPortfoliosByUserId(String userName) {
        List<Portfolio> portfolios=portfolioRepository.findAllByUserId(userName);
        PortfolioResponse portfolioResponse=new PortfolioResponse();
        List<StockPortfolio> stockList=new ArrayList<>();

        BigDecimal pnl = BigDecimal.ZERO;

        for (Portfolio portfolio : portfolios) {
            BigDecimal currentPrice=stockService.getStockPrice(portfolio.getStockId());
            BigDecimal netPnlPerStockPortfolio = currentPrice.subtract(portfolio.getBuyPrice()).multiply(BigDecimal.valueOf(portfolio.getQuantity()));
            pnl = pnl.add(netPnlPerStockPortfolio);

            StockPortfolio stockPortfolio=new StockPortfolio();

            stockPortfolio.setStockSymbol(portfolio.getStockId());
            stockPortfolio.setBuyPriceLatest(portfolio.getBuyPrice());
            stockPortfolio.setQuantity(portfolio.getQuantity());
            stockPortfolio.setCurrentPrice(currentPrice);
            stockPortfolio.setBuyPriceAndQuantity(portfolio.getBuyPriceAndQuantity());

            stockList.add(stockPortfolio);

        }
        portfolioResponse.setRealizePnL(pnl);
        portfolioResponse.setStocks(stockList);

        return portfolioResponse;


    }

    public void removePortfolio(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);
    }
}
