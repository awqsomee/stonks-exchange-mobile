package com.example.stonksexchange.api;

import com.example.stonksexchange.api.services.AuthApiService;
import com.example.stonksexchange.api.services.BalanceApiService;

public interface ApiService extends com.example.stonksexchange.api.services.AuthApiService, BalanceApiService {

    ApiService ApiService = ApiManager.getApiService();
    ApiService AuthApiService = ApiManager.getAuthApiService();
}