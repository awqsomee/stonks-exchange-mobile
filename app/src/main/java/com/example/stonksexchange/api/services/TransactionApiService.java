package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;
import com.example.stonksexchange.api.domain.transactions.GetTransactionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TransactionApiService {
    @GET("auth/transactions")
    Call<GetTransactionsResponse> getTransactions();
}
