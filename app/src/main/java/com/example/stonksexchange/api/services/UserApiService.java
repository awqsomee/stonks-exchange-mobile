package com.example.stonksexchange.api.services;

import com.example.stonksexchange.api.domain.user.ChangeUserDataRequest;
import com.example.stonksexchange.api.domain.user.UserDataResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserApiService {
    @GET("auth/user")
    Call<UserDataResponse> getUserData();

    @PUT("auth/user")
    Call<UserDataResponse> changeUserData(@Body ChangeUserDataRequest request);

    @DELETE("auth/user/")
    Call<UserDataResponse> deleteUser();

    @Multipart
    @POST("auth/user/avatar")
    Call<UserDataResponse> uploadAvatar(@Part MultipartBody.Part image);

    @DELETE("auth/user/avatar/")
    Call<UserDataResponse> deleteAvatar();
}
