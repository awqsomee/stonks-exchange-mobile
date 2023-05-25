package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;

public class StockExchangeResponse {
    Stock stock;
    Transaction transaction;
    User user;
    String message;

    public StockExchangeResponse() {
    }

    public StockExchangeResponse(Stock stock, Transaction transaction, User user, String message) {
        this.stock = stock;
        this.transaction = transaction;
        this.user = user;
        this.message = message;
    }
}
