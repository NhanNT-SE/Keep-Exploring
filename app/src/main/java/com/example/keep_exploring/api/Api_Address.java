package com.example.keep_exploring.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_Address {
    //    PUBLIC API
    @GET("/public/address")
    Call<String> getProvinceList();
    @POST("/public/address")
    Call<String> getAddressList(@Body HashMap<String, String> map);
}
