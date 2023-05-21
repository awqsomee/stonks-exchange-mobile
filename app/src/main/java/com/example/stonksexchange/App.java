package com.example.stonksexchange;

import android.app.Application;

import com.example.stonksexchange.models.User;

public class App extends Application {
    private static App instance;
    private User user;
    private boolean isAuth;

    public App() {
        isAuth = false;
    }

    public static synchronized App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(boolean auth) {
        this.isAuth = auth;
    }
}