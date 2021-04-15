package com.example.keep_exploring.api;

import com.example.keep_exploring.model.Blog_Details;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GET("/blog/{idUser}")
    Call<String> getBlogByUser(
            @Header("Authorization") String accessToken,
            @Path("idUser") String idUser);
    @Multipart
    @POST("/blog/add")
    Call<String> createBlog(
            @Header("Authorization") String accessToken,
            @Part("title") RequestBody titleBlog,
            @Part("folder_storage") RequestBody folder_storage,
            @Part MultipartBody.Part imageBlog,
            @Part("detail_list") List<Blog_Details> contentList
    );

    @Multipart
    @PATCH("/blog/update/{idBlog}")
    Call<String> updateBlog(
            @Header("Authorization") String accessToken,
            @Path("idBlog") String idBlog,
            @Part("title") RequestBody titleBlog,
            @Part("created_on") RequestBody created_on,
            @Part MultipartBody.Part imageBlog,
            @Part("detail_list") List<Blog_Details> contentList
    );

    @DELETE("/blog/delete/{idBlog}")
    Call<String> deleteBlog(
            @Header("Authorization") String accessToken,
            @Path("idBlog") String idBlog
    );
}
