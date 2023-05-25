package com.example.stonksexchange.models;

public class Price {
    String date;
    Number open;
    Number close;
    Number low;
    Number high;

    public Price() {
    }

    public Price(String date, Number open, Number close, Number low, Number high) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
    }

    public Float getClose() {
        return close.floatValue();
    }
}
