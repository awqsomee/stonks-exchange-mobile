package com.example.stonksexchange.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;
import com.example.stonksexchange.api.domain.stock.GetUserStocksResponse;
import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.Stock;
import com.example.stonksexchange.models.UserStock;
import com.example.stonksexchange.utils.ArrayListSortUtil;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.CurrenciesAdapter;
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
    View view;
    RecyclerView recyclerView;
    Fragment fragment;
    boolean isLoading = false;
    Comparator<UserStock> comparator = Comparator.comparing(UserStock::getChange).reversed();
    private CountDownLatch responseCountDownLatch;

    public InvestmentsFragment() {
    }

    private void setAdapter() {
        recyclerView.setAdapter(new UserStockAdapter(ArrayListSortUtil.sortArrayList(app.getInvestments().getUserStocks(), comparator, true), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_investments, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);
        fragment = this;

        isLoading = true;

        recyclerView = view.findViewById(R.id.userStockList);

        int numberOfApiCalls = 2;
        responseCountDownLatch = new CountDownLatch(numberOfApiCalls);
        getUserStocks();
        getUserCurrencies();

        return view;
    }

    private void countData() {
        TextView userAsset = view.findViewById(R.id.userAsset);
        TextView titleUserIncomeCur = view.findViewById(R.id.titleUserIncomeCur);
        TextView userIncomeCur = view.findViewById(R.id.userIncomeCur);
        TextView titleUserIncomePer = view.findViewById(R.id.titleUserIncomePer);
        TextView userIncomePer = view.findViewById(R.id.userIncomePer);

        Float assets = app.getUser().getBalance().floatValue();
        Float difference = 0f;
        ArrayList<Currency> userCurrencies = app.getWallet().getUserCurrencies();
        for (Currency currency : userCurrencies) {
            assets += currency.getAmount().floatValue() * currency.getPrice().floatValue();
            difference += currency.getDifference().floatValue();
        }
        for (UserStock stock : app.getInvestments().getUserStocks()) {
            assets += stock.getAmount() * stock.getPrices().get(0).getClose();
            difference += stock.getAmount() * (stock.getPrices().get(0).getClose() - stock.getLatestPrice().floatValue());
        }

        userAsset.setText(assets + " руб");
        if (difference >= 0) {
            titleUserIncomeCur.setText("Прибыль");
            titleUserIncomePer.setText("Прибыль, %");
            userIncomeCur.setTextColor(Color.parseColor("#BBFFA7"));
            userIncomePer.setTextColor(Color.parseColor("#BBFFA7"));
            if (difference == 0) {
                userIncomeCur.setTextColor(Color.parseColor("#E9EEF2"));
                userIncomePer.setTextColor(Color.parseColor("#E9EEF2"));
            }
        } else {
            titleUserIncomeCur.setText("Убытки");
            titleUserIncomePer.setText("Убытки, %");
            userIncomeCur.setTextColor(Color.parseColor("#FF2A51"));
            userIncomePer.setTextColor(Color.parseColor("#FF2A51"));
        }
        userIncomeCur.setText(String.format("%.2f", difference) + " руб");
        if (assets != 0)
            userIncomePer.setText(String.format("%.2f", (difference / assets * 100)) + " %");
        else
            userIncomePer.setText(String.format("%.2f", 0f) + " %");
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
                countDown();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<GetUserStocksResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
                countDown();
                isLoading = false;
            }
        });
    }

    private void getUserCurrencies() {
        Call<GetUserCurrenciesResponse> call = ApiService.AuthApiService.getUserCurrencies();
        call.enqueue(new Callback<GetUserCurrenciesResponse>() {
            @Override
            public void onResponse(Call<GetUserCurrenciesResponse> call, Response<GetUserCurrenciesResponse> response) {
                if (response.isSuccessful()) {
                    GetUserCurrenciesResponse data = response.body();
                    data.getCurrencies().add(0, new Currency("", "RUB", "Российский рубль", app.getUser().getBalance(), 0, 0, 0));
                    app.getWallet().setUserCurrencies(data.getCurrencies());
                    app.getWallet().setSelectedCurrency(app.getWallet().getUserCurrencies().get(0));
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
                countDown();
            }

            @Override
            public void onFailure(Call<GetUserCurrenciesResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
                countDown();
            }
        });
    }

    private void countDown() {
        responseCountDownLatch.countDown();
        if (responseCountDownLatch.getCount() == 0) {
            countData();
        }
    }
}