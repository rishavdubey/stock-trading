package com.example.stocktrading.controller;

import com.example.stocktrading.common.CommonValidation;
import com.example.stocktrading.response.transactions.TransactionResponse;
import com.example.stocktrading.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CommonValidation commonValidation;

    @GetMapping
    ResponseEntity<?> getAllTransactions(@CookieValue(name = "JWT_TOKEN", required = false) String token){
        String userName=commonValidation.validateToken(token);
        TransactionResponse transactions=transactionService.getAllTransactions(userName);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

}
