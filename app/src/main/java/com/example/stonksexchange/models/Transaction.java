package com.example.stonksexchange.models;

public class Transaction {
    String symbol;
    Number amount;
    String type;
    String date;
    String currency;
    Number cost;

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getCurrency() {
        return currency;
    }

    public Float getCost() {
        if (cost != null) return cost.floatValue();
        else return 0f;
    }

    public Number getAmount() {
        return amount;
    }

    public String getSymbol() {
        return symbol;
    }
}
