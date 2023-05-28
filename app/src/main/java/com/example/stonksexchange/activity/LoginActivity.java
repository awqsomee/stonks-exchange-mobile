package com.example.stonksexchange.activity;
import com.example.stonksexchange.App;
import com.example.stonksexchange.api.ApiManager;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.ApiService;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stonksexchange.R;
import com.example.stonksexchange.api.domain.auth.AuthResponse;
import com.example.stonksexchange.api.domain.auth.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Context context;
    App app;
    EditText loginInput;
    EditText passwordInput;
    TextView toSignUpText;
    Button logInBtn;
    ImageButton goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = App.getInstance();
        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        toSignUpText = findViewById(R.id.toSignUpText);
        logInBtn = findViewById(R.id.logInButton);
        goBackBtn = findViewById(R.id.goBackBtn);

        logInBtn.setOnClickListener(new logInClickListener());
        goBackBtn.setOnClickListener(new goBackClickListener());
        toSignUpText.setOnClickListener(new toSignUpClickListener());

        passwordInput.setOnEditorActionListener(new sendFormETListener());
    }

    private class logInClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            attemptLogin();
        }
    }

    private class toSignUpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class goBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class sendFormETListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin();
                return true;
            }
            return false;
        }
    }

    private void attemptLogin() {
        LoginRequest loginRequest = new LoginRequest(loginInput.getText().toString(), passwordInput.getText().toString());
        Call<AuthResponse> call = ApiService.ApiService.logIn(loginRequest);
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

                    finish();
                } else {
                    ErrorUtils.handleErrorResponse(response, LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Внутренняя ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}