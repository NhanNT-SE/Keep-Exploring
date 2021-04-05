package com.example.project01_backup.DAO;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.project01_backup.activities.MainActivity;
import com.example.project01_backup.api.Api_Auth;
import com.example.project01_backup.api.Api_Post;
import com.example.project01_backup.api.Retrofit_config;
import com.example.project01_backup.helpers.Helper_Callback;
import com.example.project01_backup.helpers.Helper_SP;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Post {
    private final Api_Post api_post;
    private final Helper_SP helper_sp;
    private Context context;

    public DAO_Post(Context context) {
        this.context = context;
        api_post = Retrofit_config.retrofit.create(Api_Post.class);
        helper_sp = new Helper_SP(context);
    }
    public void getPostByCategory(String category, Helper_Callback helper_callback) {
        Call<String> call = api_post.getPostList(category);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string());
                        log(err.getString("error"));
                        String msg = err.getString("message");
                        toast(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            toast(msg);
                        } else {
                            List<Post> postList = new ArrayList<>();
                            List<String> listImg = new ArrayList<>();
                            JSONArray data = responseData.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonPost = data.getJSONObject(i);
                                JSONObject jsonOwner = jsonPost.getJSONObject("owner");
                                JSONArray jsonArrayImg = jsonPost.getJSONArray("imgs");
                                JSONArray jsonArrayLike = jsonPost.getJSONArray("like_list");
                                JSONArray jsonArrayComment = jsonPost.getJSONArray("comment");
                                for (int j = 0; j < jsonArrayImg.length(); j++) {
                                    listImg.add(jsonArrayImg.get(i).toString());
                                }
                                Post post = new Post();
                                User user = new User();
//                                Populate Owner
                                user.setId(jsonOwner.getString("_id"));
                                user.setEmail(jsonOwner.getString("email"));
                                user.setDisplayName(jsonOwner.getString("displayName"));
                                user.setImgUser(jsonOwner.getString("imgUser"));
//                                Set post to add list
                                post.set_id(jsonPost.getString("_id"));
                                post.setCategory(jsonPost.getString("category"));
                                post.setImgs(listImg);
                                post.setStatus(jsonPost.getString("status"));
                                post.setCreated_on(jsonPost.getString("created_on"));
                                post.setLikes(jsonArrayLike.length());
                                post.setComments(jsonArrayComment.length());
                                post.setTitle(jsonPost.getString("title"));
                                post.setDesc(jsonPost.getString("desc"));
                                post.setAddress(jsonPost.getString("address"));
                                post.setRating(jsonPost.getInt("rating"));
                                post.setOwner(user);
                                postList.add(post);
                                log("post" +post.toString());
                            }
                            helper_callback.postList(postList);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t.getMessage());
            }
        });

    }


    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

}
