package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;

public class CloseAccountResponse {
    Currency currency;
    Transaction transactionClose;
    Transaction transactionExchange;
    User user;
    String message;

    public CloseAccountResponse(Currency currency, Transaction transactionClose, Transaction transactionExchange, User user, String message) {
        this.currency = currency;
        this.transactionClose = transactionClose;
        this.transactionExchange = transactionExchange;
        this.user = user;
        this.message = message;
    }

    public CloseAccountResponse() {
    }
}
