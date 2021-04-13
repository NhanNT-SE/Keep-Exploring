package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.api.Api_Auth;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Auth {
    private final Api_Auth api_auth;
    private final Helper_SP helper_sp;
    private Helper_Image helper_image;
    private Helper_Common helper_common;
    private Context context;

    public DAO_Auth(Context context) {
        this.context = context;
        api_auth = Retrofit_config.retrofit.create(Api_Auth.class);
        helper_sp = new Helper_SP(context);
        helper_image = new Helper_Image();
        helper_common = new Helper_Common();
    }


    public void signIn(String email, String pass,Helper_Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);
        Call<String> call = api_auth.signIn(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(err.toString());

                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            log(msg);
                            callback.failedReq(msg);

                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            String accessToken = data.getString("accessToken");
                            String refreshToken = data.getString("refreshToken");
                            User user = new Gson().fromJson(data.toString(),User.class);
                            callback.successReq(user);
                            helper_sp.setUser(user);
                            helper_sp.setAccessToken(accessToken);
                            helper_sp.setRefreshToken(refreshToken);
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

    private void signOut(Helper_Callback callback) {
        String userId = helper_sp.getUser().getId();
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Call<String> call = api_auth.signOut(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(err.toString());
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            log(msg);
                        } else {
                            helper_sp.clearSP();
                            log(responseData.toString());
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

    public void signUp(String realPath, String email, String password, String displayName,Helper_Callback callback) {
        RequestBody r_email = helper_common.createPartFromString(email);
        RequestBody r_password = helper_common.createPartFromString(password);
        RequestBody r_displayName = helper_common.createPartFromString(displayName);
        Call<String> call = api_auth.signUp(
                r_email,
                r_password,
                r_displayName,
                helper_image.uploadSingle(realPath, "image_user"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(err.toString());
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            callback.failedReq(msg);
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            User user = new Gson().fromJson(data.toString(),User.class);
                            callback.successReq(user);
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

    public void refreshToken(Helper_Callback callback) {
        String refreshToken = helper_sp.getRefreshToken();
        String userId = helper_sp.getUser().getId();
        HashMap<String, String> map = new HashMap<>();
        map.put("refreshToken", refreshToken);
        map.put("userId", userId);

        Call<String> call = api_auth.refreshToken(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(err.toString());
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            log(msg);
                            callback.failedReq(msg);
                        } else {
                            String newAccessToken = responseData.getString("data");
                            helper_sp.setAccessToken(newAccessToken);
                            callback.successReq(newAccessToken);
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
