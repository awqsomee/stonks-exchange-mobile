package com.example.stonksexchange.api.domain.stock;

import com.example.stonksexchange.models.Stock;

import java.util.List;

public class FindStocksResponse {
    List<Stock> stock;
    String message;

    public FindStocksResponse() {
    }

    public FindStocksResponse(List<Stock> stock, String message) {
        this.stock = stock;
        this.message = message;
    }

    public FindStocksResponse(List<Stock> stock) {
        this.stock = stock;
    }

}
