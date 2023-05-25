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
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.utils.StockAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {
    App app;
    Context context;

    RecyclerView recyclerView;

    ArrayList<Stock> stocks = new ArrayList<>();

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        app = App.getInstance();
        context = view.getContext();

        recyclerView = view.findViewById(R.id.stockList);

        getStock("GAZP");
        getStock("MGNT");
        getStock("YANDEX");
        return view;
    }

    private void getStock(String symbol) {
        Call<GetStockDataResponse> call = ApiService.ApiService.getStock(symbol);
        call.enqueue(new Callback<GetStockDataResponse>() {
            @Override
            public void onResponse(Call<GetStockDataResponse> call, Response<GetStockDataResponse> response) {
                if (response.isSuccessful()) {
                    GetStockDataResponse data = response.body();
                    stocks.add(data.GetFirstStock());
                    recyclerView.setAdapter(new StockAdapter(stocks));
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
}
