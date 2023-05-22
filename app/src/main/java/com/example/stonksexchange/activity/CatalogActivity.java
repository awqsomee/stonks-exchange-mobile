package com.example.stonksexchange.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.domain.AuthRequest;
import com.example.stonksexchange.api.domain.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {
    private MenuItem walletCount;
    private TextView tw;
    App app;
    Context context;
    SharedPreferences sharedPref;
    ImageButton accBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        app = App.getInstance();
        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        tw = findViewById(R.id.textView);
        accBtn = findViewById(R.id.accButton);

        accBtn.setOnClickListener(new AccClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAuth();
    }

    private void getAuth() {
        AuthRequest authRequest = new AuthRequest(sharedPref.getString("token", null));
        Call<AuthResponse> call = ApiService.apiService.auth(authRequest.getToken());
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

                    tw.setText(authResponse.getUser().getUsername());
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
                PopupMenu popupMenu = new PopupMenu(CatalogActivity.this, accBtn);
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
                                tw.setText("nope");
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