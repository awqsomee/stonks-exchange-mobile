package com.example.stonksexchange.models;

import java.util.List;

public class UserStock extends Stock{
    Number latestPrice;
    Number amount;

    public Number getLatestPrice() {
        return latestPrice;
    }

    public Number getAmount() {
        return amount;
    }
}
