package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;

import java.util.List;

public class GetUserCurrenciesResponse {
    List<Currency> currencies;
    String message;

    public GetUserCurrenciesResponse() {
    }

    public GetUserCurrenciesResponse(List<Currency> currencies, String message) {
        this.currencies = currencies;
        this.message = message;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
