package com.example.keep_exploring.DAO;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.keep_exploring.api.Api_Blog;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DAO_Blog {
    private Context context;
    private Helper_Common helper_common;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef;
    private Helper_SP helper_sp;
    private Api_Blog api_blog;

    public DAO_Blog(Context context) {
        this.context = context;
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(context);
        api_blog = Retrofit_config.retrofit.create(Api_Blog.class);
        storageRef = FirebaseStorage
                .getInstance()
                .getReference("Images/Blog Details/" + helper_sp.getUser().getId());
        signInAnonymously();
    }

    public void createBlog(
            List<Blog_Details> blogDetailsList,
            String titleBlog,
            String imageBlog,
            Helper_Callback callback
    ) {
        uploadImageBlogDetail(blogDetailsList, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Blog_Details> blog_detailsList = (List<Blog_Details>) response;
                String accessToken = helper_sp.getAccessToken();
                RequestBody requestBody;
                if (imageBlog.isEmpty()) {
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                } else {
                    File file = new File(imageBlog);
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                }

                MultipartBody.Part image_blog = MultipartBody.Part.createFormData("image_blog", imageBlog, requestBody);
                RequestBody title_blog = helper_common.createPartFromString(titleBlog);
                Call<String> call = api_blog.createBlog(accessToken, title_blog, image_blog, blog_detailsList);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if (response.errorBody() != null) {
                                String msg = new JSONObject(response.errorBody().string()).getString("message");
                                log(msg);
                                callback.failedReq(msg);

                            } else {
                                JSONObject responseData = new JSONObject(response.body());
                                if (responseData.has("error")) {
                                    String msg = responseData.getJSONObject("error").getString("message");
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

            @Override
            public void failedReq(String msg) {

            }
        });

    }

    public void getBlogById(String idBlog,Helper_Callback callback) {
        Call<String> call = api_blog.getBlogById(idBlog);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        String msg = new JSONObject(response.errorBody().string()).getString("message");
                        callback.failedReq(msg);
                        log(msg);

                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        if (responseData.has("error")) {
                            String msg = responseData.getJSONObject("error").getString("message");
                            callback.failedReq(msg);
                            log(msg);
                        } else {
                            JSONObject jsonData = responseData.getJSONObject("data");
                            JSONArray jsonArrayBlogDetail = jsonData
                                    .getJSONObject("blog_detail")
                                    .getJSONArray("detail_list");
                            Blog blog = new Gson().fromJson(jsonData.toString(), Blog.class);
                            List<Blog_Details> blogDetailsList = new ArrayList<>();
                            for (int i = 0; i < jsonArrayBlogDetail.length(); i++) {
                                JSONObject jsonBlogDetail = jsonArrayBlogDetail.getJSONObject(i);
                                Blog_Details blogDetails = new Gson().fromJson(jsonBlogDetail.toString(), Blog_Details.class);
                                blogDetailsList.add(blogDetails);
                            }
                            blog.setBlogDetails(blogDetailsList);
                            callback.successReq(blog);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void uploadImageBlogDetail(List<Blog_Details> blog_detailsList, Helper_Callback callback) {
        List<Uri> uriList = new ArrayList<>();
        for (Blog_Details item : blog_detailsList) {
            uriList.add(item.getUriImage());
        }
        for (int i = 0; i < uriList.size(); i++) {
            Uri uri = uriList.get(i);
            StorageReference storageBlog = storageRef.child(helper_common.getMillisTime() + "");
            UploadTask uploadTask = storageBlog.putFile(uri);
            int k = i;
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageBlog.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Blog_Details blog_details = blog_detailsList.get(k);
                        blog_details.setImg(downloadUri.toString());
                        if (k + 1 == uriList.size()) {
                            callback.successReq(blog_detailsList);
                        }
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener((Activity) context, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                log("signInAnonymously Successfully");
            }
        }).addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                log("signInAnonymously:FAILURE" + exception.toString());
            }
        });
    }

    private void log(String msg) {
        Log.d("log", msg);
    }
}
