package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.stock.FindStocksResponse;
import com.example.stonksexchange.api.domain.user.GetUserDataResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApiService {
    @GET("auth/user")
    Call<GetUserDataResponse> getUserData();

    @DELETE("auth/user/")
    Call<GetUserDataResponse> deleteUser();
}
