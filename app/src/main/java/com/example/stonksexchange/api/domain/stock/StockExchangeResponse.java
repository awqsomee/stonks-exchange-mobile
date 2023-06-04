package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;
import com.example.stonksexchange.models.UserStock;

public class StockExchangeResponse {
    UserStock stock;
    Transaction transaction;
    User user;
    String message;

    public UserStock getStock() {
        return stock;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
