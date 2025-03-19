package com.example.stocktrading.service;

import com.example.stocktrading.response.transactions.TransactionResponse;
import com.example.stocktrading.response.transactions.TransactionUser;
import com.example.stocktrading.entity.Transaction;
import com.example.stocktrading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public TransactionResponse getAllTransactions(String userName) {
        List<Transaction> transactions= transactionRepository.findByUserId(userName);
        TransactionResponse transactionResponse=new TransactionResponse();

        List<TransactionUser> transactionUserList=new ArrayList<>();

        for(Transaction transaction:transactions){
            TransactionUser transactionUser=new TransactionUser();
            transactionUser.setStockSymbol(transaction.getStockId());
            transactionUser.setPrice(transaction.getPrice());
            transactionUser.setQuantity(transaction.getQuantity());
            transactionUser.setType(transaction.getType());
            transactionUser.setCreatedAt(transaction.getCreatedAt());

            transactionUserList.add(transactionUser);

        }
        transactionResponse.setTransactions(transactionUserList);
        return transactionResponse;
    }
}
