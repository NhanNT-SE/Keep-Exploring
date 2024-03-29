package com.example.keep_exploring.DAO;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.keep_exploring.api.Api_Blog;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Blog {
    private Context context;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef;
    private Helper_SP helper_sp;
    private Api_Blog api_blog;
    private Helper_Image helper_image;



    public DAO_Blog(Context context) {
        this.context = context;
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(context);
        helper_date = new Helper_Date();
        helper_image = new Helper_Image();
        api_blog = Retrofit_config.retrofit.create(Api_Blog.class);
        if (helper_sp.getUser() != null) {
            storageRef = FirebaseStorage
                    .getInstance()
                    .getReference("Images/" + helper_sp.getUser().getId());
            signInAnonymously();
        }

    }

    public void getBlogList(Helper_Callback callback) {
        Call<String> call = api_blog.getBlogList();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String err = callback.getResponseError(response);
                    if (err.isEmpty()) {
                        JSONArray data = callback.getJsonArray(response);
                        List<Blog> blogList = new ArrayList<>();
                        int sizeList = data.length();
                        Type listType = new TypeToken<List<Blog_Details>>() {
                        }.getType();
                        for (int i = 0; i < sizeList; i++) {
                            JSONObject jsonBlog = data.getJSONObject(i);
                            Blog blog = new Gson().fromJson(jsonBlog.toString(), Blog.class);
                            JSONArray jsonArrayBlogDetail = jsonBlog
                                    .getJSONObject("blog_detail")
                                    .getJSONArray("detail_list");
                            List<Blog_Details> blogDetailsList = new Gson().fromJson(jsonArrayBlogDetail.toString(), listType);
                            blog.setBlogDetails(blogDetailsList);
                            blogList.add(blog);
                        }
                        callback.successReq(blogList);
                    }
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

    public void getBlogByQuery(String query, Helper_Callback callback) {
        Call<String> call = api_blog.getBlogByQuery(query);
        Type listType = new TypeToken<List<Blog_Details>>() {
        }.getType();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String err = callback.getResponseError(response);
                    if (err.isEmpty()) {
                        JSONArray data = callback.getJsonArray(response);
                        List<Blog> blogList = new ArrayList<>();
                        int sizeList = data.length();
                        if (sizeList>0){
                            for (int i = 0; i < sizeList; i++) {
                                JSONObject jsonBlog = data.getJSONObject(i);
                                Blog blog = new Gson().fromJson(jsonBlog.toString(), Blog.class);
                                JSONArray jsonArrayBlogDetail = jsonBlog
                                        .getJSONObject("blog_detail")
                                        .getJSONArray("detail_list");
                                List<Blog_Details> blogDetailsList = new Gson().fromJson(jsonArrayBlogDetail.toString(), listType);
                                blog.setBlogDetails(blogDetailsList);
                                blogList.add(blog);
                            }
                        }
                        callback.successReq(blogList);

                    }
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

    public void createBlog(
            String accessToken,
            List<Blog_Details> blogDetailsList,
            String titleBlog,
            String path,
            Helper_Callback callback
    ) {
        String folderStorage = helper_date.getMillisTime() + "";
        helper_image.uploadImageBlogDetail(storageRef, folderStorage, blogDetailsList, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Blog_Details> blog_detailsList = (List<Blog_Details>) response;
                RequestBody title_blog = helper_common.createPartFromString(titleBlog);
                RequestBody folder_storage = helper_common.createPartFromString(folderStorage);
                Call<String> call = api_blog.createBlog(accessToken, title_blog, folder_storage,
                        helper_image.uploadSingle(path, "image_blog"), blog_detailsList);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String err = callback.getResponseError(response);
                        if (err.isEmpty()) {
                            JSONObject data = callback.getJsonObject(response);
                            callback.successReq(data);
                        }

                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        callback.failedReq(t.getMessage());
                    }
                });
            }

            @Override
            public void failedReq(String msg) {
            }
        });

    }

    public void deleteBlog(String accessToken,
                           String idBlog, Helper_Callback callback) {
        Call<String> call = api_blog.deleteBlog(accessToken, idBlog);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                if (err.isEmpty()) {
                    JSONObject data = callback.getJsonObject(response);
                    callback.successReq(data);
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }

    public void getBlogById(String idBlog, Helper_Callback callback) {
        Call<String> call = api_blog.getBlogById(idBlog);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String err = callback.getResponseError(response);
                    Type listType = new TypeToken<List<Blog_Details>>() {
                    }.getType();
                    if (err.isEmpty()) {
                        JSONObject data = callback.getJsonObject(response);
                        JSONArray jsonArrayBlogDetail = data
                                .getJSONObject("blog_detail")
                                .getJSONArray("detail_list");
                        Blog blog = new Gson().fromJson(data.toString(), Blog.class);
                        List<Blog_Details> blogDetailsList = new Gson().fromJson(jsonArrayBlogDetail.toString(), listType);
                        blog.setBlogDetails(blogDetailsList);
                        callback.successReq(blog);

                    }
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

    public void getBlogByUser(String accessToken,
                              String idUser, Helper_Callback callback) {
        Call<String> call = api_blog.getBlogByUser(accessToken, idUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String err = callback.getResponseError(response);
                Type listType = new TypeToken<List<Blog>>() {
                }.getType();
                if (err.isEmpty()) {
                    JSONArray data = callback.getJsonArray(response);
                    List<Blog> blogList = new Gson().fromJson(data.toString(), listType);
                    callback.successReq(blogList);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.failedReq(t.getMessage());
            }
        });
    }
    public void updateBlog(
            String accessToken,
            String idBlog,
            String titleBlog,
            String folder_storage,
            String path,
            List<Blog_Details> contentList,
            Helper_Callback callback) {
        helper_image.updateImageBlogDetail(storageRef, folder_storage, contentList, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Blog_Details> blog_detailsList = (List<Blog_Details>) response;
                RequestBody rTitleBlog = helper_common.createPartFromString(titleBlog);
                RequestBody rCreated_on = helper_common.createPartFromString(helper_date.getIsoDate());
                Call<String> call = api_blog.updateBlog(accessToken, idBlog, rTitleBlog, rCreated_on,
                        helper_image.uploadSingle(path, "image_blog"), blog_detailsList);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String err = callback.getResponseError(response);
                        if (err.isEmpty()) {
                            JSONObject data = callback.getJsonObject(response);
                            callback.successReq(data);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        callback.failedReq(t.getMessage());
                    }
                });

            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }
    public void deleteFolderImage(String folder_storage, List<Blog_Details> deleteList) {
        helper_image.deleteFolderImage(storageRef, folder_storage, deleteList);
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener((Activity) context, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }


}
