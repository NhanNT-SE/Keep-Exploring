package com.example.keep_exploring.DAO;

import android.content.Context;

import com.example.keep_exploring.api.Api_Notification;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.model.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Notification {
    private Api_Notification api_notification;
    private Context context;
    public DAO_Notification(Context context) {
        this.context = context;
        api_notification = Retrofit_config.retrofit.create(Api_Notification.class);
    }

    public void getAll(String accessToken, Helper_Callback callback) {
        Call<String> call = api_notification.getAll(accessToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                Type listType = new TypeToken<List<Notification>>() {
                }.getType();
                if (err.isEmpty()) {
                    JSONArray data = callback.getJsonArray(response);
                    List<Notification> notificationList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(notificationList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());

            }
        });
    }

    public void changeSeenStatusNotify(String accessToken,Helper_Callback callback){
        Call<String> call = api_notification.changeSeenStatusNotify(accessToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty()) {
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

    public void changeNewStatusNotify(String accessToken,String idNotify, Helper_Callback callback) {
        Call<String> call = api_notification.changeNewStatusNotify(accessToken, idNotify);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty()) {
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

    public void deleteNotify(String accessToken,String idNotify, Helper_Callback callback) {
        Call<String> call = api_notification.deleteNotify(accessToken, idNotify);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty()) {
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
