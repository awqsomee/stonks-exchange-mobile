package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.UserStock;

import java.util.ArrayList;

public class GetUserStocksResponse {
    ArrayList<UserStock> stocks;
    String message;

    public GetUserStocksResponse(ArrayList<UserStock> stocks, String message) {
        this.stocks = stocks;
        this.message = message;
    }

    public ArrayList<UserStock> getStocks() {
        return stocks;
    }

    public String getMessage() {
        return message;
    }
}
