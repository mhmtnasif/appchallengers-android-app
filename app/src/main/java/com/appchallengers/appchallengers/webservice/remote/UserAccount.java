package com.appchallengers.appchallengers.webservice.remote;


import com.appchallengers.appchallengers.webservice.request.SignUpRequestModel;
import com.appchallengers.appchallengers.webservice.request.UpDateRequestModel;
import com.appchallengers.appchallengers.webservice.request.UsersLoginRequestModel;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.appchallengers.appchallengers.webservice.response.UserPreferencesData;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.Call;


public interface UserAccount {

    @Multipart
    @POST("users/account/create/with_image")
    Call<UserPreferencesData> createAccountWithImage(
            @Part("fullName") RequestBody fullName,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("country") RequestBody country,
            @Part MultipartBody.Part image);

    @POST("users/account/create/without_image")
    Observable<Response<UserPreferencesData>> createAccountWithoutImage(@Body SignUpRequestModel signUpRequestModel);


    @POST("users/account/login")
    Observable<Response<UserPreferencesData>> usersLogin(@Body UsersLoginRequestModel usersLoginRequestModel);


    @GET("users/account/send_email")
    Observable<Response<Void>> userResendConfirmEmail();


    @GET("users/account/check_confirm_email")
    Observable<Response<Void>> checkConfrimEmail();

    @Multipart
    @PUT("users/account/update_profile_with_photo")
    Call<UserBaseDataModel> upDateAccountWithImage(
            @Part("fullName") RequestBody fullName,
            @Part("country") RequestBody country,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @PUT("users/account/update_profile_without_photo")
    Observable<Response<Void>> upDateAccountWithoutImage(@Field("fullName") String fullName,@Field("country") String country);

    @FormUrlEncoded
    @PUT("users/account/change_password")
    Observable<Response<Void>> changePassword(@Field("oldPassword") String oldPassword,@Field("newPassword") String newPassWord);

    @FormUrlEncoded
    @PUT("users/account/update_email")
    Observable<Response<Void>> changeEmail(@Field("oldEmail") String oldEmail, @Field("newEmail") String newEmail);

    @PUT("users/account/delete_profile_photo")
    Observable<Response<Void>> deleteAccountImage();


}
