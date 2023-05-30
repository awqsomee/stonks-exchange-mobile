package com.example.stonksexchange.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceResponse;
import com.example.stonksexchange.api.domain.forex.CloseAccountResponse;
import com.example.stonksexchange.api.domain.forex.CurrencyExchangeRequest;
import com.example.stonksexchange.api.domain.forex.CurrencyExchangeResponse;
import com.example.stonksexchange.api.domain.forex.GetCurrenciesResponse;
import com.example.stonksexchange.api.domain.forex.GetUserCurrenciesResponse;
import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.CurrenciesAdapter;

import java.util.List;

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
    TextView prev_price;
    TextView cur_price;
    TextView price_change;
    ConstraintLayout pricesLayout;
    WalletFragment fragment;
    ImageButton closeWalletBtn;
    CurrenciesAdapter adapter;

    public WalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);
        fragment = this;

        amountInput = view.findViewById(R.id.amountET);
        replenishBtn = view.findViewById(R.id.replenishBtn);
        withdrawBtn = view.findViewById(R.id.withdrawBtn);
        balanceText = view.findViewById(R.id.balanceText);
        pricesLayout = view.findViewById(R.id.pricesLayout);
        prev_price = view.findViewById(R.id.prev_price);
        cur_price = view.findViewById(R.id.cur_price);
        price_change = view.findViewById(R.id.price_change);
        closeWalletBtn = view.findViewById(R.id.closeWalletBtn);
        recyclerView = view.findViewById(R.id.currencyList);

        balanceText.setText(String.format("%.2f", app.getUser().getBalance()));
        replenishBtn.setOnClickListener(new ReplenishClickListener());
        withdrawBtn.setOnClickListener(new WithdrawClickListener());
        closeWalletBtn.setOnClickListener(new CloseAccListener());

        getUserCurrencies();
        getCurrencies();

        return view;
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

    private void exchangeCurrency(CurrencyExchangeRequest request) {
        Call<CurrencyExchangeResponse> call = ApiService.AuthApiService.exchangeCurrency(request);
        call.enqueue(new Callback<CurrencyExchangeResponse>() {
            @Override
            public void onResponse(Call<CurrencyExchangeResponse> call, Response<CurrencyExchangeResponse> response) {
                if (response.isSuccessful()) {
                    CurrencyExchangeResponse data = response.body();
                    app.getUser().setBalance(data.getUser().getBalance());
                    app.pushTransaction(data.getTransaction());

                    amountInput.setText("");
                    balanceText.setText(data.getCurrency().getAmount().toString());

                    prev_price.setText(data.getCurrency().getLatestPriceString());
                    cur_price.setText(data.getCurrency().getLatestPriceString());
                    price_change.setText("0");

                    app.getWallet().setUserCurrencies(data.getCurrencies());
                    System.out.println("AAS 1");
                    adapter.updateCurrencyList(data.getCurrency());

                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<CurrencyExchangeResponse> call, Throwable t) {
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
                    data.getCurrencies().add(0, new Currency("", "RUB", "Российский рубль", app.getUser().getBalance(), 0, 0, 0));
                    app.getWallet().setUserCurrencies(data.getCurrencies());
                    adapter = new CurrenciesAdapter(fragment, app.getWallet().getUserCurrencies());
                    recyclerView.setAdapter(adapter);
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

    public void onSelectedCurrencyChange() {
        if (app.getWallet().getSelectedCurrency().getSymbol() == "RUB") {
            pricesLayout.setVisibility(View.GONE);
            balanceText.setText(app.getUser().getBalanceString());
        } else {
            pricesLayout.setVisibility(View.VISIBLE);
            Currency currency = app.getWallet().getSelectedCurrency();
            prev_price.setText(currency.getLatestPriceString());
            cur_price.setText(currency.getPriceString());
            price_change.setText(currency.getDifferenceString());
            balanceText.setText(currency.getAmount().toString());

            switch (currency.getDifferenceString().charAt(0)) {
                case '-':
                    price_change.setTextColor(Color.parseColor("#FF2A51"));
                    break;
                case '0':
                    price_change.setTextColor(Color.parseColor("#E9EEF2"));
                    break;
                default:
                    price_change.setTextColor(Color.parseColor("#BBFFA7"));
                    break;
            }
        }
    }

    private class ReplenishClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (amountInput.getText().toString().equals("")) return;
            if (app.getWallet().getSelectedCurrency().getSymbol() == "RUB") {
                ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest(Float.parseFloat(amountInput.getText().toString()));
                changeBalance(changeBalanceRequest);
            } else {
                CurrencyExchangeRequest currencyExchangeRequest = new CurrencyExchangeRequest(app.getWallet().getSelectedCurrency().getSymbol(), Float.parseFloat(amountInput.getText().toString()));
                exchangeCurrency(currencyExchangeRequest);
            }
        }
    }

    private class WithdrawClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (amountInput.getText().toString().equals("")) return;
            if (app.getWallet().getSelectedCurrency().getSymbol() == "RUB") {
                ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest(-Float.parseFloat(amountInput.getText().toString()));
                changeBalance(changeBalanceRequest);
            } else {
                CurrencyExchangeRequest currencyExchangeRequest = new CurrencyExchangeRequest(app.getWallet().getSelectedCurrency().getSymbol(), -Float.parseFloat(amountInput.getText().toString()));
                exchangeCurrency(currencyExchangeRequest);
            }
        }
    }

    private class CloseAccListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Call<CloseAccountResponse> call = ApiService.AuthApiService.closeAccount(app.getWallet().getSelectedCurrency().getSymbol());
            call.enqueue(new Callback<CloseAccountResponse>() {
                @Override
                public void onResponse(Call<CloseAccountResponse> call, Response<CloseAccountResponse> response) {
                    if (response.isSuccessful()) {
                        CloseAccountResponse data = response.body();
                        // TODO: Рассортировать ответ
                    } else {
                        ErrorUtils.handleErrorResponse(response, context);
                    }
                }

                @Override
                public void onFailure(Call<CloseAccountResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}