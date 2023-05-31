package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.fragment.WalletFragment;
import com.example.stonksexchange.models.Currency;

import java.util.ArrayList;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ButtonViewHolder> {
    private ArrayList<Currency> currencies;
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

        boolean isSelected = currency.getSymbol().equals(app.getWallet().getSelectedCurrency().getSymbol());

        if(currency.getSymbol().equals("RUB")) {
            holder.toggleButton.setTextOn(app.getUser().getBalanceString() + " " + currency.getSymbol());
            holder.toggleButton.setTextOff(app.getUser().getBalanceString() + " " + currency.getSymbol());
        }
        else {
            holder.toggleButton.setTextOn(currency.getAmount() + " " + currency.getSymbol());
            holder.toggleButton.setTextOff(currency.getAmount() + " " + currency.getSymbol());
        }


        holder.toggleButton.setChecked(isSelected);

        holder.toggleButton.setOnClickListener(view -> {
            if (isSelected) {
                holder.toggleButton.setChecked(true); // Keep the ToggleButton checked
            } else {
                app.getWallet().setSelectedCurrency(currency);
                fragment.onSelectedCurrencyChange();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public int updateCurrencyList(Currency currency) {
        for (int i = 0; i < currencies.size(); i++) {
            if (currency.getSymbol().equals(this.currencies.get(i).getSymbol())) {
                this.currencies.set(i, currency);
                notifyItemChanged(i);
                return i;
            }
            if (this.currencies.get(i).getSymbol().equals("RUB")) {
                this.currencies.get(i).setAmount(app.getUser().getBalance());
                notifyItemChanged(i);
            }
        }
        return 0;
    }

    public void addCurrency(Currency currency) {
        notifyItemChanged(currencies.indexOf(app.getWallet().getSelectedCurrency()));
        this.currencies.add(currency);
        fragment.onSelectedCurrencyChange();
        app.getWallet().setSelectedCurrency(currency);
        notifyItemInserted(currencies.size() - 1);
        notifyItemChanged(currencies.size() - 1);
        fragment.onSelectedCurrencyChange();
    }

    public void deleteCurrency(Currency currency) {
        notifyItemChanged(0);
        for (int i = 0; i < this.currencies.size(); i++) {
            if (currency.getSymbol().equals(this.currencies.get(i).getSymbol())) {
                app.getWallet().setSelectedCurrency(this.currencies.get(0));
                fragment.onSelectedCurrencyChange();
                this.currencies.remove(this.currencies.get(i));

                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void changeBalance() {
        if (this.currencies.get(0).getSymbol().equals("RUB")) {
            this.currencies.get(0).setAmount(app.getUser().getBalance());
        }
        notifyDataSetChanged();
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        ToggleButton toggleButton;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.toggleButton);
        }
    }

}

