package com.example.stonksexchange.models;

import java.util.List;

public class Stock {
    String symbol;
    String name;
    String shortname;
    String latname;
    String currency;
    List<Price> prices;
    Number isTraded;
//    String isin;
//    String issuesize;
//    String facevalue;
//    String faceunit;
//    String issuedate;
//    String isqualifiedinvestors;
//    String typename;
//    String group;
//    String type;
//    String groupname;
//    String emitterId;
//    String engine;
//    String market;
//    String board;

    public Stock() {
    }

    public Stock(String symbol, String name, String shortname, String latname, String currency, List<Price> prices, Number isTraded) {
        this.symbol = symbol;
        this.name = name;
        this.shortname = shortname;
        this.latname = latname;
        this.currency = currency;
        this.prices = prices;
        this.isTraded = isTraded;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getShortname() {
        return shortname;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        if (prices.size() > 0 && prices.get(0).getClose() != null) {
            return prices.get(0).getClose().toString();
        }
        return "-";
    }

    public Float getChange() {
        if (prices.size() > 0 && prices.get(0).getClose() != null) {
            return (prices.get(0).getClose() - prices.get(1).getClose()) / prices.get(0).getClose() * 100;
        }
        return 0f;
    }

    public String getLatname() {
        return latname;
    }
}
