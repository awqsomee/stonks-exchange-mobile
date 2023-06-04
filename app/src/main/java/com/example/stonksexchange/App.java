package com.example.stonksexchange;

import android.app.Application;

import com.example.stonksexchange.models.Investments;
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
    private Investments investments;

    public App() {
        super.onCreate();

        isAuth = false;
        transactions = new ArrayList<>();
        displayedStocks = new ArrayList<>();
        wallet = new Wallet();
        investments = new Investments();
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

    public void setUserData(User user) {
        this.user.setUsername(user.getUsername());
        this.user.setName(user.getName());
        this.user.setAvatar(user.getAvatar());
        this.user.setBirthday(user.getBirthday());
        this.user.setEmail(user.getEmail());
        this.user.setPassportNumber(user.getPassportNumber());
        this.user.setPhoneNumber(user.getPhoneNumber());
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

    public Investments getInvestments() {
        return investments;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}