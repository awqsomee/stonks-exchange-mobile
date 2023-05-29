package com.example.stonksexchange.api.domain.user;

import com.example.stonksexchange.models.User;

public class ChangeUserDataRequest {
    User user;

    public ChangeUserDataRequest(User user) {
        this.user = user;
    }
}
