package com.example.keep_exploring.helpers;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.DAO.DAO_Auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public abstract class Helper_Callback {
    public Helper_Callback() {
    }

    public abstract void successReq(Object response);

    public abstract void failedReq(String msg);

    public String getResponseError(Response<String> response) {
        String msg = "";
        String msgDebug = "";
        try {
            if (response.errorBody() != null) {
                JSONObject err = new JSONObject(response.errorBody().string()).getJSONObject("error");
                msg = err.getString("message");
                if (msg.equalsIgnoreCase("jwt expired")){
                    int dateExpired = err.getInt("payload");
                    if (dateExpired < 8){
                        msg = "REFRESH TOKEN";
                    }else {
                        msg = "LOG OUT";
                    }
                }
                msgDebug = "Error from error body:\n" + msg;
            } else {
                assert response.body() != null;
                JSONObject responseData = new JSONObject(response.body());
                if (responseData.has("error")) {
                    JSONObject err = responseData.getJSONObject("error");
                    msg =  err.getString("message");
                    msgDebug = "Error from response data:\n" + msg;
                }
            }
        } catch (Exception e) {
            msg = e.getMessage();
            msgDebug =  "Error form exception:\n" + msg;
            e.printStackTrace();
        }
        if (!msg.isEmpty()) {
            log(msgDebug);
            failedReq(msg);
        }
        return msg;
    }

    public JSONObject getJsonObject(Response<String> response) {
        JSONObject data = null;
        try {
            assert response.body() != null;
            JSONObject responseData = new JSONObject(response.body());
            data = responseData.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public JSONArray getJsonArray(Response<String> response) {
        JSONArray data = null;
        try {
            assert response.body() != null;
            JSONObject responseData = new JSONObject(response.body());
            data = responseData.getJSONArray("data");
        } catch (JSONException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    private void log(String s) {
        Log.d("log", s);
    }

}
