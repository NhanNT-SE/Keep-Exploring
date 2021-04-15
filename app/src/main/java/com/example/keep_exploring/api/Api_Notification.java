package com.example.keep_exploring.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface Api_Notification {

    //GET Method
    @GET("/notification")
    Call<String> getAll(@Header("Authorization") String accessToken);

    @PATCH("/notification/status")
    Call<String> changeSeenStatusNotify(@Header("Authorization") String accessToken);

    @PATCH("/notification/status/{idNotify}")
    Call<String> changeNewStatusNotify(@Header("Authorization") String accessToken,
                                       @Path("idNotify") String idNotify);

    @DELETE("/notification/delete")
    Call<String> deleteAllNotify(@Header("Authorization") String accessToken);

    @DELETE("/notification/delete/{idNotify}")
    Call<String> deleteNotifyById(@Header("Authorization") String accessToken,
                                  @Path("idNotify") String idNotify);
}
