package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ForexApiService {
    @GET("forex/auth")
    Call<GetUserCurrenciesResponse> getUserCurrencies();
}
