package com.example.stonksexchange.api.domain;

import com.example.stonksexchange.models.User;

public class AuthResponse {
    String token;
    User user;
    String message;

    public AuthResponse() {
    }

    public AuthResponse(String token, User user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}

