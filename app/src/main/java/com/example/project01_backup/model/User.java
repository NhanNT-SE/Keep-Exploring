package com.example.project01_backup.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String email;
    private String _id;
    private String displayName;
    private String imgUser;
    private List<Post> post;
    private List<Blog> blog;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", _id='" + _id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", imgUser='" + imgUser + '\'' +
                ", post=" + post +
                ", blog=" + blog +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    public List<Blog> getBlog() {
        return blog;
    }

    public void setBlog(List<Blog> blog) {
        this.blog = blog;
    }
}
