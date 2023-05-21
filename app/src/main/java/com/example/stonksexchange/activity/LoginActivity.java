package com.example.stonksexchange.activity;
import com.example.stonksexchange.App;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.ApiService;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stonksexchange.R;
import com.example.stonksexchange.api.domain.AuthResponse;
import com.example.stonksexchange.api.domain.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App app = App.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        EditText loginInput = findViewById(R.id.loginInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        TextView toSignUpText = findViewById(R.id.toSignUpText);
        Button logInBtn = findViewById(R.id.logInButton);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest loginRequest = new LoginRequest(loginInput.getText().toString(), passwordInput.getText().toString());
                Call<AuthResponse> call = ApiService.apiService.logIn(loginRequest);
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

                            finish();
                        } else {
                            ErrorUtils.handleErrorResponse(response, LoginActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // Handle failure
                        Toast.makeText(LoginActivity.this, "Internal error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}