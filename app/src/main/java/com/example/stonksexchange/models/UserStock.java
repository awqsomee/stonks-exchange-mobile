package com.example.stonksexchange.models;

import java.util.List;

public class UserStock extends Stock{
    Number latestPrice;

    public Number getLatestPrice() {
        return latestPrice;
    }

    public String getLatestPriceString() {
        return String.format("%.2f", latestPrice.floatValue());
    }
}
