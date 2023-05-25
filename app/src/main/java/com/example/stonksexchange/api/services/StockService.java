package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockService {
    @GET("stocks/search")
    Call<GetStockDataResponse> getStock(@Query("q") String symbol);
}
