package com.example.stocktrading.controller;

import com.example.stocktrading.common.CommonValidation;
import com.example.stocktrading.request.stock.StockRequest;
import com.example.stocktrading.entity.Stock;
import com.example.stocktrading.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private  StockService stockService;
    @Autowired
    private CommonValidation commonValidation;

    @GetMapping
    public List<Stock> getStocks() {
        return stockService.getAllStocks();
    }
    @PostMapping
    public Stock addStock(@Valid @RequestBody StockRequest stockRequest, @CookieValue(name = "JWT_TOKEN", required = false) String token) {
        commonValidation.validateToken(token);
        return stockService.addStock(stockRequest);
    }
}
