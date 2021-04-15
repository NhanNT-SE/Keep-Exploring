package com.example.keep_exploring.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api_Comment {

@GET("/public/post/comments/{idPost}")
    Call<String> getCommentPost(@Path("idPost") String idPost);

}
