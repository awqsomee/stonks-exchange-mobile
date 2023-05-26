package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.utils.StockAdapter;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {
    App app;
    Context context;

    RecyclerView recyclerView;
    boolean isLoading = false;

    ArrayList<Stock> stocks = new ArrayList<>();
    private CountDownLatch responseCountDownLatch;

    public CatalogFragment() {
    }

    public void updateUI() {
        stocks = app.getDisplayedStocks();
        recyclerView.setAdapter(new StockAdapter(stocks));
    }

    public void clearUI() {
        stocks = app.getDisplayedStocks();
        getStandartStocks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        app = App.getInstance();
        context = view.getContext();

        recyclerView = view.findViewById(R.id.stockList);

        stocks = app.getDisplayedStocks();
        if (app.getDisplayedStocks().size() == 0) {
            getStandartStocks();
        } else recyclerView.setAdapter(new StockAdapter(stocks));

        return view;
    }

    private void getStock(String symbol) {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStockData(symbol, "", "");
        call.enqueue(new Callback<GetStockDataResponse>() {
            void countDown() {
                responseCountDownLatch.countDown();
                if (responseCountDownLatch.getCount() == 0) {
                    app.setDisplayedStocks(stocks);
                    recyclerView.setAdapter(new StockAdapter(stocks));
                    isLoading = false;
                }
            }

            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    GetStockDataResponse data = response.body();
                    stocks.add(data.getStock());
                } else {
//                    ErrorUtils.handleErrorResponse(response, context);
                }
                countDown();

            }

            @Override
            public void onFailure(Call<GetStockDataResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                countDown();
            }
        });
    }

    private void getStandartStocks() {
        if (!isLoading) {
            isLoading = true;
            int numberOfApiCalls = 6;
            responseCountDownLatch = new CountDownLatch(numberOfApiCalls);
            getStock("GAZP");
            getStock("MGNT");
            getStock("YNDX");
            getStock("TSLA-RM");
            getStock("AAPL-RM");
            getStock("META-RM");
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
