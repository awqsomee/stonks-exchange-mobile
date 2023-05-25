package com.example.stonksexchange.models;

public class CurrencyShort {
    private String name;
    private double value;
    private double previous;

    public CurrencyShort() {
    }

    public CurrencyShort(String name, double value, double previous) {
        this.name = name;
        this.value = value;
        this.previous = previous;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public double getPrevious() {
        return previous;
    }
}
