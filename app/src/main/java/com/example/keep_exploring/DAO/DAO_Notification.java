package com.example.keep_exploring.DAO;

import android.content.Context;

import com.example.keep_exploring.api.Api_Notification;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Notification;
import com.example.keep_exploring.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Notification {
    private Api_Notification api_notification;
    private Helper_SP helper_sp;
    private Context context;
    private String accessToken;
    public DAO_Notification(Context context) {
        this.context = context;
        api_notification = Retrofit_config.retrofit.create(Api_Notification.class);
        helper_sp = new Helper_SP(context);
        accessToken = helper_sp.getAccessToken();
    }

    public void getAll(Helper_Callback callback) {
        Call<String> call = api_notification.getAll(accessToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray data = callback.getJsonArray(response);
                Type listType = new TypeToken<List<Notification>>() {
                }.getType();
                List<Notification> notificationList = new Gson().fromJson(data.toString(), listType);
                callback.successReq(notificationList);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });
    }

}