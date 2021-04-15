package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.api.Api_Post;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
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
    private final Helper_SP helper_sp;
    private Helper_Image helper_image;
    private Context context;
    private String accessToken;
    public DAO_Post(Context context) {
        this.context = context;
        api_post = Retrofit_config.retrofit.create(Api_Post.class);
        helper_sp = new Helper_SP(context);
        helper_image = new Helper_Image();
        accessToken = helper_sp.getAccessToken();
    }
    public void createPost(HashMap<String, RequestBody> map, List<String> imageSubmitList, Helper_Callback callback) {
        Call<String> call = api_post.createPost(accessToken, map, helper_image.uploadMulti(imageSubmitList, "image_post"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                callback.successReq(data);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });

    }

    public void deletePost(String postId, Helper_Callback callback) {
        Call<String> call = api_post.deletePost(accessToken, postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                callback.successReq(data);
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
                JSONObject jsonData = callback.getJsonObject(response);
                Post post = new Gson().fromJson(jsonData.toString(), Post.class);
                callback.successReq(post);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });

    }

    public void getLikeByPost(String idPost, Helper_Callback callback){
Call<String> call = api_post.getLikeByPost(idPost);
call.enqueue(new Callback<String>() {
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        try {
            if (response.errorBody() != null) {
                JSONObject err = new JSONObject(response.errorBody().string());
                log(err.getString("error"));
                String msg = err.getString("message");
                callback.failedReq(msg);
                log(msg);
            } else {
                JSONObject responseData = new JSONObject(response.body());
                if (responseData.has("error")) {
                    JSONObject err = responseData.getJSONObject("error");
                    String msg = err.getString("message");
                    callback.failedReq(msg);
                } else {
                    JSONArray data = responseData.getJSONArray("data");
                    Type listType = new TypeToken<List<User>>() {}.getType();
                    List<User> userLikedList = new Gson().fromJson(data.toString(),listType);
                    callback.successReq(userLikedList);
                }
            }
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
log(t.getMessage());
    }
});
    }

    public void getPostByCategory(String category, Helper_Callback callback) {
        Call<String> call = api_post.getPostList(category);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray data = callback.getJsonArray(response);
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> postList = new Gson().fromJson(data.toString(), listType);
                callback.successReq(postList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });
    }

    public void getPostByUser(String idUser, Helper_Callback callback) {
        Call<String> call = api_post.getPostByUser(accessToken, idUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray data = callback.getJsonArray(response);
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> postList = new Gson().fromJson(data.toString(), listType);
                callback.successReq(postList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }

    public void updatePost(
            HashMap<String, RequestBody> map,
            String idPost,
            List<String> imageSubmitList,
            Helper_Callback callback) {
        Call<String> call = api_post.updatePost(accessToken, idPost, map,
                helper_image.uploadMulti(imageSubmitList, "image_post"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                callback.successReq(data);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });

    }

    private void log(String s) {
        Log.d("log", s);
    }


}