package com.example.stonksexchange.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stonksexchange.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText fullNameInput = findViewById(R.id.fullNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordSignUpInput = findViewById(R.id.passwordSignUpInput);
        EditText repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
        TextView toLoginText = findViewById(R.id.toLoginText);
        Button signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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