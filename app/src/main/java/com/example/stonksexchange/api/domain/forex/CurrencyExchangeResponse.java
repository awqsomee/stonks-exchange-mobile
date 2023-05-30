package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;

import java.util.ArrayList;

public class CurrencyExchangeResponse {
    ArrayList<Currency> currencies;
    Currency currency;
    Transaction transaction;
    User user;
    String message;

    public CurrencyExchangeResponse() {
    }

    public CurrencyExchangeResponse(ArrayList<Currency> currencies, Currency currency, Transaction transaction, User user, String message) {
        this.currencies = currencies;
        this.currency = currency;
        this.transaction = transaction;
        this.user = user;
        this.message = message;
    }
}
