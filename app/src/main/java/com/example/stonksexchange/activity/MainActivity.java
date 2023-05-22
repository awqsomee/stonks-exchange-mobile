package com.example.stonksexchange.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.domain.auth.AuthRequest;
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.fragment.CatalogFragment;
import com.example.stonksexchange.fragment.InvestmentsFragment;
import com.example.stonksexchange.fragment.WalletFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = App.getInstance();
        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        accBtn = findViewById(R.id.accButton);
        navigationView = findViewById(R.id.navigationView);
        accBtn.setOnClickListener(new AccClickListener());

        navigationView.setOnItemSelectedListener(this);

        // Show the initial fragment
        showFragment(new CatalogFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_catalog:
                showFragment(new CatalogFragment());
                return true;
            case R.id.menu_investments:
                showFragment(new InvestmentsFragment());
                return true;
            case R.id.menu_wallet:
                showFragment(new WalletFragment());
                return true;
            default:
                return false;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAuth();
    }

    private void getAuth() {
        AuthRequest authRequest = new AuthRequest(sharedPref.getString("token", null));
        Call<AuthResponse> call = ApiService.ApiService.auth(authRequest.getToken());
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", authResponse.getToken());
                    editor.apply();

                    app.setUser(authResponse.getUser());
                    app.setIsAuth(true);
                    navigationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
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
                walletCount.setTitle(app.getUser().getBalance() + " ₽");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.balanceBtn:
                                // TODO: Должен отображать текущий баланс и быть ссылкой на кошелек
                                return true;
                            case R.id.accountBtn:
                                // TODO: Ссылка на аккаунт
                                return true;
                            case R.id.infoBtn:
                                // TODO: Ссылка на инфо
                                return true;
                            case R.id.logOutBtn:
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove("token");
                                editor.apply();
                                app.setIsAuth(false);
                                app.setUser(null);
                                navigationView.setVisibility(View.GONE);
                                showFragment(new CatalogFragment());
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
}