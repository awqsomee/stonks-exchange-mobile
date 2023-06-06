package com.example.stonksexchange.activity;

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

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiManager;
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
    EditText firstNameInput;
    EditText lastNameInput;
    EditText patronymicInput;
    EditText emailInput;
    EditText passwordSignUpInput;
    EditText repeatPasswordInput;
    TextView toLoginText;
    Button signUpBtn;
    ImageButton goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        app = App.getInstance();

        context = this;
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        patronymicInput = findViewById(R.id.patronymicInput);
        emailInput = findViewById(R.id.emailInput);
        passwordSignUpInput = findViewById(R.id.passwordSignUpInput);
        repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
        toLoginText = findViewById(R.id.toLoginText);
        signUpBtn = findViewById(R.id.signUpBtn);
        goBackBtn = findViewById(R.id.goBackBtn);

        signUpBtn.setOnClickListener(new SignUpClickListener());
        goBackBtn.setOnClickListener(new goBackClickListener());
        toLoginText.setOnClickListener(new toLoginClickListener());

        repeatPasswordInput.setOnEditorActionListener(new sendFormETListener());
    }

    private class SignUpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            attemptSignUp();
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
                attemptSignUp();
                return true;
            }
            return false;
        }
    }

    private void attemptSignUp() {
        if (!passwordSignUpInput.getText().toString().equals(repeatPasswordInput.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        String lastName = lastNameInput.getText().toString();
        String firstName = firstNameInput.getText().toString();
        String patronymic = patronymicInput.getText().toString();
        if (lastName.equals("")) return;
        if (firstName.equals("")) return;
        if (patronymic.equals("")) return;
        String fullName = lastName + " " + firstName + " " + patronymic;

        SignUpRequest signUpRequest = new SignUpRequest(emailInput.getText().toString(), fullName, passwordSignUpInput.getText().toString());
        Call<AuthResponse> call = ApiService.ApiService.signUp(signUpRequest);

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
                    ErrorUtils.handleErrorResponse(response, SignUpActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
            }
        });
    }
}