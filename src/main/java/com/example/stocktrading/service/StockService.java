package com.example.stocktrading.service;

import com.example.stocktrading.exception.CustomException;
import com.example.stocktrading.repository.StockRepository;
import com.example.stocktrading.request.stock.StockRequest;
import com.example.stocktrading.entity.Stock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final Random random = new Random();

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Scheduled(fixedRate = 1000) // Update prices every 1 seconds
    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            BigDecimal change = BigDecimal.valueOf(random.nextDouble() * 10 - 5); // Random fluctuation (-5 to +5)
            BigDecimal newPrice = stock.getCurrentPrice().add(change);
            if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                newPrice = BigDecimal.valueOf(0.01); // Set to a minimal positive value
            }
            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock);
        }
    }
    @Transactional
    public Stock addStock(StockRequest stockRequest) {
        if (stockRepository.findBySymbol(stockRequest.getSymbol()).isPresent()) {
            throw new CustomException("Stock with symbol " + stockRequest.getSymbol() + " already exists.");
        }
        Stock newStock=new Stock();
        newStock.setSymbol(stockRequest.getSymbol());
        newStock.setName(stockRequest.getName());
        newStock.setListedPrice(stockRequest.getListedPrice());
        newStock.setCurrentPrice(stockRequest.getListedPrice());
        return stockRepository.save(newStock);
    }


    public Stock getStockBySymbol(String stockSymbol) {
        Optional<Stock> stockOptional= stockRepository.findBySymbol(stockSymbol);
        if(stockOptional.isEmpty()){
            throw new CustomException("Stock with symbol "+stockSymbol+" Not Exist!!");

        }
        return stockOptional.get() ;
    }

    public BigDecimal getStockPrice(String stockSymbol) {
        Stock stock=getStockBySymbol(stockSymbol);
        return stock.getCurrentPrice();

    }
}
