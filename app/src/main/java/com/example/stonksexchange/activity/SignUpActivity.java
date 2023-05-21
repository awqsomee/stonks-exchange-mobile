package com.example.stonksexchange.activity;

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

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.AuthResponse;
import com.example.stonksexchange.api.domain.LoginRequest;
import com.example.stonksexchange.api.domain.SignUpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App app = App.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        EditText fullNameInput = findViewById(R.id.fullNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordSignUpInput = findViewById(R.id.passwordSignUpInput);
        EditText repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
        TextView toLoginText = findViewById(R.id.toLoginText);
        Button signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordSignUpInput.getText().toString().equals(repeatPasswordInput.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignUpRequest signUpRequest = new SignUpRequest(emailInput.getText().toString(), fullNameInput.getText().toString(), passwordSignUpInput.getText().toString());
                Call<AuthResponse> call = ApiService.apiService.signUp(signUpRequest);

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
                            ErrorUtils.handleErrorResponse(response, SignUpActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Внутренняя ошибка", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}