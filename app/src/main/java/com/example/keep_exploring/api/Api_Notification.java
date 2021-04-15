package com.example.keep_exploring.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Api_Notification {

    //GET Method
    @GET("/notification")
    Call<String> getAll(@Header("Authorization") String accessToken);
}
