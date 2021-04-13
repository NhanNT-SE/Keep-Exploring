package com.example.keep_exploring.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Api_User {

    //GET Method
    @GET("/user/{idUser}")
    Call<String> getProfile(
            @Header("Authorization") String accessToken,
            @Path("idUser") String idUser
    );
}
