package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.stonksexchange.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.stonksexchange.models.Stock;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private List<Stock> stocks;
    Transliterator transliterator = null;

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
        holder.stockChange.setText(stock.getChange() + "%");
        if (stock.getChange().charAt(0) != '-'){
            holder.stockChange.setTextColor(Color.parseColor("#BBFFA7"));
        } else if (stock.getChange().charAt(0) == '0') {
            holder.stockChange.setTextColor(Color.parseColor("#E9EEF2"));
        } else{
            holder.stockChange.setTextColor(Color.parseColor("#FF2A51"));
        }

        transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        String iconUrl = "https://cdn.bcs.ru/company-logos/" + transliterator.transliterate(stock.getShortname().split(" ")[0].toLowerCase()) + ".svg";
        Context context = holder.imageView.getContext().getApplicationContext();
        FetchSvgTask task = new FetchSvgTask(iconUrl, holder.imageView, context);
        task.execute();

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
