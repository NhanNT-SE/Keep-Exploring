package com.example.project01_backup.DAO;

import android.content.Context;
import android.util.Log;

import com.example.project01_backup.api.Api_Auth;
import com.example.project01_backup.api.Retrofit_config;
import com.example.project01_backup.helpers.Helper_Common;
import com.example.project01_backup.helpers.Helper_SP;
import com.example.project01_backup.model.User;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Auth {
    private final Api_Auth api_auth;
    private final Helper_SP helper_sp;
    public DAO_Auth(Context context) {
        api_auth = Retrofit_config.retrofit.create(Api_Auth.class);
        helper_sp = new Helper_SP(context);
    }
    public void signIn(String email, String pass) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);
        Call<String> call = api_auth.signIn(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        log(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            log(err.toString());
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            String accessToken = data.getString("accessToken");
                            String refreshToken = data.getString("refreshToken");
                            User user = new User();
                            user.setId(data.getString("_id"));
                            user.setEmail(data.getString("email"));
                            user.setImgUser(data.getString("imgUser"));
                            user.setDisplayName(data.getString("displayName"));

                            helper_sp.setUser(user);
                            helper_sp.setAccessToken(accessToken);
                            helper_sp.setRefreshToken(refreshToken);
                            log("User SP:\n" + helper_sp.getUser().toString());
                            log("Access Token:\n" + helper_sp.getAccessToken());
                            log("Refresh Token:\n" + helper_sp.getRefreshToken());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t.getMessage());
            }
        });

    }
    private void log(String s) {
        Log.d("log", s);
    }

}