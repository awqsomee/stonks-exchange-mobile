package com.example.stonksexchange.models;

import java.util.ArrayList;

public class Investments {
    ArrayList<UserStock> userStocks;

    public Investments() {
    }

    public ArrayList<UserStock> getUserStocks() {
        return userStocks;
    }

    public void setUserStocks(ArrayList<UserStock> userStocks) {
        this.userStocks = userStocks;
    }
}
