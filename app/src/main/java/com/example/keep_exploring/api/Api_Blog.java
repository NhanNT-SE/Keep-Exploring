package com.example.keep_exploring.api;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api_Blog {
    //    PUBLIC API
    @GET("/public/blog")
    Call<String> getBlogList();

    @GET("/public/blog/{idBlog}")
    Call<String> getBlogById(@Path("idBlog") String idBlog);

    @Multipart
    @POST("/post")
    Call<String> createBlog(
            @Header("Authorization") String accessToken,
            @PartMap() HashMap<String, RequestBody> partMap,
            @Part MultipartBody.Part imageBlog
    );

    @Multipart
    @PATCH("/post/{idPost}")
    Call<String> updatePost(
            @Header("Authorization") String accessToken,
            @Path("idPost") String idPost,
            @PartMap() HashMap<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> imageList
    );

    @DELETE("/post/delete/{blogID}")
    Call<String> deleteBlog(
            @Header("Authorization") String accessToken,
            @Path("blogID") String blogID
    );
}
