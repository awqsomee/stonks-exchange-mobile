package com.example.stonksexchange.api;

import com.example.stonksexchange.api.domain.AuthRequest;
import com.example.stonksexchange.api.domain.AuthResponse;
import com.example.stonksexchange.api.domain.LoginRequest;
import com.example.stonksexchange.api.domain.SignUpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<AuthResponse> logIn(@Body LoginRequest request);

    @POST("auth/registration")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @GET("auth")
    Call<AuthResponse> auth(@Header("Authorization") String AuthHeader);

    ApiService apiService = ApiManager.getApiService();
}
