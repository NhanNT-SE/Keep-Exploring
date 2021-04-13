package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.api.Api_User;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

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

    public void getProfile(Helper_Callback callback, String idUser) {
        String accessToken = helper_sp.getAccessToken();
        Call<String> callProfile = apiUser.getProfile(accessToken,idUser);
        callProfile.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(err.toString());
                    } else {
                        JSONObject responseData = new JSONObject(response.body()).getJSONObject("data");
                        User user = new Gson().fromJson(responseData.toString(), User.class);
                        callback.successReq(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    public void updateProfile(){

    }

    private void log(String s) {
        Log.d("log", s);
    }

}
