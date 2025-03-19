package com.example.stocktrading.controller;

import com.example.stocktrading.common.CommonValidation;
import com.example.stocktrading.response.trade.StockBuyResponse;
import com.example.stocktrading.response.trade.StockSellResponse;
import com.example.stocktrading.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    @Autowired
    private  TradeService tradeService;
    @Autowired
    private CommonValidation commonValidation;

    @PostMapping("/buy")
    public ResponseEntity<StockBuyResponse> buyStock(@RequestParam("stockSymbol") String stockSymbol, @RequestParam("quantity") int quantity, @CookieValue(name = "JWT_TOKEN", required = false) String token) {
        String username=commonValidation.validateToken(token);
        StockBuyResponse stockBuyResponse =tradeService.buyStock(username, stockSymbol, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(stockBuyResponse);
    }

    @PostMapping("/sell")
    public ResponseEntity<StockSellResponse> sellStock(@RequestParam("stockSymbol") String stockSymbol, @RequestParam("quantity") int quantity, @CookieValue(name = "JWT_TOKEN", required = false) String token) {
        String username=commonValidation.validateToken(token);
        StockSellResponse stockSellResponse =tradeService.sellStock(username, stockSymbol, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(stockSellResponse);
    }
}