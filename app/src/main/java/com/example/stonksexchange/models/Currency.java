package com.example.stonksexchange.models;

public class Currency {
    String id;
    String symbol;
    String name;
    Number amount;
    Number latestPrice;
    Number price;
    Number difference;

    public Currency() {
    }

    public Currency(String id, String symbol, String name, Number amount, Number latestPrice, Number price, Number difference) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.amount = amount;
        this.latestPrice = latestPrice;
        this.price = price;
        this.difference = difference;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Number getAmount() {
        return amount;
    }

    public Number getLatestPrice() {
        return latestPrice;
    }

    public Number getPrice() {
        return price;
    }

    public Number getDifference() {
        return difference;
    }
}
