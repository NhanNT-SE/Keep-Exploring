package com.example.keep_exploring.DAO;

import android.content.Context;

import com.example.keep_exploring.api.Api_Like;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Like {
    private Api_Like api_like;
    private Context context;

    public DAO_Like(Context context) {
        this.context = context;
        api_like = Retrofit_config.retrofit.create(Api_Like.class);
    }

    public void getLikeList(String id, String type, Helper_Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        if (type.equals("post")) {
            map.put("idPost", id);
            Call<String> call = api_like.getLikeListPost(map);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String err = callback.getResponseError(response);
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    if (err.isEmpty() ) {
                        JSONArray data = callback.getJsonArray(response);
                        List<User> userLikedList = new Gson().fromJson(data.toString(), listType);
                        callback.successReq(userLikedList);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.failedReq(t.getMessage());
                }
            });
        } else {
            map.put("idBlog", id);
            Call<String> call = api_like.getLikeListBlog(map);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String err = callback.getResponseError(response);
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    if (err.isEmpty() ) {
                        JSONArray data = callback.getJsonArray(response);
                        List<User> userLikedList = new Gson().fromJson(data.toString(), listType);
                        callback.successReq(userLikedList);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.failedReq(t.getMessage());
                }
            });
        }
    }

    public void setLike(String accessToken, String id, String type, Helper_Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        if (type.equals("post")) {
            map.put("idPost", id);
            Call<String> call = api_like.likePost(accessToken, map);
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
        } else {
            map.put("idBlog", id);
            Call<String> call = api_like.likeBlog(accessToken, map);
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
    }
}
