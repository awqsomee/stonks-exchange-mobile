package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;

import java.util.List;

import android.widget.TextView;

import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.fragment.StockFragment;
import com.example.stonksexchange.models.UserStock;

public class UserStockAdapter extends StockAdapter {

    public UserStockAdapter(List<UserStock> userStocks, Fragment fragment) {
        super(userStocks, fragment);
    }
    @NonNull
    @Override
    public UserStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_stock, parent, false);
        return new UserStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserStockAdapter.StockViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        UserStock stock = (UserStock) stocks.get(position);
        UserStockViewHolder userStockViewHolder = (UserStockViewHolder) holder;
        userStockViewHolder.stockAmount.setText(stock.getAmount() + " шт");
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class UserStockViewHolder extends StockViewHolder {
        TextView stockAmount;

        public UserStockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockAmount = itemView.findViewById(R.id.stockAmount);
        }
    }
}