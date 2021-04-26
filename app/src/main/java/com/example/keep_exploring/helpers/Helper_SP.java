package com.example.keep_exploring.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.keep_exploring.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void setUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("_id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("displayName", user.getDisplayName());
        editor.putString("imgUser", user.getImgUser());
        editor.apply();
    }

    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("_id", null) != null) {
            User user = new User();
            user.setId(sharedPreferences.getString("_id", null));
            user.setEmail(sharedPreferences.getString("email", null));
            user.setDisplayName(sharedPreferences.getString("displayName", null));
            user.setImgUser(sharedPreferences.getString("imgUser", null));
            return user;
        }

        return null;
    }

    public void clearSP() {
        context.getSharedPreferences("sp_token", Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).edit().clear().apply();
    }

    public void setProvinceList(List<String> provinceList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_province_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("province_list", String.join(",", provinceList));
        editor.apply();
    }

    public List<String> getProvinceList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_province_list", Context.MODE_PRIVATE);
        String provincesString = sharedPreferences.getString("province_list", null);
        return new ArrayList<String>(Arrays.asList(provincesString.split(",")));
    }
    public void setIsRemember(boolean isRemember) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRemember", isRemember);
        editor.apply();
    }
    public boolean getIsRemember() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isRemember", false);
    }

    public void setIsNotify(boolean isNotify) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isNotify", isNotify);
        editor.apply();
    }
    public boolean getIsNotify() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isNotify", true);
    }
}
