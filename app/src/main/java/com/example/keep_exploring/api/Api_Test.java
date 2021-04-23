package com.example.keep_exploring.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Api_Test {
    @GET("/test")
    Call<String> test();

    @GET("/test/test2")
    Call<String> test2();

}
