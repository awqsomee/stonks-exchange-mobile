package com.example.stonksexchange.api;

import com.example.stonksexchange.api.services.AuthApiService;
import com.example.stonksexchange.api.services.BalanceApiService;
import com.example.stonksexchange.api.services.ForexApiService;
import com.example.stonksexchange.api.services.StockService;

public interface ApiService extends com.example.stonksexchange.api.services.AuthApiService, BalanceApiService, ForexApiService, StockService {

    ApiService ApiService = ApiManager.getApiService();
    ApiService AuthApiService = ApiManager.getAuthApiService();
}