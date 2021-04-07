package com.example.keep_exploring.DAO;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Blog_Details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class DAO_Blog {
    private Context context;
    private Helper_Common helper_common;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference storageRef;
    private String userId;

    public DAO_Blog(Context context) {
        this.context = context;
        helper_common = new Helper_Common();
    }

    public DAO_Blog(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        helper_common = new Helper_Common();
        storageRef = FirebaseStorage
                .getInstance()
                .getReference("Images/Blog Details/" + userId);
        signInAnonymously();
    }

    public void uploadImageBlogDetail(List<Blog_Details> blog_detailsList, Helper_Callback helper_callback) {
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

//    private void createBlog() {
//        new Helper_Callback() {
//            @Override
//            public void blogDetailList(List<Blog_Details> blog_detailsList) {
//                super.blogDetailList(blog_detailsList);
//            }
//        };
//
//    }

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
