package com.example.stonksexchange.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiManager;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.stock.FindStocksResponse;
import com.example.stonksexchange.api.domain.stock.GetStockDataResponse;
import com.example.stonksexchange.fragment.AccountFragment;
import com.example.stonksexchange.fragment.CatalogFragment;
import com.example.stonksexchange.fragment.InvestmentsFragment;
import com.example.stonksexchange.fragment.WalletFragment;
import com.example.stonksexchange.utils.StockAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Console;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    private MenuItem walletCount;
    App app;
    Context context;
    SharedPreferences sharedPref;
    ImageButton accBtn;
    BottomNavigationView navigationView;
    SearchView searchView;
    CatalogFragment catalogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        ApiManager.setToken(sharedPref.getString("token", null));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = App.getInstance();
        accBtn = findViewById(R.id.accButton);
        navigationView = findViewById(R.id.navigationView);
        searchView = findViewById(R.id.searchView);

        accBtn.setOnClickListener(new AccClickListener());

        navigationView.setOnItemSelectedListener(this);
        searchView.setOnQueryTextListener(new SearchSubmitListener());

        catalogFragment = new CatalogFragment();
        showFragment(catalogFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_catalog:
                if (navigationView.getSelectedItemId() == R.id.menu_catalog) {
                    app.setDisplayedStocks(new ArrayList<>());
                    catalogFragment.clearUI();
                }
                showFragment(catalogFragment);
                return true;
            case R.id.menu_investments:
                showFragment(new InvestmentsFragment());
                return true;
            case R.id.menu_wallet:
                showFragment(new WalletFragment());
                return true;
            case R.id.menu_dummy:
                showFragment(new AccountFragment());
                return true;
            default:
                return false;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAuth();
    }

    private void getAuth() {
        Call<AuthResponse> call = ApiService.AuthApiService.auth();
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", authResponse.getToken());
                    editor.apply();

                    ApiManager.setToken(authResponse.getToken());

                    app.setUser(authResponse.getUser());
                    app.setIsAuth(true);
                    navigationView.setVisibility(View.VISIBLE);
                } else {
//                    ErrorUtils.handleErrorResponse(response, MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class AccClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (app.getIsAuth()) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, accBtn);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                walletCount = popupMenu.getMenu().findItem(R.id.balanceBtn);
                walletCount.setTitle(String.format("%.2f", app.getUser().getBalance()) + " ₽");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.balanceBtn:
                                showFragment(new WalletFragment());
                                navigationView.setSelectedItemId(R.id.menu_wallet);
                                return true;
                            case R.id.accountBtn:
                                showFragment(new AccountFragment());
                                navigationView.setSelectedItemId(R.id.menu_dummy);
                                return true;
                            case R.id.infoBtn:
                                // TODO: Ссылка на инфо
                                return true;
                            case R.id.logOutBtn:
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove("token");
                                editor.apply();
                                ApiManager.setToken(null);
                                app.setIsAuth(false);
                                app.setUser(null);
                                navigationView.setVisibility(View.GONE);
                                showFragment(new CatalogFragment());
                                navigationView.setSelectedItemId(R.id.menu_catalog);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private class SearchSubmitListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            catalogFragment.setLoading(true);
            
            Call<FindStocksResponse> call = ApiService.ApiService.findStock(searchView.getQuery().toString());
            call.enqueue(new Callback<FindStocksResponse>() {
                @Override
                public void onResponse(Call<FindStocksResponse> call, Response<FindStocksResponse> response) {
                    if (response.isSuccessful()) {
                        FindStocksResponse data = response.body();
                        app.setDisplayedStocks(data.getStocks());
                        catalogFragment.updateUI();
                    } else {
                        ErrorUtils.handleErrorResponse(response, context);
                    }
                    catalogFragment.setLoading(false);
                }

                @Override
                public void onFailure(Call<FindStocksResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    catalogFragment.setLoading(false);
                }
            });

            searchView.onActionViewCollapsed();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}