package com.example.project01_backup.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api_Post {
    //    PUBLIC API
    @GET("/public/post")
    Call<String> getPostList(@Query("category") String category);
}
