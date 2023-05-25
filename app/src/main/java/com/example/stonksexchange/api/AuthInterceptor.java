package com.example.stonksexchange.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String authToken;

    public AuthInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + authToken).build();
        return chain.proceed(request);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
