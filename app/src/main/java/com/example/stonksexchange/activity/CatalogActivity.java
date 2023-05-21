package com.example.stonksexchange.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.AuthRequest;
import com.example.stonksexchange.api.domain.AuthResponse;
import com.example.stonksexchange.api.domain.LoginRequest;
import com.example.stonksexchange.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App app = App.getInstance();
        Context context = this;
        checkAuth(context, app);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ImageButton login = findViewById(R.id.toLoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAuth(Context context, App app) {
        SharedPreferences sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
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

                    TextView tw = findViewById(R.id.textView);
                    tw.setText(authResponse.getUser().getUsername());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
            }
        });
    }
}