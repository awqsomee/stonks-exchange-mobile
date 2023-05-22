package com.example.stonksexchange.api;

import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface BalanceApiService {
    @PATCH("auth/balance")
    Call<AuthResponse> auth(@Header("Authorization") String AuthHeader, @Body ChangeBalanceRequest request);
}
