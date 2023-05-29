package com.example.stonksexchange.api.domain.user;

import com.example.stonksexchange.models.User;

public class UserDataResponse {
    User user;
    String message;

    public UserDataResponse() {
    }

    public UserDataResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
