package com.example.stonksexchange.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.models.Price;
import com.example.stonksexchange.utils.BackButtonHandler;
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
    TextView stockSymbol;
    ArrayList<Entry> prices;
    String[] dates;

    View view;

    private LineChart chart;

    public StockFragment() {
    }

    public static StockFragment newInstance(String symbol) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        app = App.getInstance();
        System.out.println("AASS");
        context = view.getContext();
        System.out.println("AASS2");
        BackButtonHandler.setupBackPressedCallback(this);
        System.out.println("AASS3");
        symbol = getArguments().getString("symbol");
        System.out.println("AASS4");

        stockSymbol = view.findViewById(R.id.stockSymbol);
        System.out.println("AASS5");
        stockSymbol.setText(symbol);
        System.out.println("AASS6");

        getStockData();

        System.out.println(symbol);
        return view;
    }

    private void getStockData() {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStockData(symbol, "", "");
        call.enqueue(new Callback<GetStockDataResponse>() {
            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    GetStockDataResponse data = response.body();
                    stockSymbol.setText(data.getStock().getName());
                    prices = data.getStock().getFullPrice();
                    dates = data.getStock().getAllDates();
                    chart = view.findViewById(R.id.chart);
                    setChartData();
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetStockDataResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setChartData(){

        LineDataSet dataset = new LineDataSet(prices, "График первый");
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setColors(new int[] {R.color.green}, context);    //цвет линии графика
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
        chart.setPadding(0,0,0,0);
        chart.invalidate();
    }

}