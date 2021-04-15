package com.example.keep_exploring.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    private String status;
    private String content,contentAdmin, created_on;
    @SerializedName("idPost")
    private Post post;
    @SerializedName("idBlog")
    private Blog blog;

    @Override
    public String toString() {
        return "Notification{" +
                "status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", contentAdmin='" + contentAdmin + '\'' +
                ", created_on='" + created_on + '\'' +
                ", post=" + post +
                ", blog=" + blog +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentAdmin() {
        return contentAdmin;
    }

    public void setContentAdmin(String contentAdmin) {
        this.contentAdmin = contentAdmin;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
