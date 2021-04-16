package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;

import com.example.keep_exploring.api.Api_Address;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Address {
    private final Api_Address api_address;
    private final Helper_SP helper_sp;
    private Context context;

    public DAO_Address(Context context) {
        this.context = context;
        api_address = Retrofit_config.retrofit.create(Api_Address.class);
        helper_sp = new Helper_SP(context);
    }

    public void getProvinceList(Helper_Callback callback) {
        Call<String> call = api_address.getProvinceList();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray data = callback.getJsonArray(response);
                List provinceList = new Gson().fromJson(data.toString(), List.class);
                helper_sp.setProvinceList(provinceList);
                callback.successReq(provinceList);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }

    public void getAddressList(String province, String district, Helper_Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("province", province);
        map.put("district", district);
        Call<String> call = api_address.getAddressList(map);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject data = callback.getJsonObject(response);
                    JSONArray jsonDistrictList = data.getJSONArray("districtList");
                    JSONArray jsonWardList = data.getJSONArray("wardList");
                    List<String> districtList = new ArrayList<>();
                    List<String> wardList = new ArrayList<>();
                    for (int i = 0; i < jsonDistrictList.length(); i++) {
                        districtList.add(jsonDistrictList.get(i).toString());
                    }
                    for (int i = 0; i < jsonWardList.length(); i++) {
                        wardList.add(jsonWardList.get(i).toString());
                    }
                    Map<String, List<String>> map = new HashMap<>();
                    map.put("districtList", districtList);
                    map.put("wardList", wardList);
                    callback.successReq(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    private void log(String s) {
        Log.d("log", s);
    }

}
