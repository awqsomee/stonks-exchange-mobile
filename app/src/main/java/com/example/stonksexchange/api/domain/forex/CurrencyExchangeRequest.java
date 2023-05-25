package com.example.stonksexchange.api.domain.forex;

public class CurrencyExchangeRequest {
    String symbol;
    Number amount;

    public CurrencyExchangeRequest(String symbol, Number amount) {
        this.symbol = symbol;
        this.amount = amount;
    }
}
