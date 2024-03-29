package com.example.keep_exploring.DAO;

import android.content.Context;

import com.example.keep_exploring.api.Api_Post;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Post {
    private final Api_Post api_post;
    private Helper_Image helper_image;
    private Context context;

    public DAO_Post(Context context) {
        this.context = context;
        api_post = Retrofit_config.retrofit.create(Api_Post.class);
        helper_image = new Helper_Image();
    }

    public void createPost(String accessToken,HashMap<String, RequestBody> map, List<String> imageSubmitList, Helper_Callback callback) {
        Call<String> call = api_post.createPost(accessToken, map, helper_image.uploadMulti(imageSubmitList, "image_post"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty() ) {
                    JSONObject data = callback.getJsonObject(response);
                    callback.successReq(data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });

    }

    public void deletePost(String accessToken, String postId, Helper_Callback callback) {
        Call<String> call = api_post.deletePost(accessToken, postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty() ) {
                    JSONObject data = callback.getJsonObject(response);
                    callback.successReq(data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });

    }

    public void getPostById(String idPost, Helper_Callback callback) {
        Call<String> call = api_post.getPostById(idPost);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty() ) {
                    JSONObject data = callback.getJsonObject(response);
                    Post post = new Gson().fromJson(data.toString(), Post.class);
                    callback.successReq(post);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });

    }


    public void getPostByCategory(String category, Helper_Callback callback) {
        Call<String> call = api_post.getPostList(category);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                if (err.isEmpty() ) {
                    JSONArray data = callback.getJsonArray(response);
                    List<Post> postList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(postList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });
    }

    public void getPostByQuery(String query, Helper_Callback callback) {
        Call<String> call = api_post.getPostByQuery(query);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                if (err.isEmpty() ) {
                    JSONArray data = callback.getJsonArray(response);
                    List<Post> postList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(postList);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });
    }

    public void getPostByUser(String  accessToken,String idUser, Helper_Callback callback) {
        Call<String> call = api_post.getPostByUser(accessToken, idUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                if (err.isEmpty()) {
                    JSONArray data = callback.getJsonArray(response);
                    List<Post> postList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(postList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }


    public void updatePost(
            String accessToken,
            HashMap<String, RequestBody> map,
            String idPost,
            List<String> imageSubmitList,
            Helper_Callback callback) {
        Call<String> call = api_post.updatePost(accessToken, idPost, map,
                helper_image.uploadMulti(imageSubmitList, "image_post"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty() ){
                    JSONObject data = callback.getJsonObject(response);
                    callback.successReq(data);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });

    }

}