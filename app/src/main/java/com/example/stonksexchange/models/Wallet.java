package com.example.stonksexchange.models;

import java.util.ArrayList;
import java.util.Map;

public class Wallet {
    Map<String, CurrencyShort> currencies;
    ArrayList<Currency> userCurrencies;

    public Wallet() {
        userCurrencies = new ArrayList<>();
        userCurrencies.add(new Currency("", "RUB", "Рубль", 0, 1, 1, 0, ""));
    }

    public Wallet(ArrayList<Currency> currencies) {
        currencies.add(new Currency("", "RUB", "Рубль", 0, 1, 1, 0, ""));
        this.userCurrencies = currencies;
    }

    public void setCurrencies(Map<String, CurrencyShort> currencies) {
        this.currencies = currencies;
    }

    public Map<String, CurrencyShort> getCurrencies() {
        return currencies;
    }
}
