package com.example.keep_exploring.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface Api_Like {
    @POST("/public/post/like")
    Call<String> getLikeListPost(@Body HashMap<String, String> map);

    @POST("/public/blog/like")
    Call<String> getLikeListBlog(@Body HashMap<String, String> map);

    @PATCH("/post/like")
    Call<String> likePost(
            @Header("Authorization") String accessToken,
            @Body HashMap<String, String> map);

    @PATCH("/blog/like")
    Call<String> likeBlog(
            @Header("Authorization") String accessToken,
            @Body HashMap<String, String> map);
}
