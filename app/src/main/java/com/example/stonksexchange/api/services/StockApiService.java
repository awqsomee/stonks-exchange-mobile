package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.stock.FindStocksResponse;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.api.domain.stock.GetUserStocksResponse;
import com.example.stonksexchange.api.domain.stock.StockExchangeRequest;
import com.example.stonksexchange.api.domain.stock.StockExchangeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StockApiService {
    @GET("stocks/search")
    Call<FindStocksResponse> findStock(@Query("q") String q);

    @GET("stocks")
    Call<GetStockDataResponse> getStockData(@Query("symbol") String symbol, @Query("from") String from, @Query("till") String till);

    @GET("stocks/auth")
    Call<GetUserStocksResponse> getUserStocks();

    @POST("stocks/auth")
    Call<StockExchangeResponse> exchangeStock(@Body StockExchangeRequest request);
}
