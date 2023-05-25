package com.example.stonksexchange.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;

import java.util.List;

import android.widget.TextView;
import com.example.stonksexchange.models.Stock;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private List<Stock> stocks;

    public StockAdapter(List<com.example.stonksexchange.models.Stock> stocks) {
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.stockShortname.setText(stock.getShortname());
        holder.stockSymbol.setText(stock.getSymbol());
        holder.stockPrice.setText(stock.getPrice() + " " + stock.getCurrency());
        holder.stockChange.setText(String.format("%.2f", stock.getChange())  + "%");
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockShortname;
        TextView stockSymbol;
        TextView stockPrice;
        TextView stockChange;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockShortname = itemView.findViewById(R.id.stockShortname);
            stockSymbol = itemView.findViewById(R.id.stockSymbol);
            stockPrice = itemView.findViewById(R.id.stockPrice);
            stockChange = itemView.findViewById(R.id.stockChange);
        }
    }
}
