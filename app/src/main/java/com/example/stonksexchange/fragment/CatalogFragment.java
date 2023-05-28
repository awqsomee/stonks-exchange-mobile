package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.utils.ArrayListSortUtil;
import com.example.stonksexchange.utils.StockAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {
    App app;
    Context context;

    RecyclerView recyclerView;
    ToggleButton changeSortOrderBtn;
    ToggleButton sortByChangeBtn;
    ToggleButton sortByNameBtn;

    boolean isLoading = false;
    private CountDownLatch responseCountDownLatch;

    boolean isSortAsc = true;
    Comparator<Stock> comparator = Comparator.comparing(Stock::getShortname);

    public CatalogFragment() {
    }

    public void updateUI() {
        recyclerView.setAdapter(new StockAdapter(
                ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));
    }

    public void clearUI() {
        getStandartStocks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        app = App.getInstance();
        context = view.getContext();

        recyclerView = view.findViewById(R.id.stockList);
        changeSortOrderBtn = view.findViewById(R.id.changeSortOrderBtn);
        sortByChangeBtn = view.findViewById(R.id.sortByChangeBtn);
        sortByNameBtn = view.findViewById(R.id.sortByNameBtn);

        setSortClickListeners();
        if (app.getDisplayedStocks().size() == 0) {
            getStandartStocks();
        } else
            recyclerView.setAdapter(new StockAdapter(
                    ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));

        return view;
    }

    private void setSortClickListeners() {
        changeSortOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSortAsc = !isSortAsc;
                recyclerView.setAdapter(new StockAdapter(
                        ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));
            }
        });

        sortByChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparator = Comparator.comparing(Stock::getChange);
                recyclerView.setAdapter(new StockAdapter(
                        ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));
            }
        });

        sortByNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparator = Comparator.comparing(Stock::getShortname);
                recyclerView.setAdapter(new StockAdapter(
                        ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));
            }
        });
    }

    private void getStock(String symbol) {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStockData(symbol, "", "");
        call.enqueue(new Callback<GetStockDataResponse>() {
            void countDown() {
                responseCountDownLatch.countDown();
                if (responseCountDownLatch.getCount() == 0) {
                    recyclerView.setAdapter(new StockAdapter(
                            ArrayListSortUtil.sortArrayList(app.getDisplayedStocks(), comparator, isSortAsc)));
                    isLoading = false;
                }
            }

            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    GetStockDataResponse data = response.body();
                    app.getDisplayedStocks().add(data.getStock());
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
