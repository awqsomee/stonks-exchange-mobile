package com.example.stonksexchange;

import android.app.Application;

import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;

import java.util.ArrayList;

public class App extends Application {
    private static App instance;
    private User user;
    private ArrayList<Transaction> transactions;
    private boolean isAuth;

    public App() {
        super.onCreate();
        isAuth = false;
        transactions = new ArrayList<>();
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


    public void pushTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}