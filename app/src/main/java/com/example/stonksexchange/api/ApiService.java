package com.example.stonksexchange.api;

import com.example.stonksexchange.api.services.AuthApiService;
import com.example.stonksexchange.api.services.BalanceApiService;
import com.example.stonksexchange.api.services.ForexApiService;
import com.example.stonksexchange.api.services.StockApiService;
import com.example.stonksexchange.api.services.TransactionApiService;
import com.example.stonksexchange.api.services.UserApiService;

public interface ApiService extends AuthApiService, BalanceApiService, ForexApiService, StockApiService, UserApiService, TransactionApiService {

    ApiService ApiService = ApiManager.getApiService();
    ApiService AuthApiService = ApiManager.getAuthApiService();
}