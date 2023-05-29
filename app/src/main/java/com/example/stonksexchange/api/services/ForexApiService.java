package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.forex.CloseAccountResponse;
import com.example.stonksexchange.api.domain.forex.CurrencyExchangeRequest;
import com.example.stonksexchange.api.domain.forex.CurrencyExchangeResponse;
import com.example.stonksexchange.api.domain.forex.GetCurrenciesResponse;
import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;
import com.example.stonksexchange.api.domain.forex.OpenAccountResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ForexApiService {
    @GET("forex/auth")
    Call<GetUserCurrenciesResponse> getUserCurrencies();

    @GET("forex")
    Call<GetCurrenciesResponse> getCurrencies();

    @POST("forex/auth/{symbol}/open")
    Call<OpenAccountResponse> openAccount(@Path("symbol") String symbol);

    @POST("forex/auth/{symbol}/close")
    Call<CloseAccountResponse> closeAccount(@Path("symbol") String symbol);

    @POST("forex/auth")
    Call<CurrencyExchangeResponse> exchangeCurrency(@Body CurrencyExchangeRequest request);

}
