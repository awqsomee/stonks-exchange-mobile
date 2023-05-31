package com.example.stonksexchange.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiManager;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.stock.FindStocksResponse;
import com.example.stonksexchange.fragment.AccountFragment;
import com.example.stonksexchange.fragment.CatalogFragment;
import com.example.stonksexchange.fragment.InvestmentsFragment;
import com.example.stonksexchange.fragment.WalletFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    App app;
    Context context;
    SharedPreferences sharedPref;
    Button accBtn;
    static BottomNavigationView navigationView;
    SearchView searchView;
    CatalogFragment catalogFragment;
    static ImageView accAuthButton;
    private MenuItem walletCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        ApiManager.setToken(sharedPref.getString("token", null));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = App.getInstance();
        accBtn = findViewById(R.id.accButton);
        accAuthButton = findViewById(R.id.accAuthButton);
        navigationView = findViewById(R.id.navigationView);
        searchView = findViewById(R.id.searchView);

        accBtn.setOnClickListener(new AccClickListener());
        accAuthButton.setOnClickListener(new AccClickListener());

        navigationView.setOnItemSelectedListener(this);
        searchView.setOnQueryTextListener(new SearchSubmitListener());
        searchView.setOnClickListener(new SearchClickListener());

        catalogFragment = new CatalogFragment();
        showFragment(catalogFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_catalog:
                if (navigationView.getSelectedItemId() == R.id.menu_catalog) {
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
                    if (authResponse.getUser().getAvatar() != null)
                        Picasso.get().load("https://stonks-kaivr.amvera.io/" + authResponse.getUser().getAvatar()).into(accAuthButton);
                    app.setIsAuth(true);
                    navigationView.setVisibility(View.VISIBLE);

                    changePopupMenuIcon();
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

    public static BottomNavigationView getNavigationView() {
        return navigationView;
    }
    public void changePopupMenuIcon() {
        Button accButton = findViewById(R.id.accButton);
        ImageView accAuthButton = findViewById(R.id.accAuthButton);
        if(app.getIsAuth()) {
            accButton.setVisibility(View.GONE);
            accAuthButton.setVisibility(View.VISIBLE);
        }else{
            accButton.setVisibility(View.VISIBLE);
            accAuthButton.setVisibility(View.GONE);
        }
    }

    public static ImageView getAccAuthButton() {
        return accAuthButton;
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
                                logOut();
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
            int searchCloseButtonId = searchView.getContext().getResources()
                    .getIdentifier("android:id/search_close_btn", null, null);
            ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);
            if (newText.equals(""))
                closeButton.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private class SearchClickListener implements View.OnClickListener {
        int searchCloseButtonId = searchView.getContext().getResources()
            .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);

        @Override
        public void onClick(View v) {
            searchView.setIconified(false);
            searchView.onActionViewExpanded();
            searchView.setFocusable(true);
            closeButton.setVisibility(View.VISIBLE);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    searchView.onActionViewCollapsed();
                }
            });
        }

    }

    public void logOut() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.apply();
        ApiManager.setToken(null);
        app.setIsAuth(false);
        app.setUser(null);
        navigationView.setVisibility(View.GONE);
        changePopupMenuIcon();
        showFragment(new CatalogFragment());
        navigationView.setSelectedItemId(R.id.menu_catalog);
    }
}