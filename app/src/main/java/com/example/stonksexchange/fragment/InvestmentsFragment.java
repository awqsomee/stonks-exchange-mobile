package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.stock.GetUserStocksResponse;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.models.UserStock;
import com.example.stonksexchange.utils.ArrayListSortUtil;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.StockAdapter;
import com.example.stonksexchange.utils.UserStockAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentsFragment extends Fragment {
    App app;
    Context context;
    RecyclerView recyclerView;
    Fragment fragment;
    ToggleButton changeSortOrderBtn;
    ToggleButton sortByChangeBtn;
    ToggleButton sortByNameBtn;
    boolean isLoading = false;
    Comparator<UserStock> comparator = Comparator.comparing(UserStock::getChange).reversed();
    private CountDownLatch responseCountDownLatch;

    ArrayList<UserStock> stocks = new ArrayList<>();
    public InvestmentsFragment() {
    }

    private void setAdapter() {
        recyclerView.setAdapter(new UserStockAdapter(ArrayListSortUtil.sortArrayList(app.getInvestments().getUserStocks(), comparator, true), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investments, container, false);
        
        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);

        isLoading = true;

        recyclerView = view.findViewById(R.id.userStockList);

        getUserStocks();
        fragment = this;

        return view;
    }

    private void getUserStocks() {
        Call<GetUserStocksResponse> call = ApiService.AuthApiService.getUserStocks();
        call.enqueue(new Callback<GetUserStocksResponse>() {
            @Override
            public void onResponse(Call<GetUserStocksResponse> call, Response<GetUserStocksResponse> response) {
                if (response.isSuccessful()) {
                    GetUserStocksResponse data = response.body();
                    app.getInvestments().setUserStocks(data.getStocks());
                    setAdapter();
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<GetUserStocksResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }
}