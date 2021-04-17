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
    private final Api_Comment api_comment;
    private final Helper_SP helper_sp;
    private Context context;
    private String accessToken;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    public DAO_Comment(Context context) {
        this.context = context;
        api_comment = Retrofit_config.retrofit.create(Api_Comment.class);
        helper_sp = new Helper_SP(context);
        helper_common = new Helper_Common();
        helper_image = new Helper_Image();
        accessToken = helper_sp.getAccessToken();
    }
    public void deleteComment(String idComment, Helper_Callback callback) {
        Call<String> call = api_comment.deleteComment(accessToken, idComment);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = callback.getJsonObject(response);
                if (data != null) {
                    callback.successReq(response);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    public void addComment(String id, String type, String sContent, String path, Helper_Callback callback) {
        RequestBody content = helper_common.createPartFromString(sContent);
        RequestBody rId = helper_common.createPartFromString(id);
        if (type.equals("post")) {
            Call<String> call_post = api_comment.addCommentPost(
                    accessToken, content, rId,
                    helper_image.uploadSingle(path, "image_comment"));
            call_post.enqueue(new Callback<String>() {
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
        } else {
            Call<String> call_blog = api_comment.addCommentBlog(
                    accessToken, content, rId,
                    helper_image.uploadSingle(path, "image_comment"));
            call_blog.enqueue(new Callback<String>() {
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
    }

    public void getCommentList(String id, String type, Helper_Callback callback) {


        if (type.equals("post")) {
            Call<String> call_post = api_comment.getCommentPost(id);
            call_post.enqueue(new Callback<String>() {
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
        } else {
            Call<String> call_blog = api_comment.getCommentBlog(id);

            call_blog.enqueue(new Callback<String>() {
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

    }
    private void log(String s) {
        Log.d("log", s);
    }
    private void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
