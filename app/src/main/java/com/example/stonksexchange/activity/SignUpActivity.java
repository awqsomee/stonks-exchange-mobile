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
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.auth.SignUpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    App app;
    Context context;
    SharedPreferences sharedPref;
    EditText fullNameInput;
    EditText emailInput;
    EditText passwordSignUpInput;
    EditText repeatPasswordInput;
    TextView toLoginText;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        app = App.getInstance();

        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordSignUpInput = findViewById(R.id.passwordSignUpInput);
        repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
        toLoginText = findViewById(R.id.toLoginText);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new SignUpClickListener());

        toLoginText.setOnClickListener(new toLoginClickListener());
    }

    private class SignUpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!passwordSignUpInput.getText().toString().equals(repeatPasswordInput.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            SignUpRequest signUpRequest = new SignUpRequest(emailInput.getText().toString(), fullNameInput.getText().toString(), passwordSignUpInput.getText().toString());
            Call<AuthResponse> call = ApiService.ApiService.signUp(signUpRequest);

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
    }

    private class toLoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}