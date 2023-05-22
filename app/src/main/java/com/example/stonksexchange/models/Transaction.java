package com.example.stonksexchange.models;

public class Transaction {
    String id;
    String type;
    String date;
    String currency;
    Number cost;
    String userId;
    Number balance;
    String message;

    public Transaction() {
    }

    public Transaction(String id, String type, String date, String currency, Number cost, String userId, Number balance, String message) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.currency = currency;
        this.cost = cost;
        this.userId = userId;
        this.balance = balance;
        this.message = message;
    }
}
