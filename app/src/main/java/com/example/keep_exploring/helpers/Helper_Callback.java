package com.example.keep_exploring.helpers;

import org.json.JSONObject;

import retrofit2.Response;

public abstract class Helper_Callback {
    public abstract void successReq(Object response);

    public abstract void failedReq(String msg);

    public String getResponseError(Response<String> response) {
        String msg = "";
        try {
            if (response.errorBody() != null) {
                JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                msg = err.getString("message");
            } else {
                JSONObject responseData = new JSONObject(response.body());
                if (responseData.has("error")) {
                    JSONObject err = responseData.getJSONObject("error");
                    msg = err.getString("message");
                }
            }
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        if (!msg.isEmpty()) {
            failedReq(msg);
        }
        return msg;
    }
}
