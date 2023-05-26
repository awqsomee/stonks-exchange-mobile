package com.example.stonksexchange.utils;

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
        MainActivity.getNavigationView().setSelectedItemId(R.id.menu_catalog);
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, new CatalogFragment());
        transaction.commit();
    }
}
