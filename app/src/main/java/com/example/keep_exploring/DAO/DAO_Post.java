package com.example.keep_exploring.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.keep_exploring.api.Api_Post;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
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

    public void createPost(HashMap<String, RequestBody> map, List<String> imageSubmitList, Helper_Callback callback) {
        String accessToken = helper_sp.getAccessToken();
        List<MultipartBody.Part> imageList = new ArrayList<>();
        for (int i = 0; i < imageSubmitList.size(); i++) {
            File file = new File(imageSubmitList.get(i));
            String realPath = imageSubmitList.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part singleImage = MultipartBody.Part.createFormData("image_post", realPath, requestBody);
            imageList.add(singleImage);
        }
        Call<String> call = api_post.createPost(accessToken, map, imageList);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string());
                        log(err.getString("error"));
                        String msg = err.getString("message");
                        log(msg);
                        callback.failedReq(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            log(msg);
                            callback.failedReq(msg);
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            callback.successReq(data);
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

    public void deletePost(String postId, Helper_Callback callback) {
        String accessToken = helper_sp.getAccessToken();
        Call<String> call = api_post.deletePost(accessToken, postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string());
                        log(err.getString("error"));
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        toast(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            callback.failedReq(msg);
                            toast(msg);
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            callback.successReq(data);
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

    public void getPostById(String idPost, Helper_Callback callback) {
        Call<String> call = api_post.getPostById(idPost);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string());
                        log(err.getString("error"));
                        String msg = err.getString("message");
                        callback.failedReq(msg);
                        log(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            callback.failedReq(msg);
                            log(msg);
                        } else {
                            JSONObject jsonData = responseData.getJSONObject("data");
                            Post post = new Gson().fromJson(jsonData.toString(), Post.class);
                            callback.successReq(post);
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

    public void getPostByCategory(String category, Helper_Callback callback) {
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
                        callback.failedReq(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            toast(msg);
                            callback.failedReq(msg);
                        } else {
                            JSONArray data = responseData.getJSONArray("data");
                            Type listType = new TypeToken<List<Post>>() {}.getType();
                            List<Post> postList = new Gson().fromJson(data.toString(),listType);
                            callback.successReq(postList);
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

    public void updatePost(
            HashMap<String, RequestBody> map,
            String idPost,
            List<String> imageSubmitList,
            Helper_Callback callback) {
        String accessToken = helper_sp.getAccessToken();
        List<MultipartBody.Part> imageList = new ArrayList<>();
        for (int i = 0; i < imageSubmitList.size(); i++) {
            File file = new File(imageSubmitList.get(i));
            String realPath = imageSubmitList.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part singleImage = MultipartBody.Part.createFormData("image_post", realPath, requestBody);
            imageList.add(singleImage);
        }
        Call<String> call = api_post.updatePost(accessToken, idPost, map, imageList);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject err = new JSONObject(response.errorBody().string());
                        log(err.getString("error"));
                        String msg = err.getString("message");
                        log(msg);
                        callback.failedReq(msg);
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            String msg = err.getString("message");
                            log(msg);
                            callback.failedReq(msg);
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            callback.successReq(data);
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
