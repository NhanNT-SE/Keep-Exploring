package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.api.Api_User;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_User {
    private Helper_SP helper_sp;
    private Context context;
    private Api_User apiUser;

    public DAO_User(Context context) {
        this.context = context;
        this.apiUser = Retrofit_config.retrofit.create(Api_User.class);
        this.helper_sp = new Helper_SP(context);
    }

    public void getMyProfile() {
        String accessToken = helper_sp.getAccessToken();
        Call<String> callProfile =apiUser.getMyProfile(accessToken);
        Log.d("TAG", "accessToken: "+accessToken);
        callProfile.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonData = new JSONObject(response.body()).getJSONObject("data");

                    User user = new Gson().fromJson(jsonData.toString(),User.class);
//                    user.setPost(null);
//                    user.setBlog(null);
                    log("User: " + user.toString());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });



    }
    private void log(String s) {
        Log.d("log", s);
    }

}
