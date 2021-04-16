package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.keep_exploring.api.Api_Comment;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Comment {
    private final Api_Comment api_coment;
    private final Helper_SP helper_sp;
    private Context context;
    private String accessToken;
    private Helper_Common helper_common;
    private Helper_Image helper_image;

    public DAO_Comment(Context context) {
        this.context = context;
        api_coment = Retrofit_config.retrofit.create(Api_Comment.class);
        helper_sp = new Helper_SP(context);
        helper_common = new Helper_Common();
        helper_image = new Helper_Image();
        accessToken = helper_sp.getAccessToken();
    }

    public void addCommentPost(String sContent, String sIdPost, String path, Helper_Callback callback) {
        RequestBody content = helper_common.createPartFromString(sContent);
        RequestBody idPost = helper_common.createPartFromString(sIdPost);
        Call<String> call = api_coment.addCommentPost(
                accessToken, content, idPost,
                helper_image.uploadSingle(path, "image_comment"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                if (data != null) {
                    callback.successReq(data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    public void addCommentBlog(String sContent, String sIdBlog, String path, Helper_Callback callback) {
        RequestBody content = helper_common.createPartFromString(sContent);
        RequestBody idPost = helper_common.createPartFromString(sIdBlog);
        Call<String> call = api_coment.addCommentBlog(
                accessToken, content, idPost,
                helper_image.uploadSingle(path, "image_comment"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                if (data != null) {
                    callback.successReq(data);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    public void getCommentPost(String idPost, Helper_Callback callback) {
        Call<String> call = api_coment.getCommentPost(idPost);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray data = callback.getJsonArray(response);
                if (data != null) {
                    Type listType = new TypeToken<List<Comment>>() {
                    }.getType();
                    List<Comment> commentList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(commentList);
                }
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

    private void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


}
