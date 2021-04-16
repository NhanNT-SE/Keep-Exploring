package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.keep_exploring.api.Api_Comment;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Comment {
    private final Api_Comment api_post;
    private final Helper_SP helper_sp;
    private Context context;


    public DAO_Comment(Context context) {
        this.context = context;
        api_post = Retrofit_config.retrofit.create(Api_Comment.class);
        helper_sp = new Helper_SP(context);
    }

    public void getCommentPost(String idPost, Helper_Callback callback) {
        Call<String> call = api_post.getCommentPost(idPost);
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
