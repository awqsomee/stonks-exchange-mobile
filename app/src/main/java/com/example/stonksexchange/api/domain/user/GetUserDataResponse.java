package com.example.stonksexchange.api.domain.user;

import com.example.stonksexchange.models.User;

public class GetUserDataResponse {
    User user;
    String message;

    public GetUserDataResponse() {
    }

    public GetUserDataResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }
}
