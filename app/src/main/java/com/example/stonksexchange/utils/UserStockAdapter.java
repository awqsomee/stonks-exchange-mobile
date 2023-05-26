package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;

import java.util.List;

import android.widget.TextView;

import com.example.stonksexchange.models.UserStock;

public class UserStockAdapter extends RecyclerView.Adapter<UserStockAdapter.StockViewHolder> {
    private List<UserStock> stocks;

    public UserStockAdapter(List<UserStock> stocks) {
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        UserStock stock = stocks.get(position);
        holder.stockName.setText(stock.getName());
        holder.stockSymbol.setText(stock.getSymbol());
        holder.stockPrice.setText(stock.getPrice() + " " + stock.getCurrency());
        holder.stockChange.setText(stock.getChange() + "%");
        holder.stockAmount.setText(stock.getAmount() + " шт");
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockName;
        TextView stockSymbol;
        TextView stockPrice;
        TextView stockChange;
        TextView stockAmount;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockName = itemView.findViewById(R.id.stockName);
            stockSymbol = itemView.findViewById(R.id.stockSymbol);
            stockPrice = itemView.findViewById(R.id.stockPrice);
            stockChange = itemView.findViewById(R.id.stockChange);
            stockAmount = itemView.findViewById(R.id.stockAmount);
        }
    }
}