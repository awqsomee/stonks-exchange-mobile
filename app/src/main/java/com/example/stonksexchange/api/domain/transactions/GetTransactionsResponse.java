package com.example.stonksexchange.api.domain.transactions;

import com.example.stonksexchange.models.Transaction;

import java.util.ArrayList;

public class GetTransactionsResponse {
    String message;
    ArrayList<Transaction> transactions;

    public String getMessage() {
        return message;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
