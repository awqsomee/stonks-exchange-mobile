package com.example.stonksexchange.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.api.ApiManager;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.api.domain.stock.StockExchangeRequest;
import com.example.stonksexchange.api.domain.stock.StockExchangeResponse;
import com.example.stonksexchange.models.Price;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.FetchSvgTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockFragment extends Fragment {
    App app;
    Context context;
    String symbol;
    String name;
    int amount;

    View view;
    TextView stockSymbol;
    TextView stockFullname;
    TextView stockPrice;
    TextView stockChange;
    TextView maxStockPrice;
    TextView minStockPrice;
    TextView stockPriceChange;
    ArrayList<Entry> prices;
    String[] dates;
    ConstraintLayout pricesLayout;
    Stock stock;

    Counters counters;
    private LineChart chart;
    int counter = 0;


    public StockFragment() {
    }

    public static StockFragment newInstance(String symbol, String name, int amount) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        args.putString("name", name);
        args.putInt("amount", amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.goBackBtn.setVisibility(View.VISIBLE);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);
        symbol = getArguments().getString("symbol");
        name = getArguments().getString("name");
        amount = getArguments().getInt("amount");

        stockFullname = view.findViewById(R.id.stockFullname);
        stockSymbol = view.findViewById(R.id.stockSymbol);
        stockPrice = view.findViewById(R.id.stockPrice);
        stockChange = view.findViewById(R.id.stockChange);
        maxStockPrice = view.findViewById(R.id.maxStockPrice);
        minStockPrice = view.findViewById(R.id.minStockPrice);
        stockPriceChange = view.findViewById(R.id.stockPriceChange);
        pricesLayout = view.findViewById(R.id.pricesLayout);

        stockFullname.setText(name);
        stockSymbol.setText(symbol);

        getStockData();

        return view;
    }

    private void getStockData() {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStockData(symbol, "", "");
        call.enqueue(new Callback<GetStockDataResponse>() {
            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    ImageView imageView = view.findViewById(R.id.imageView);
                    String iconUrl;
                    stock = response.body().getStock();

                    if (!stock.getLatname().matches("\\d")){
                        iconUrl = "https://cdn.bcs.ru/company-logos/" + stock.getLatname().toLowerCase() + ".svg";
                    } else {
                        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
                        iconUrl = "https://cdn.bcs.ru/company-logos/" + transliterator.transliterate(stock.getShortname().split("[ -]")[0].toLowerCase()) + ".svg";
                    }
                    FetchSvgTask task = new FetchSvgTask(iconUrl, imageView, context);
                    task.execute();

                    if (amount != 0) pricesLayout.setVisibility(View.VISIBLE);
                    prices = stock.getFullPrice();
                    dates = stock.getAllDates();
                    chart = view.findViewById(R.id.chart);
                    stockPrice.setText(stock.getPrice());
                    stockChange.setText(String.format("%.2f", stock.getChange()) + "%");
                    stockChange.setTextColor(Color.parseColor(stock.getChangeColor()));
                    maxStockPrice.setText(stock.getPrice());
                    minStockPrice.setText(stock.getOldestPrice());
                    stockPriceChange.setText(stock.getPriceChange());
                    stockPriceChange.setTextColor(Color.parseColor(stock.getPriceChangeColor()));
                    setChartData();
                    if (stock.getPrices().get(0).getClose() != null)
                        counters = new Counters();
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetStockDataResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
            }
        });
    }

    private void setChartData() {

        LineDataSet dataset = new LineDataSet(prices, "График первый");
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setColors(new int[]{R.color.green}, context);    //цвет линии графика
        dataset.setDrawCircles(false);

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_gradients);
        dataset.setDrawFilled(true);
        dataset.setFillDrawable(drawable);

        dataset.setValueTextSize(10);
        dataset.setValueTextColor(context.getResources().getColor(R.color.white));

        dataset.setHighlightEnabled(true);  //линии при выделенной точке
        dataset.setHighLightColor(context.getResources().getColor(R.color.light_grey));

        LineData data = new LineData(dataset);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return dates[(int) value];
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#44454B"));
        xAxis.setTextColor(context.getResources().getColor(R.color.white));
        xAxis.setTextSize(10);
        xAxis.setValueFormatter(formatter);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawLabels(false); //значения на главной оси Y
        yAxis.setDrawGridLines(false); //горизонтальные линии
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawAxisLine(false);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setGridBackgroundColor(context.getResources().getColor(R.color.orange));
        chart.setData(data);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setKeepPositionOnRotation(true);
        chart.setPadding(0, 0, 0, 0);
        chart.invalidate();
    }

    private void exchangeStocks(int exchangeAmount) {
        Call<StockExchangeResponse> call = ApiService.AuthApiService.exchangeStock(new StockExchangeRequest(exchangeAmount, symbol));
        call.enqueue(new Callback<StockExchangeResponse>() {
            @Override
            public void onResponse(Call<StockExchangeResponse> call, Response<StockExchangeResponse> response) {
                if (response.isSuccessful()) {
                    StockExchangeResponse sResponse = response.body();

                    amount = sResponse.getStock().getAmount();
                    if (amount != 0) pricesLayout.setVisibility(View.VISIBLE);
                    else pricesLayout.setVisibility(View.GONE);
                    stockPrice.setText(sResponse.getStock().getPrice());
                    minStockPrice.setText(sResponse.getStock().getLatestPriceString());
                    maxStockPrice.setText(sResponse.getStock().getLatestPriceString());
                    stockPriceChange.setText("0.00%");
                    stockPriceChange.setTextColor(Color.parseColor("#E9EEF2"));
                    float balance = sResponse.getUser().getBalance();
                    app.pushTransaction(sResponse.getTransaction());
                    app.getUser().setBalance(balance);

                    counters.setCounters(balance, sResponse.getStock().getPrices().get(0).getClose(), amount);

                    Toast.makeText(context, sResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<StockExchangeResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
            }
        });
    }

    private class Counters {
        Button buyBtn = view.findViewById(R.id.buyBtn);
        Button sellBtn = view.findViewById(R.id.sellBtn);
        ConstraintLayout buyCounter = view.findViewById(R.id.stockBuyCounter);
        ConstraintLayout sellCounter = view.findViewById(R.id.stockSellCounter);
        Button lessBuyBtn = view.findViewById(R.id.lessBuyBtn);
        Button moreBuyBtn = view.findViewById(R.id.moreBuyBtn);
        Button lessSellBtn = view.findViewById(R.id.lessSellBtn);
        Button moreSellBtn = view.findViewById(R.id.moreSellBtn);
        TextView selectedBuyCount = view.findViewById(R.id.selectedBuyCount);
        TextView selectedSellCount = view.findViewById(R.id.selectedSellCount);
        TextView allBuyCount = view.findViewById(R.id.allBuyCount);
        TextView allSellCount = view.findViewById(R.id.allSellCount);

        int allBuyCounter = 0;

        Counters() {
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(counter);
                    if (counter == 0) {
                        sellBtn.setVisibility(View.GONE);
                        buyCounter.setVisibility(View.VISIBLE);
                        counter = 1;
                        setCounters();
                        return;
                    }

                    exchangeStocks(counter);

                    counter = 0;
                    checkCounter();
                }
            });

            sellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (amount == 0) return;

                    if (counter == 0) {
                        buyBtn.setVisibility(View.GONE);
                        sellCounter.setVisibility(View.VISIBLE);
                        counter = 1;
                        setCounters();
                        return;
                    }

                    exchangeStocks(-counter);

                    counter = 0;
                    checkCounter();
                }
            });

            lessBuyBtn.setOnClickListener(new lessClickListener());
            moreBuyBtn.setOnClickListener(new moreClickListener());
            lessSellBtn.setOnClickListener(new lessClickListener());
            moreSellBtn.setOnClickListener(new moreSellClickListener());
                setCounters(app.getUser().getBalance(), stock.getPrices().get(0).getClose(), amount);
        }

        private void checkCounter() {
            if (counter <= 0) {
                sellBtn.setVisibility(View.VISIBLE);
                buyBtn.setVisibility(View.VISIBLE);
                buyCounter.setVisibility(View.GONE);
                sellCounter.setVisibility(View.GONE);
            }
        }

        public void setCounters(float balance, float price, int amount) {
            allSellCount.setText(amount + "");
            this.allBuyCounter = (int) Math.floor(balance / price);
            allBuyCount.setText(allBuyCounter + "");
        }

        private class lessClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                counter--;
                checkCounter();
                setCounters();
            }
        }

        private class moreClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                System.out.println(allBuyCounter);
                if (counter < allBuyCounter) {
                    counter++;
                }
                setCounters();
            }
        }

        private class moreSellClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                if (counter < amount) {
                    counter++;
                }
                setCounters();
            }
        }

        private void setCounters() {
            selectedBuyCount.setText(counter + "");
            selectedSellCount.setText(counter + "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.goBackBtn.setVisibility(View.GONE);
    }
}