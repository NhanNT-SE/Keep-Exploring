package com.example.keep_exploring.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api_Comment {

    @GET("/public/post/comments/{idPost}")
    Call<String> getCommentPost(@Path("idPost") String idPost);

    @GET("/public/blog/comments/{idBlog}")
    Call<String> getCommentBlog(@Path("idBlog") String idBlog);
    @Multipart
    @POST("/comment/post")
    Call<String> addCommentPost(@Header("Authorization") String accessToken,
                                @Part("content") RequestBody content,
                                @Part("idPost") RequestBody idPost,
                                @Part MultipartBody.Part image_post);
    @Multipart
    @POST("/comment/blog")
    Call<String> addCommentBlog(@Header("Authorization") String accessToken,
                                @Part("content") RequestBody content,
                                @Part("idBlog") RequestBody idBlog,
                                @Part MultipartBody.Part image_blog);

    @DELETE("/comment/delete/{idComment}")
    Call<String> deleteComment(@Header("Authorization") String accessToken,
                               @Path("idComment") String idComment);

}
