package com.example.stonksexchange.utils;

import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.fragment.CatalogFragment;

public class BackButtonHandler {
    public static void setupBackPressedCallback(Fragment fragment) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToCatalogFragment(fragment);
            }
        };

        fragment.requireActivity().getOnBackPressedDispatcher().addCallback(fragment.getViewLifecycleOwner(), callback);
    }

    private static void navigateToCatalogFragment(Fragment fragment) {
        Menu menu = MainActivity.getNavigationView().getMenu();
        MenuItem menuItem = menu.findItem(R.id.menu_catalog);
        menuItem.setChecked(true);
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, new CatalogFragment());
        transaction.commit();
    }
}
