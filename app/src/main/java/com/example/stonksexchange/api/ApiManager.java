package com.example.stonksexchange.api;

import com.example.stonksexchange.api.services.AuthApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String BASE_URL = "https://stonks-kaivr.amvera.io/api/";
//    private static final String BASE_URL = "http://192.168.0.47/api/";

    static Retrofit retrofit;
    static String authToken;
    private static Retrofit retrofitAuth;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getAuthenticatedRetrofitInstance(String authToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(authToken));
        retrofitAuth = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofitAuth;
    }

    public static ApiService getApiService() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(ApiService.class);
    }

    public static ApiService getAuthApiService() {
        Retrofit retrofitAuth = getAuthenticatedRetrofitInstance(authToken);
        return retrofitAuth.create(ApiService.class);
    }

    public static void setToken(String token) {
        authToken = token;
    }
}