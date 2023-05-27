package com.example.stonksexchange;

import android.app.Application;

import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.models.Transaction;
import com.example.stonksexchange.models.User;
import com.example.stonksexchange.models.Wallet;

import java.util.ArrayList;

public class App extends Application {
    private static App instance;
    private User user;
    private ArrayList<Stock> displayedStocks;
    private ArrayList<Transaction> transactions;
    private boolean isAuth;
    private Wallet wallet;

    public App() {
        super.onCreate();

        isAuth = false;
        transactions = new ArrayList<>();
        displayedStocks = new ArrayList<>();
        wallet = new Wallet();
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

    public Wallet getWallet() {
        return wallet;
    }

    public ArrayList<Stock> getDisplayedStocks() {
        return displayedStocks;
    }

    public void setDisplayedStocks(ArrayList<Stock> displayedStocks) {
        this.displayedStocks = displayedStocks;
    }
}