package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.fragment.StockFragment;
import com.example.stonksexchange.models.Stock;

import java.util.List;

public class StockAdapter<T extends Stock> extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    Transliterator transliterator = null;
    protected List<T> stocks;
    protected Fragment fragment;

    public StockAdapter(List<T> stocks, Fragment fragment) {
        this.stocks = stocks;
        this.fragment = fragment;
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
        holder.stockChange.setText(String.format("%.2f", stock.getChange()) + "%");
        if (stock.getChange() > 0)
            holder.stockChange.setTextColor(Color.parseColor("#BBFFA7"));
        if (stock.getChange() < 0)
            holder.stockChange.setTextColor(Color.parseColor("#FF2A51"));
        if (stock.getChange() == 0)
            holder.stockChange.setTextColor(Color.parseColor("#E9EEF2"));

        String iconUrl;

        if (!stock.getLatname().matches("\\d")){
            iconUrl = "https://cdn.bcs.ru/company-logos/" + stock.getLatname().toLowerCase() + ".svg";
        } else {
            transliterator = Transliterator.getInstance("Russian-Latin/BGN");
            iconUrl = "https://cdn.bcs.ru/company-logos/" + transliterator.transliterate(stock.getShortname().split("[ -]")[0].toLowerCase()) + ".svg";
        }
        Context context = holder.imageView.getContext().getApplicationContext();
        FetchSvgTask task = new FetchSvgTask(iconUrl, holder.imageView, context);
        task.execute();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = fragment.getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, StockFragment.newInstance(stock.getSymbol(), stock.getName()));
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockShortname;
        TextView stockSymbol;
        TextView stockPrice;
        TextView stockChange;
        ImageView imageView;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockShortname = itemView.findViewById(R.id.stockShortname);
            stockSymbol = itemView.findViewById(R.id.stockSymbol);
            stockPrice = itemView.findViewById(R.id.stockPrice);
            stockChange = itemView.findViewById(R.id.stockChange);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
