package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.models.Currency;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
    private ArrayList<Currency> currencies;
    private ToggleButton checkedButton;
    private boolean isFirstItemChecked;
    private App app;

    public ButtonAdapter(ArrayList<Currency> currencies) {
        this.currencies = currencies;
        app = App.getInstance();
        isFirstItemChecked = false;
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
                System.out.println("AAS eq");
                holder.toggleButton.setChecked(true); // Keep the ToggleButton checked
            }
            else {
                System.out.println("AAS neq");
                app.getWallet().setSelectedCurrency(currency);
                checkedButton.toggle();
                checkedButton = holder.toggleButton;
                checkedButton.setChecked(true);
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
}

