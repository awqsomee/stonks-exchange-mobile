package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Price;
import com.example.stonksexchange.models.Stock;

import java.util.List;

public class GetStockDataResponse {
    List<Stock> stock;
    String message;

    public GetStockDataResponse() {
    }

    public GetStockDataResponse(List<Stock> stock, String message) {
        this.stock = stock;
        this.message = message;
    }

    public GetStockDataResponse(List<Stock> stock) {
        this.stock = stock;
    }

    public Stock GetFirstStock() {
        return stock.get(0);
    }
}
