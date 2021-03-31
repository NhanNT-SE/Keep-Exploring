package com.example.project01_backup.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project01_backup.model.User;

import java.util.Map;

public class Helper_SP {
    private Context context;

    public Helper_SP(Context context) {
        this.context = context;
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }
    public String getAccessToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", null);
    }
    public void setRefreshToken(String refreshToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }
    public String getRefreshToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        return sharedPreferences.getString("refreshToken", null);
    }
    public void setUser(User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("_id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("displayName", user.getDisplayName());
        editor.putString("imgUser", user.getImgUser());
        editor.apply();
    }
    public User getUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sharedPreferences.getString("_id",null));
        user.setEmail(sharedPreferences.getString("email",null));
        user.setDisplayName(sharedPreferences.getString("displayName",null));
        user.setImgUser(sharedPreferences.getString("imgUser",null));
        return user;
    }
    public void clearSP() {
        context.getSharedPreferences("sp_token", Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
