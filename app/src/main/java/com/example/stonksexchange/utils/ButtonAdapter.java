package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
    private List<String> buttonNames;

    public ButtonAdapter(List<String> buttonNames) {
        buttonNames.add("+");
        this.buttonNames = buttonNames;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        String buttonName = buttonNames.get(position);
        holder.currencyName.setText(buttonName);
        // Set click listener or any other customization for the button
    }

    @Override
    public int getItemCount() {
        return buttonNames.size();
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        ToggleButton btnItem;
        TextView currencyName;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.currencyName);
        }
    }
}

