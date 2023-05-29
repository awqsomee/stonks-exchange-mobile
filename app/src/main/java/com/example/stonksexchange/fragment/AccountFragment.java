package com.example.stonksexchange.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.user.GetUserDataResponse;
import com.example.stonksexchange.models.User;
import com.example.stonksexchange.utils.BackButtonHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    App app;
    Context context;

    EditText usernameInput;
    EditText editLastname;
    EditText editName;
    EditText editPatronymic;
    EditText editEmail;
    EditText editBirthday;
    EditText editPhone;
    EditText editPassport;


    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        app = App.getInstance();
        context = view.getContext();
        BackButtonHandler.setupBackPressedCallback(this);

        usernameInput = view.findViewById(R.id.usernameInput);
        editLastname = view.findViewById(R.id.editLastname);
        editName = view.findViewById(R.id.editFirstname);
        editPatronymic = view.findViewById(R.id.editPatronymic);
        editEmail = view.findViewById(R.id.editEmail);
        editBirthday = view.findViewById(R.id.editBirthday);
        editPhone = view.findViewById(R.id.editPhone);
        editPassport = view.findViewById(R.id.editPassport);

        getAccountData();

        return view;
    }

    private void getAccountData() {
        Call<GetUserDataResponse> call = ApiService.AuthApiService.getUserData();
        call.enqueue(new Callback<GetUserDataResponse>() {
            @Override
            public void onResponse(Call<GetUserDataResponse> call, Response<GetUserDataResponse> response) {
                if (response.isSuccessful()) {
                    GetUserDataResponse data = response.body();
                    User user = data.getUser();
                    app.setUser(user);
                    usernameInput.setText(user.getUsername());
                    String[] fullName = user.getName().split(" ");
                    editLastname.setText(fullName[0]);
                    if (fullName.length > 1)
                        editName.setText(fullName[1]);
                    if (fullName.length > 2)
                        editPatronymic.setText(fullName[2]);
                    editEmail.setText(user.getEmail());
                    editBirthday.setText(user.getBirthday());
                    editPhone.setText(user.getPhoneNumber());
                    editPassport.setText(user.getPassportNumber());
                } else {
//                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<GetUserDataResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
