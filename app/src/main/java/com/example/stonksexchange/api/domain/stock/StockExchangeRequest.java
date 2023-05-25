package com.example.stonksexchange.api.domain.stock;

public class StockExchangeRequest {
    Number amount;
    String symbol;

    public StockExchangeRequest() {
    }

    public StockExchangeRequest(Number amount, String symbol) {
        this.amount = amount;
        this.symbol = symbol;
    }
}
