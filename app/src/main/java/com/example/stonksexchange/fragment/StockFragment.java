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
import com.example.stonksexchange.utils.BackButtonHandler;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockFragment extends Fragment {
    App app;
    Context context;
    String symbol;
    TextView textView;

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
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);
        symbol = getArguments().getString("symbol");

        textView = view.findViewById(R.id.stockFullname);
        textView.setText(symbol);

        getStockData();
        chart = view.findViewById(R.id.chart);
        setChartData();
        return view;
    }

    private void getStockData() {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStockData(symbol, "", "");
        call.enqueue(new Callback<GetStockDataResponse>() {
            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    GetStockDataResponse data = response.body();
                    textView.setText(data.getStock().getName());
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
        // Массив координат точек
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 5f));
        entries.add(new Entry(2f, 2f));
        entries.add(new Entry(3f, 1f));
        entries.add(new Entry(4f, 3f));
        entries.add(new Entry(5f, 4f));
        entries.add(new Entry(6f, 1f));

        LineDataSet dataset = new LineDataSet(entries, "График первый");
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setColors(new int[] {R.color.green}, context);    //цвет линии графика

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_gradients);
        dataset.setDrawFilled(true);
        dataset.setFillDrawable(drawable);

//        dataset.setGradientColor(getResources().getColor(R.color.green), getResources().getColor(R.color.app_background));
        dataset.setValueTextSize(10);
        dataset.setValueTextColor(getResources().getColor(R.color.white));

        dataset.setHighlightEnabled(true);  //линии при выделенной точке
        dataset.setHighLightColor(getResources().getColor(R.color.light_grey));

        LineData data = new LineData(dataset);

        IMarker marker = chart.getMarker();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#44454B"));
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setTextSize(10);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawAxisLine(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
//        legend.setTextColor(Color.parseColor("#EE8100"));
        Description description = chart.getDescription();
        description.setEnabled(false);

        chart.getAxisRight().setEnabled(false);
        chart.setGridBackgroundColor(getResources().getColor(R.color.orange));
        chart.setData(data);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setKeepPositionOnRotation(true);
        chart.setPadding(0,0,0,0);
        chart.invalidate();
    }

}