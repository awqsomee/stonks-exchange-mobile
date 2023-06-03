package com.example.stonksexchange.models;

import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stock {
    String symbol;
    String name;
    String shortname;
    String latname;
    String currency;
    List<Price> prices;
//    List<Price> dates;

//    Number isTraded;
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

    public Stock(String symbol, String name, String shortname, String latname, String currency, List<Price> prices) {
        this.symbol = symbol;
        this.name = name;
        this.shortname = shortname;
        this.latname = latname;
        this.currency = currency;
        this.prices = prices;
//        this.dates = dates;
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

    public ArrayList getFullPrice() {
        ArrayList<Entry> data = new ArrayList<Entry>();
        if (prices.size() > 0 && prices.get(0).getClose() != null) {
            for (int i=0; i<prices.size(); i++){
                if (prices.get(i).getClose() != null) {
                    data.add(new Entry(i * 1f, prices.get(i).getClose()));
                }
                else{
                    data.add(new Entry(i * 1f, 0));
                }
            }
            return data;
        }
        return null;
    }

    public String[] getAllDates() {
        String[] data = new String[prices.size()];
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM");
        Date date = null;
        if (prices.size() > 0 && prices.get(0).getClose() != null) {
            for (int i=0; i<prices.size() - 1; i++){
                try {
                    date = inputFormat.parse(prices.get(i).getDate());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                data[i] = outputFormat.format(date);
            }
            return data;
        }
        return null;
    }
}
