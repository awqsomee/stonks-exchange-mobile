package com.example.stonksexchange.api;

import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.auth.LoginRequest;
import com.example.stonksexchange.api.domain.auth.SignUpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Call<AuthResponse> logIn(@Body LoginRequest request);

    @POST("auth/registration")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @GET("auth")
    Call<AuthResponse> auth(@Header("Authorization") String AuthHeader);
}
