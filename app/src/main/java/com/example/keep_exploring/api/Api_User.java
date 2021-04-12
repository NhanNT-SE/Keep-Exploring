package com.example.keep_exploring.api;

import com.example.keep_exploring.model.User;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api_User {

    //GET Method
    @GET("/user")
    Call<String> getMyProfile(
            @Header("Authorization") String accessToken
    );

    //POST Method
    @Multipart
    @POST("/user/signUp")
    Call<String> signUp(
//            @Part MultipartBody.Part image_user,
                        @Part("displayName") RequestBody displayName,
                        @Part("email") RequestBody email,
                        @Part("pass") RequestBody pass
    );

    @POST("/user/signIn")
    Call<String> signIn(@Body User user);
}
