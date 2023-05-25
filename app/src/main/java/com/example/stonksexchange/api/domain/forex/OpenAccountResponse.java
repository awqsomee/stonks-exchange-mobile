package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.Transaction;

public class OpenAccountResponse {
    Currency currency;
    Transaction transaction;
    String message;

    public OpenAccountResponse() {
    }

    public OpenAccountResponse(Currency currency, Transaction transaction, String message) {
        this.currency = currency;
        this.transaction = transaction;
        this.message = message;
    }
}
