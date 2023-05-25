package com.example.stonksexchange.api.domain.forex;

import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.CurrencyShort;

import java.util.Map;

public class GetCurrenciesResponse {
    private Map<String, CurrencyShort> currencies;
    String message;

    public GetCurrenciesResponse() {
    }

    public GetCurrenciesResponse(Map<String, CurrencyShort> currencies, String message) {
        this.currencies = currencies;
        this.message = message;
    }

    public Map<String, CurrencyShort> getCurrencies() {
        return currencies;
    }
}
