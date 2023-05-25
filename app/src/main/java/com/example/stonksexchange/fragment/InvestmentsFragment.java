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
import com.example.stonksexchange.api.domain.stock.GetUserStocksResponse;
import com.example.stonksexchange.models.UserStock;
import com.example.stonksexchange.utils.UserStockAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentsFragment extends Fragment {
    App app;
    Context context;

    RecyclerView recyclerView;

    ArrayList<UserStock> stocks = new ArrayList<>();
    public InvestmentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investments, container, false);

        app = App.getInstance();
        context = view.getContext();

        recyclerView = view.findViewById(R.id.userStockList);

        getUserStocks();

        return view;
    }

    private void getUserStocks() {
        Call<GetUserStocksResponse> call = ApiService.AuthApiService.getUserStocks();
        call.enqueue(new Callback<GetUserStocksResponse>() {
            @Override
            public void onResponse(Call<GetUserStocksResponse> call, Response<GetUserStocksResponse> response) {
                if (response.isSuccessful()) {
                    GetUserStocksResponse data = response.body();
                    stocks = data.getStocks();
                    recyclerView.setAdapter(new UserStockAdapter(stocks));
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetUserStocksResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}