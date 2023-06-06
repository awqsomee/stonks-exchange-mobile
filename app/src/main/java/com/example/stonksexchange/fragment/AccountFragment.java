package com.example.stonksexchange.fragment;

import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stonksexchange.App;
import com.example.stonksexchange.R;
import com.example.stonksexchange.activity.LoginActivity;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceRequest;
import com.example.stonksexchange.api.domain.balance.ChangeBalanceResponse;
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
    private static final int PERMISSION_REQUEST_CODE_2 = 2;
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
    TextView changeAvatarBtn;
    TextView deleteAvatarBtn;

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


        avatar.setOnClickListener(new AccountFragment.AccClickListener());
        deleteAccText.setOnClickListener(new DeleteAccClickListener());

        for (EditText editText : editTextList) {
            editText.setOnEditorActionListener(new FocusChangeListener());
        }
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
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
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
        launcher.launch(intent);
    }

    private void deleteAvatar() {
        Call<UserDataResponse> call = ApiService.AuthApiService.deleteAvatar();
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    UserDataResponse data = response.body();
                    User user = data.getUser();
                    app.setUserData(user);
                    avatar.setImageDrawable(context.getDrawable(R.drawable.authorized_user));
                    MainActivity.getAccAuthButton().setImageDrawable(context.getDrawable(R.drawable.authorized_user));
                } else {
                    ErrorUtils.handleErrorResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                ErrorUtils.failureRequest(context);
            }
        });
    }

    private ActivityResultLauncher<Intent> registerGalleryLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri selectedFileUri = data.getData();
                String filePath = FileUtils.getPathFromUri(requireContext(), selectedFileUri);

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
                                UserDataResponse data = response.body();
                                User user = data.getUser();
                                app.setUserData(user);
                                if (app.getUser().getAvatar() != null) {
                                    RequestCreator avatarImage = Picasso.get().load("https://stonks-kaivr.amvera.io/" + app.getUser().getAvatar());
                                    avatarImage.into(avatar);
                                    avatarImage.into(MainActivity.getAccAuthButton());
                                }
                            } else {
                                ErrorUtils.handleErrorResponse(response, context);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {
                            ErrorUtils.failureRequest(context);
                        }
                    });
                }
            }
        });
    }

    private class DeleteAccClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Call<ChangeBalanceResponse> callWithdraw = ApiService.AuthApiService.changeBalance(new ChangeBalanceRequest(-app.getUser().getBalance()));
            callWithdraw.enqueue(new Callback<ChangeBalanceResponse>() {
                @Override
                public void onResponse(Call<ChangeBalanceResponse> call, Response<ChangeBalanceResponse> response) {
                    if (response.isSuccessful()) {
                        ChangeBalanceResponse data = response.body();
                        app.getUser().setBalance(data.getUser().getBalance());
                        app.pushTransaction(data.getTransaction());

                        Call<UserDataResponse> callDelete = ApiService.AuthApiService.deleteUser();
                        callDelete.enqueue(new Callback<UserDataResponse>() {
                            @Override
                            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                                if (response.isSuccessful()) {
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.logOut();
                                } else {
                                    ErrorUtils.handleErrorResponse(response, context);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                                ErrorUtils.failureRequest(context);
                            }
                        });
                    } else {
                        ErrorUtils.handleErrorResponse(response, context);
                    }
                }

                @Override
                public void onFailure(Call<ChangeBalanceResponse> call, Throwable t) {
                    ErrorUtils.failureRequest(context);

                }
            });

        }
    }

    private class FocusChangeListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                User user = new User();
                user.setUsername(usernameInput.getText().toString());
                String lastName = editLastname.getText().toString();
                String firstName = editFirstname.getText().toString();
                String patronymic = editPatronymic.getText().toString();
                if (lastName.equals("")) return false;
                if (firstName.equals("")) return false;
                if (patronymic.equals("")) return false;
                String fullName = lastName + " " + firstName + " " + patronymic;
                user.setName(fullName);
                user.setEmail(editEmail.getText().toString());
                user.setPhoneNumber(editPhone.getText().toString());
                user.setPassportNumber(editPassport.getText().toString());
                user.setBirthday(editBirthday.getText().toString());

                Call<UserDataResponse> call = ApiService.AuthApiService.changeUserData(user);
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
                            Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            ErrorUtils.handleErrorResponse(response, context);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDataResponse> call, Throwable t) {
                        ErrorUtils.failureRequest(context);
                    }
                });
                // Closing the keyboard
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Unfocusing the EditText
                v.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class AccClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(context, avatar);
            popupMenu.getMenuInflater().inflate(R.menu.popup_edit_avatar, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.changeAvatarBtn) {
                        String readImagePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
                        if (ContextCompat.checkSelfPermission(context, readImagePermission) == PackageManager.PERMISSION_GRANTED) {
                            uploadAvatar();
                        } else {
                            Toast.makeText(context, "Нужно разрешение", Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                            else {
                                MainActivity mainActivity = (MainActivity) getActivity();
                                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_2);
                            }
                        }
                        return true;
                    } else if (itemId == R.id.deleteAvatarBtn) {
                        deleteAvatar();
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_2) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
}
