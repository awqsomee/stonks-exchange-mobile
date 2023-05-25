package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Stock;

public class GetStockDataResponse {
    Stock stock;
    String message;

    public GetStockDataResponse() {
    }

    public GetStockDataResponse(Stock stock, String message) {
        this.stock = stock;
        this.message = message;
    }

    public Stock getStock() {
        return stock;
    }
}
