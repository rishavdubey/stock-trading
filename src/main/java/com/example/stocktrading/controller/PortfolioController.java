package com.example.stocktrading.controller;

import com.example.stocktrading.common.CommonValidation;
import com.example.stocktrading.response.portofolio.PortfolioResponse;
import com.example.stocktrading.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private CommonValidation commonValidation;

    @GetMapping
    public ResponseEntity<?> userPortfolio(@CookieValue(name = "JWT_TOKEN", required = false) String token){
        String userName=commonValidation.validateToken(token);
        PortfolioResponse portfolios = portfolioService.getPortfoliosByUserId(userName);
        if (portfolios==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No portfolios found for user");
        }
        return ResponseEntity.status(HttpStatus.OK).body(portfolios);

    }
}
