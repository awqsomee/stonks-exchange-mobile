package com.example.stonksexchange.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.user.UserDataResponse;
import com.example.stonksexchange.models.User;
import com.example.stonksexchange.utils.BackButtonHandler;
import com.example.stonksexchange.utils.FileUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;
    App app;
    Context context;
    SharedPreferences sharedPref;
    ActivityResultLauncher<Intent> launcher;
    EditText usernameInput;
    EditText editLastname;
    EditText editFirstname;
    EditText editPatronymic;
    EditText editEmail;
    EditText editBirthday;
    EditText editPhone;
    EditText editPassport;
    TextView deleteAccText;
    ImageView avatar;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        app = App.getInstance();
        context = view.getContext();
        sharedPref = context.getSharedPreferences("stonks_exchange", Context.MODE_PRIVATE);
        BackButtonHandler.setupBackPressedCallback(this);

        launcher = registerGalleryLauncher();

        List<EditText> editTextList = new ArrayList<>();
        usernameInput = view.findViewById(R.id.usernameInput);
        editLastname = view.findViewById(R.id.editLastname);
        editFirstname = view.findViewById(R.id.editFirstname);
        editPatronymic = view.findViewById(R.id.editPatronymic);
        editEmail = view.findViewById(R.id.editEmail);
        editBirthday = view.findViewById(R.id.editBirthday);
        editPhone = view.findViewById(R.id.editPhone);
        editPassport = view.findViewById(R.id.editPassport);
        deleteAccText = view.findViewById(R.id.deleteAccText);
        avatar = view.findViewById(R.id.avatar);
        editTextList.add(usernameInput);
        editTextList.add(editLastname);
        editTextList.add(editFirstname);
        editTextList.add(editPatronymic);
        editTextList.add(editEmail);
        editTextList.add(editBirthday);
        editTextList.add(editPhone);
        editTextList.add(editPassport);

        getAccountData();

        avatar.setOnClickListener(new AvatarClickListener());
        deleteAccText.setOnClickListener(new DeleteAccClickListener());

        return view;
    }

    private void getAccountData() {
        if (app.getUser().getAvatar() != null) {
            RequestCreator avatarImage = Picasso.get().load("https://stonks-kaivr.amvera.io/" + app.getUser().getAvatar());
            avatarImage.into(avatar);
            avatarImage.into(MainActivity.getAccAuthButton());
        }
        Call<UserDataResponse> call = ApiService.AuthApiService.getUserData();
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    UserDataResponse data = response.body();
                    User user = data.getUser();
                    app.setUserData(user);
                    usernameInput.setText(user.getUsername());
                    String[] fullName = user.getName().split(" ");
                    editLastname.setText(fullName[0]);
                    if (fullName.length > 1) editFirstname.setText(fullName[1]);
                    if (fullName.length > 2) editPatronymic.setText(fullName[2]);
                    editEmail.setText(user.getEmail());
                    editBirthday.setText(stringToDate(user.getBirthday()));
                    editPhone.setText(user.getPhoneNumber());
                    editPassport.setText(user.getPassportNumber());
                } else {
//                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String stringToDate(String inputDate) {
        try {
            if (inputDate == null) return "";
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
            String outputDate = outputFormat.format(date);

            return outputDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void uploadAvatar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("*/*");  // Set the MIME type to filter file types if needed
        System.out.println("AAS Y");
        launcher.launch(intent);
    }

    private ActivityResultLauncher<Intent> registerGalleryLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            System.out.println("AAS 1");
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri selectedFileUri = data.getData();
                String filePath = FileUtils.getPathFromUri(requireContext(), selectedFileUri);


                System.out.println("AAS 2");
                if (filePath != null) {
                    // Step 4: Create a RequestBody from the selected file
                    File file = new File(filePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                    // Step 5: Make a request to the server using Retrofit
                    Call<UserDataResponse> call = ApiService.AuthApiService.uploadAvatar(filePart);
                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                            if (response.isSuccessful()) {
                                System.out.println("AAS Pic");
                                UserDataResponse data = response.body();
                                User user = data.getUser();
                                app.setUserData(user);
                                if (app.getUser().getAvatar() != null){
                                    RequestCreator avatarImage = Picasso.get().load("https://stonks-kaivr.amvera.io/" + app.getUser().getAvatar());
                                    avatarImage.into(avatar);
                                    avatarImage.into(MainActivity.getAccAuthButton());
                                }
                            } else {
                                System.out.println("AAS nePic");
                                ErrorUtils.handleErrorResponse(response, context);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {
                            System.out.println("AAS err");
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else System.out.println("AAS 44");
            }
        });
    }

    private class DeleteAccClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Call<UserDataResponse> call = ApiService.AuthApiService.deleteUser();
            call.enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    if (response.isSuccessful()) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.logOut();
                    } else {
//                    ErrorUtils.handleErrorResponse(response, context);
                    }
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
