package com.example.stonksexchange.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.example.stonksexchange.api.domain.forex.OpenAccountResponse;
import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.models.CurrencyShort;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.ButtonAdapter;

import java.util.ArrayList;
import com.example.stonksexchange.models.Currency;
import com.example.stonksexchange.utils.CurrenciesAdapter;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
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
    TextView prev_price;
    TextView cur_price;
    TextView price_change;
    TextView currencyFullName;
    ConstraintLayout pricesLayout;
    WalletFragment fragment;
    ImageButton closeWalletBtn;
    Button openWalletBtn;
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
        openWalletBtn = view.findViewById(R.id.button);
        currencyFullName = view.findViewById(R.id.currencyFullName);

        openWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyPopup(view);
            }
        });

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

                    adapter.changeBalance();

                    amountInput.setText("");
                    balanceText.setText(app.getUser().getBalanceString());
                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollToPosition(0);
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
                    price_change.setText(data.getCurrency().getDifferenceString());

                    data.getCurrencies().add(0, new Currency("", "RUB", "Российский рубль", app.getUser().getBalance(), 0, 0, 0));
                    int index = adapter.updateCurrencyList(data.getCurrency());
                    app.getWallet().setUserCurrencies(adapter.getCurrencies());

                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollToPosition(index);
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
                    app.getWallet().setSelectedCurrency(app.getWallet().getUserCurrencies().get(0));
                    adapter = new CurrenciesAdapter(fragment, app.getWallet().getUserCurrencies());
                    currencyFullName.setText( app.getWallet().getSelectedCurrency().getName());
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
        currencyFullName.setText(app.getWallet().getSelectedCurrency().getName());
        if (app.getWallet().getSelectedCurrency().getSymbol() == "RUB") {
            pricesLayout.setVisibility(View.GONE);
            closeWalletBtn.setVisibility(View.GONE);
            balanceText.setText(app.getUser().getBalanceString());
        } else {
            pricesLayout.setVisibility(View.VISIBLE);
            closeWalletBtn.setVisibility(View.VISIBLE);
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
                        app.getUser().setBalance(data.getUser().getBalance());

                        app.pushTransaction(data.getTransactionExchange());
                        app.pushTransaction(data.getTransactionClose());

                        adapter.changeBalance();
                        adapter.deleteCurrency(data.getCurrency());

                        app.getWallet().setUserCurrencies(adapter.getCurrencies());

                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                        recyclerView.smoothScrollToPosition(0);
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

    private void showCurrencyPopup(View anchorView) {
        // Create the custom dialog
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_currency);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set the desired width and height of the dialog
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = anchorView.getWidth() - 200;
        params.height = anchorView.getHeight() - 300;
        dialog.getWindow().setAttributes(params);

        // Get the ListView from the dialog layout
        ListView currencyListView = dialog.findViewById(R.id.currencyListView);

        // Create an ArrayList of currency symbols
        List<String> currencySymbols = new ArrayList<>(app.getWallet().getCurrencyNames());

        // Create an ArrayAdapter for the currency symbols
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, currencySymbols);

        // Set the adapter on the ListView
        currencyListView.setAdapter(adapter);

        // Set the item click listener for the ListView
        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCurrencySymbol = app.getWallet().getCurrencySymbols().get(position);

                openAccount(selectedCurrencySymbol);

                dialog.dismiss();
            }
        });

        params.gravity = Gravity.CENTER; // Center the dialog on the screen
        dialog.getWindow().setAttributes(params);

        // Show the dialog
        dialog.show();
    }

    public void openAccount(String symbol) {
        Call<OpenAccountResponse> call = ApiService.AuthApiService.openAccount(symbol);
        call.enqueue(new Callback<OpenAccountResponse>() {
            @Override
            public void onResponse(Call<OpenAccountResponse> call, Response<OpenAccountResponse> response) {
                if (response.isSuccessful()) {
                    OpenAccountResponse data = response.body();
                    app.pushTransaction(data.getTransaction());
                    adapter.addCurrency(data.getCurrency());


                    app.getWallet().setUserCurrencies(adapter.getCurrencies());

                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollToPosition(adapter.getCurrencies().size() - 1);
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<OpenAccountResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}