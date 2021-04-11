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
import java.util.stream.Collectors;

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
    private StorageReference storageBlog;
    private int countUpdate;


    public DAO_Blog(Context context) {
        this.context = context;
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(context);
        api_blog = Retrofit_config.retrofit.create(Api_Blog.class);
        storageRef = FirebaseStorage
                .getInstance()
                .getReference("Images/" + helper_sp.getUser().getId());
        signInAnonymously();
    }

    public void createBlog(
            List<Blog_Details> blogDetailsList,
            String titleBlog,
            String imageBlog,
            Helper_Callback callback
    ) {
        String folderStorage = helper_common.getMillisTime() + "";
        uploadImageBlogDetail(folderStorage, blogDetailsList, new Helper_Callback() {
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
                RequestBody folder_storage = helper_common.createPartFromString(folderStorage);
                Call<String> call = api_blog.createBlog(accessToken, title_blog, folder_storage, image_blog, blog_detailsList);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if (response.errorBody() != null) {
                                String msg = new JSONObject(response.errorBody().string())
                                        .getJSONObject("error")
                                        .getString("message");
                                log(msg);
                                callback.failedReq(msg);
                            } else {
                                JSONObject responseData = new JSONObject(response.body());
                                JSONObject data = responseData.getJSONObject("data");
                                callback.successReq(data);
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

    public void deleteBlog(String idBlog,List<Blog_Details> blog_detailsList, String folder_storage, Helper_Callback callback) {
        deleteFolderImage(
                folder_storage, blog_detailsList,new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        String accessToken = helper_sp.getAccessToken();
                        Call<String> call = api_blog.deleteBlog(accessToken, idBlog);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                try {
                                    if (response.errorBody() != null) {
                                        String msg = new JSONObject(response.errorBody().string())
                                                .getJSONObject("error")
                                                .getString("message");
                                        log(msg);
                                        callback.failedReq(msg);
                                    } else {
                                        JSONObject responseData = new JSONObject(response.body());
                                        JSONObject data = responseData.getJSONObject("data");
                                        callback.successReq(data);
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

                    @Override
                    public void failedReq(String msg) {
                        log(msg);
                    }
                }
        );
    }

    public void getBlogById(String idBlog, Helper_Callback callback) {
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

    public void updateBlog(
            String idBlog,
            String titleBlog,
            String folder_storage,
            String imageBlog,
            List<Blog_Details> contentList,
            Helper_Callback callback) {
        updateImageBlogDetail(folder_storage, contentList, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Blog_Details> blog_detailsList = (List<Blog_Details>) response;
                RequestBody requestBody;
                if (imageBlog.isEmpty()) {
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                } else {
                    File file = new File(imageBlog);
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                }
                String accessToken = helper_sp.getAccessToken();
                RequestBody rTitleBlog = helper_common.createPartFromString(titleBlog);
                RequestBody rCreated_on = helper_common.createPartFromString(helper_common.getIsoDate());
                MultipartBody.Part image_blog = MultipartBody.Part.createFormData("image_blog", imageBlog, requestBody);
                Call<String> call = api_blog.updateBlog(accessToken, idBlog, rTitleBlog, rCreated_on, image_blog, blog_detailsList);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if (response.errorBody() != null) {
                                JSONObject err = new JSONObject(response.errorBody().string());
                                String msg = err.getJSONObject("error").getString("message");
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
                        callback.failedReq(t.getMessage());
                        log(t.getMessage());
                    }
                });
            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void uploadImageBlogDetail(String folder_storage, List<Blog_Details> blog_detailsList, Helper_Callback callback) {
        for (int i = 0; i < blog_detailsList.size(); i++) {
            Uri uri = blog_detailsList.get(i).getUriImage();
            String fileName = helper_common.getMillisTime() + "";
            storageBlog = storageRef.child(folder_storage + "/" + fileName);
            UploadTask uploadTask = storageBlog.putFile(uri);
            int k = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadUri = task.getResult();
                            Blog_Details blog_details = blog_detailsList.get(k);
                            blog_details.setImg(downloadUri.toString());
                            blog_details.setFileName(fileName);
                            if (k + 1 == blog_detailsList.size()) {
                                callback.successReq(blog_detailsList);
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateImageBlogDetail(String folder_storage, List<Blog_Details> blog_detailsList, Helper_Callback callback) {
        String childStorage;
        countUpdate = 0;
        List<Blog_Details> updateList = blog_detailsList.stream()
                .filter(p -> p.getUriImage() != null).collect(Collectors.toList());
        int sideList = blog_detailsList.size();
        if (updateList.size() > 0) {
            for (int i = 0; i < sideList; i++) {
                Blog_Details blog_details = blog_detailsList.get(i);
                int k = i;
                Uri uri = blog_detailsList.get(i).getUriImage();
                String fileName = helper_common.getMillisTime() + "";
                if (uri != null) {
                    countUpdate++;
                    if (blog_details.getFileName() != null) {
                        childStorage = blog_details.getFileName();
                    } else {
                        childStorage = fileName;

                    }
                    storageBlog = storageRef.child(folder_storage + "/" + childStorage);
                    if (blog_details.getFileName() == null) {
                        blog_details.setFileName(fileName);
                    }
                    UploadTask uploadTask = storageBlog.putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri downloadUri = task.getResult();
                                    Blog_Details blog_details = blog_detailsList.get(k);
                                    blog_details.setImg(downloadUri.toString());
                                    if (blog_details.getFileName() == null) {
                                        blog_details.setFileName(fileName);
                                    }
                                    if (countUpdate == updateList.size()) {
                                        callback.successReq(blog_detailsList);
                                        log(blog_detailsList.toString());
                                    }
                                }
                            });
                        }
                    });
                }

            }
        } else {
            callback.successReq(blog_detailsList);
        }
    }

    private void deleteFolderImage(String folder_storage, List<Blog_Details> blog_detailsList, Helper_Callback callback) {
        storageBlog = storageRef.child(folder_storage);
        int sizeList = blog_detailsList.size();
        for (int i = 0; i < sizeList; i++) {
            Blog_Details blog_details = blog_detailsList.get(i);
            StorageReference storageChild = storageBlog.child(blog_details.getFileName());
            int k = i;
            storageChild.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (k + 1 == sizeList) {
                        callback.successReq(sizeList);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    callback.failedReq(exception.getMessage());
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
