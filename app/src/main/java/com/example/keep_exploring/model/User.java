package com.example.keep_exploring.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String email;
    @SerializedName("_id")
    private String id;
    private String displayName;
    private String imgUser;
    @SerializedName("post")
    private List<String> postList;
    @SerializedName("blog")
    private List<String> blogList;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", _id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", imgUser='" + imgUser + '\'' +
                ", postList=" + postList +
                ", blogList=" + blogList +
                '}';
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public List<String> getPostList() {
        return postList;
    }

    public void setPostList(List<String> postList) {
        this.postList = postList;
    }

    public List<String> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<String> blogList) {
        this.blogList = blogList;
    }
}
