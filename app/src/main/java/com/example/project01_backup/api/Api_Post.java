package com.example.project01_backup.api;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api_Post {
    //    PUBLIC API
    @GET("/public/post")
    Call<String> getPostList(@Query("category") String category);
    @GET("/public/post/{idPost}")
    Call<String> getPostById(@Path("idPost") String idPost);

    @Multipart
    @POST("/post")
    Call<String> createPost(
            @Header("Authorization") String accessToken,
            @PartMap() HashMap<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> imageList
    );
}