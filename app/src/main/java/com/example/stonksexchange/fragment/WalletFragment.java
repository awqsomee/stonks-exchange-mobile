package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceResponse;
import com.example.stonksexchange.api.domain.forex.GetCurrenciesResponse;
import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;
import com.example.stonksexchange.models.CurrencyShort;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.ButtonAdapter;

import java.util.ArrayList;
import com.example.stonksexchange.models.Currency;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {
    App app;
    Context context;

    EditText amountInput;
    Button replenishBtn;
    Button withdrawBtn;
    TextView balanceText;
    RecyclerView recyclerView;

    List<String> currencySymbols;

    public WalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);

        amountInput = view.findViewById(R.id.amountET);
        replenishBtn = view.findViewById(R.id.replenishBtn);
        withdrawBtn = view.findViewById(R.id.withdrawBtn);
        balanceText = view.findViewById(R.id.balanceText);

        balanceText.setText(String.format("%.2f", app.getUser().getBalance()));
        replenishBtn.setOnClickListener(new ReplenishClickListener());
        withdrawBtn.setOnClickListener(new WithdrawClickListener());

        recyclerView = view.findViewById(R.id.currencyList);
        getUserCurrencies();
        getCurrencies();
        return view;
    }


    private class ReplenishClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (amountInput.getText().toString().equals("")) return;
            ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest(Float.parseFloat(amountInput.getText().toString()));
            changeBalance(changeBalanceRequest);
        }
    }

    private class WithdrawClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (amountInput.getText().toString().equals("")) return;
            ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest(-Float.parseFloat(amountInput.getText().toString()));
            changeBalance(changeBalanceRequest);
        }
    }

    private void changeBalance(ChangeBalanceRequest amount) {
        Call<ChangeBalanceResponse> call = ApiService.AuthApiService.changeBalance(amount);
        call.enqueue(new Callback<ChangeBalanceResponse>() {
            @Override
            public void onResponse(Call<ChangeBalanceResponse> call, Response<ChangeBalanceResponse> response) {
                if (response.isSuccessful()) {
                    ChangeBalanceResponse data = response.body();
                    app.getUser().setBalance(data.getUser().getBalance());
                    app.pushTransaction(data.getTransaction());

                    amountInput.setText("");
                    balanceText.setText(String.format("%.2f", data.getUser().getBalance()));
                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<ChangeBalanceResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

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
                    currencySymbols = new ArrayList<>();
                    currencySymbols.add("RUB");
                    for (Currency currency : data.getCurrencies()) {
                        String currencyString = currency.getSymbol();
                        currencySymbols.add(currencyString);
                    }
                    recyclerView.setAdapter(new ButtonAdapter(currencySymbols));
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetUserCurrenciesResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrencies() {
        Call<GetCurrenciesResponse> call = ApiService.ApiService.getCurrencies();
        call.enqueue(new Callback<GetCurrenciesResponse>() {
            @Override
            public void onResponse(Call<GetCurrenciesResponse> call, Response<GetCurrenciesResponse> response) {
                if (response.isSuccessful()) {
                    GetCurrenciesResponse data = response.body();

//                    for (Map.Entry<String, CurrencyShort> entry : data.getCurrencies().entrySet()) {
//                        String currencyCode = entry.getKey();
//                        CurrencyShort currency = entry.getValue();
//
//                        String name = currency.getName();
//                        double value = currency.getValue();
//                        double previous = currency.getPrevious();
//
//                        // Do something with the currency information
//                        System.out.println(currencyCode + ": " + name + ", Value: " + value + ", Previous: " + previous);
//                    }

                    app.getWallet().setCurrencies(data.getCurrencies());
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetCurrenciesResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}