package com.example.stocktrading.response.transactions;


import java.util.List;

public class TransactionResponse {
    private List<TransactionUser> transactions;

    public List<TransactionUser> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionUser> transactions) {
        this.transactions = transactions;
    }
}
