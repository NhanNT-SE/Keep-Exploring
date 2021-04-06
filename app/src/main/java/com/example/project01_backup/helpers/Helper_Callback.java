package com.example.project01_backup.helpers;

import com.example.project01_backup.model.Post;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

import retrofit2.http.POST;

public class Helper_Callback {

    public void postList(List<Post> postList) {
    }

    public void addressList(List<String> districtList, List<String> wardList) {
    }

    public void getPostById(Post post) throws MalformedURLException {
    }

    public void successReq(JSONObject data) {
    }

    public void selectImage(){};

    public void onSubmitAlertDialog(){};
}
