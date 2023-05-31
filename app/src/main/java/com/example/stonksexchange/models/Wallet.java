package com.example.stonksexchange.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wallet {
    Map<String, CurrencyShort> currencies;
    ArrayList<Currency> userCurrencies;
    Currency selectedCurrency;

    public Wallet() {
        userCurrencies = new ArrayList<>();
    }

    public Wallet(ArrayList<Currency> currencies) {
        this.userCurrencies = currencies;
    }

    public void setCurrencies(Map<String, CurrencyShort> currencies) {
        this.currencies = currencies;
    }

    public Map<String, CurrencyShort> getCurrencies() {
        return currencies;
    }

    public ArrayList<String> getCurrencyNames() {
        ArrayList<String> names = new ArrayList<>();
        for (CurrencyShort currency : currencies.values()) {
            names.add(currency.getName());
        }
        return names;
    }

    public void setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public void setUserCurrencies(ArrayList<Currency> userCurrencies) {
        this.userCurrencies = userCurrencies;
    }

    public ArrayList<Currency> getUserCurrencies() {
        return userCurrencies;
    }

    public Currency getSelectedCurrency() {
        return selectedCurrency;
    }
}
