package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;

import java.util.ArrayList;
import java.util.List;

public class GetUserCurrenciesResponse {
    ArrayList<Currency> currencies;
    String message;

    public GetUserCurrenciesResponse() {
    }

    public GetUserCurrenciesResponse(ArrayList<Currency> currencies, String message) {
        this.currencies = currencies;
        this.message = message;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }
}
