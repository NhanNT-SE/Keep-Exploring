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
import com.example.keep_exploring.model.Blog_Details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private FirebaseUser user = mAuth.getCurrentUser();
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
            public void blogDetailList(List<Blog_Details> blog_detailsList) {
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
                                JSONObject err = new JSONObject(response.errorBody().string());
                                log(err.getString("error"));
                                String msg = err.getString("message");
                                log(msg);

                            } else {
                                JSONObject responseData = new JSONObject(response.body());
                                if (responseData.has("error")) {
                                    JSONObject err = responseData.getJSONObject("error");
                                    String msg = err.getString("message");
                                    log(msg);
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
        });

    }

    private void uploadImageBlogDetail(List<Blog_Details> blog_detailsList, Helper_Callback helper_callback) {
        List<Uri> uriList = new ArrayList<>();
        for (Blog_Details item : blog_detailsList) {
            uriList.add(item.getUriImage());
        }
        for (int i = 0; i < uriList.size(); i++) {
            Uri uri = uriList.get(i);
            StorageReference storageBlog = storageRef.child(helper_common.getMillisTime() + "");
            UploadTask uploadTask = storageBlog.putFile(uri);
            int k = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageBlog.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Blog_Details blog_details = blog_detailsList.get(k);
                            blog_details.setImg(uri.toString());
                            if (k + 1 == uriList.size()) {
                                helper_callback.blogDetailList(blog_detailsList);
                            }

                        }
                    });
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
