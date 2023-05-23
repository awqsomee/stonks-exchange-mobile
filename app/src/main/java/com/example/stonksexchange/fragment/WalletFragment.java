package com.example.stonksexchange.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.LoginActivity;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.auth.AuthRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {
    App app;
    SharedPreferences sharedPref;
    Context context;

    EditText amountInput;
    Button replenishBtn;
    Button withdrawBtn;
    TextView balanceText;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        app = App.getInstance();
        context = view.getContext();
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);

        amountInput = view.findViewById(R.id.amountET);
        replenishBtn = view.findViewById(R.id.replenishBtn);
        withdrawBtn = view.findViewById(R.id.withdrawBtn);
        balanceText = view.findViewById(R.id.balanceText);

        balanceText.setText(app.getUser().getBalance().toString());
        replenishBtn.setOnClickListener(new ReplenishClickListener());
        withdrawBtn.setOnClickListener(new WithdrawClickListener());

        return view;
    }


    private class ReplenishClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest(Float.parseFloat(amountInput.getText().toString()));
            changeBalance(changeBalanceRequest);
        }
    }

    private class WithdrawClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
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
                    balanceText.setText(data.getUser().getBalance().toString());
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
}