package com.example.keep_exploring.api;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api_Auth {
    @POST("/auth/sign-in")
    Call<String> signIn(@Body HashMap<String, String> map);
    @POST("/auth/sign-out")
    Call<String> signOut(@Body HashMap<String, String> map);
    @Multipart
    @POST("/auth/sign-up")
    Call<String> signUp(
            @Part("email") RequestBody username,
            @Part("pass") RequestBody password,
            @Part("displayName") RequestBody displayName,
            @Part MultipartBody.Part single);
    @POST("/auth/refresh-token")
    Call<String> refreshToken(@Body HashMap<String, String> map);
}
