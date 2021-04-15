package com.example.keep_exploring.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("bod")
    private String birthday;
    @SerializedName("post")
    private List<String> postList;
    @SerializedName("blog")
    private List<String> blogList;
    private String displayName, created_on, gender, address, email;
    private String imgUser;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", postList=" + postList +
                ", blogList=" + blogList +
                ", displayName='" + displayName + '\'' +
                ", created_on='" + created_on + '\'' +
                ", bod='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", imgUser='" + imgUser + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }
}
