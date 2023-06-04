package com.example.stonksexchange.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;
import com.example.stonksexchange.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    protected List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.transactionsName.setText(transaction.getType());
        if (transaction.getAmount() != null)
            holder.transactionsQuantity.setText(transaction.getAmount() + " " + transaction.getSymbol());
        if (transaction.getCost() != 0f)
            holder.transactionsAmount.setText(String.format("%.2f", transaction.getCost()) + " " + transaction.getCurrency());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView transactionsName;
        TextView transactionsQuantity;
        TextView transactionsAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionsName = itemView.findViewById(R.id.transactionsName);
            transactionsQuantity = itemView.findViewById(R.id.transactionsQuantity);
            transactionsAmount = itemView.findViewById(R.id.transactionsAmount);
        }
    }
}
