package com.example.keep_exploring.helpers;

import android.util.Log;

import com.example.keep_exploring.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Response;

public abstract class Helper_Callback {
    public abstract List<User> successReq(Object response);

    public abstract void failedReq(String msg);

    public String getResponseError(Response<String> response) {
        String msg = "";
        try {
            if (response.errorBody() != null) {
                JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                msg = "Error from error body:\n" + err.getString("message");

            } else {
                JSONObject responseData = new JSONObject(response.body());
                if (responseData.has("error")) {
                    JSONObject err = responseData.getJSONObject("error");
                    msg = "Error from response data:\n" + err.getString("message");
                }
            }
        } catch (Exception e) {
            msg =  "Error form exception:\n" + e.getMessage();
            e.printStackTrace();
        }
        if (!msg.isEmpty()) {
            log(msg);
            failedReq(msg);
        }
        return msg;
    }

    public JSONObject getJsonObject(Response<String> response) {
        JSONObject data = null;
        String error = getResponseError(response);
        if (error.isEmpty()) {
            try {
                JSONObject responseData = new JSONObject(response.body());
                data = responseData.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public JSONArray getJsonArray(Response<String> response) {
        JSONArray data = null;
        String error = getResponseError(response);
        if (error.isEmpty()) {
            try {
                JSONObject responseData = new JSONObject(response.body());
                data = responseData.getJSONArray("data");

            } catch (JSONException e) {
                log(e.getMessage());
                e.printStackTrace();
            }
        }
        return data;
    }

    private void log(String s) {
        Log.d("log", s);
    }

}
