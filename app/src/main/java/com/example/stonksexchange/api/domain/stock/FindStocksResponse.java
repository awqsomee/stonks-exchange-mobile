package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class FindStocksResponse {
    ArrayList<Stock> stock;
    String message;

    public FindStocksResponse() {
    }

    public FindStocksResponse(ArrayList<Stock> stock, String message) {
        this.stock = stock;
        this.message = message;
    }

    public ArrayList<Stock> getStocks() {
        return stock;
    }
}
