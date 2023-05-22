package com.example.stonksexchange.api.domain.balance;

import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;

public class ChangeBalanceResponse {
    User user;
    Transaction transaction;
    String message;

    public ChangeBalanceResponse() {
    }

    public ChangeBalanceResponse(User user, Transaction transaction, String message) {
        this.transaction = transaction;
        this.user = user;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
