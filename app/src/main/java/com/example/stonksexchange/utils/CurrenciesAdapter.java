package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.fragment.WalletFragment;
import com.example.stonksexchange.models.Currency;

import java.util.ArrayList;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ButtonViewHolder> {
    private ArrayList<Currency> currencies;
    private ToggleButton checkedButton;
    private boolean isFirstItemChecked;
    private App app;
    private WalletFragment fragment;

    public CurrenciesAdapter(WalletFragment fragment, ArrayList<Currency> currencies) {
        this.currencies = currencies;
        app = App.getInstance();
        isFirstItemChecked = false;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);

        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        Currency currency = currencies.get(position);

        // Check the first toggle button only during initial creation
        if (!isFirstItemChecked && position == 0) {
            isFirstItemChecked = true;
            checkedButton = holder.toggleButton;
            holder.toggleButton.setChecked(true);
        }
        holder.toggleButton.setText(currency.getAmount() + " " + currency.getSymbol());
        holder.toggleButton.setTextOn(currency.getAmount() + " " + currency.getSymbol());
        holder.toggleButton.setTextOff(currency.getAmount() + " " + currency.getSymbol());

        holder.toggleButton.setOnClickListener(view -> {
            if (checkedButton == view) {
                holder.toggleButton.setChecked(true); // Keep the ToggleButton checked
            }
            else {
                app.getWallet().setSelectedCurrency(currency);
                checkedButton.toggle();
                checkedButton = holder.toggleButton;
                checkedButton.setChecked(true);
                fragment.onSelectedCurrencyChange();
            }
        });

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        ToggleButton toggleButton;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.toggleButton);
        }
    }

    public void updateCurrencyList(Currency currency) {
        for (int i = 0; i < this.currencies.size(); i++) {
            if (currency.getSymbol().equals(this.currencies.get(i).getSymbol())) {
                this.currencies.set(i, currency);
                break;
            }
        }

        for (Currency c : this.currencies) {
                System.out.println("AAS 4" + c.getSymbol() + " " + c.getAmount());
        }
        notifyDataSetChanged();
    }
}

