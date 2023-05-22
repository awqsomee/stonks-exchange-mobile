package com.example.stonksexchange.api;

import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;

public interface BalanceApiService {

    @PUT("auth/balance")
    Call<ChangeBalanceResponse> changeBalance(@Header("Authorization") String AuthHeader, @Body ChangeBalanceRequest request);
}
