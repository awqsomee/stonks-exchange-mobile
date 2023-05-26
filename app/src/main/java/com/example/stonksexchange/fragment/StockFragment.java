package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;

public class StockFragment extends Fragment {
    App app;
    Context context;

    public StockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        app = App.getInstance();
        context = view.getContext();

        return view;
    }
}