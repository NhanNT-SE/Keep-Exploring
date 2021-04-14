package com.example.keep_exploring.api;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Api_User {

    //GET Method
    @GET("/user/{idUser}")
    Call<String> getProfile(
            @Header("Authorization") String accessToken,
            @Path("idUser") String idUser
    );

    @Multipart
    @PATCH("/user")
    Call<String> updateProfile(
            @Header("Authorization") String accessToken,
            @PartMap() HashMap<String, RequestBody> partMap,
            @Part MultipartBody.Part imgUser
    );

    @PATCH("/user/changePass")
    Call<String> changePassword(
            @Header("Authorization") String accessToken,
            @Body HashMap<String, String> partMap
    );
}
