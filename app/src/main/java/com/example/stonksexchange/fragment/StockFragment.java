package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.utils.BackButtonHandler;

public class StockFragment extends Fragment {
    App app;
    Context context;

    public StockFragment() {
    }

    public static StockFragment newInstance(String symbol) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);
        String symbol = getArguments().getString("symbol");

        TextView textView = view.findViewById(R.id.textView2);
        textView.setText(symbol);

        return view;
    }
}