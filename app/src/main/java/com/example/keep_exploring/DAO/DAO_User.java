package com.example.keep_exploring.DAO;

import android.content.Context;

import com.example.keep_exploring.api.Api_User;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_User {
    private Context context;
    private Api_User apiUser;
    private Helper_Image helper_image;

    public DAO_User(Context context) {
        this.context = context;
        apiUser = Retrofit_config.retrofit.create(Api_User.class);
        helper_image = new Helper_Image();
    }

    public void getProfile(String accessToken,String idUser, Helper_Callback callback) {
        Call<String> callProfile = apiUser.getProfile(accessToken, idUser);
        callProfile.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty() ) {
                    JSONObject data = callback.getJsonObject(response);
                    User user = new Gson().fromJson(data.toString(), User.class);
                    callback.successReq(user);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }

    public void updateProfile(String accessToken, String path, HashMap<String, RequestBody> map, Helper_Callback callback) {
        Call<String> call = apiUser.updateProfile(accessToken, map, helper_image.uploadSingle(path, "image_user"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty()) {
                    JSONObject data = callback.getJsonObject(response);
                    User user = new Gson().fromJson(data.toString(), User.class);
                    callback.successReq(user);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    public void changePassword(String accessToken,String oldPass, String newPass, Helper_Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("oldPass", oldPass);
        map.put("newPass", newPass);
        Call<String> call = apiUser.changePassword(accessToken, map);
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
