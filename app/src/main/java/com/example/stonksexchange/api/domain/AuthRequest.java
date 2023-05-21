package com.example.stonksexchange.api.domain;

public class AuthRequest {
    String token;

    public AuthRequest(String token) {
        this.token = "Bearer " + token;
    }

    public String getToken() {
        return token;
    }
}
