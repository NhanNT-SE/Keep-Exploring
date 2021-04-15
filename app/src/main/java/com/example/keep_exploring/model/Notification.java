package com.example.keep_exploring.model;

public class Notification {
    private String status,idPost,idBlog;
    private String content,contentAdmin, created_on;

    @Override
    public String toString() {
        return "Notification{" +
                "status='" + status + '\'' +
                ", idPost='" + idPost + '\'' +
                ", idBlog='" + idBlog + '\'' +
                ", content='" + content + '\'' +
                ", contentAdmin='" + contentAdmin + '\'' +
                ", created_on='" + created_on + '\'' +
                '}';
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getIdPost() {
        return idPost;
    }
    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
    public String getIdBlog() {
        return idBlog;
    }
    public void setIdBlog(String idBlog) {
        this.idBlog = idBlog;
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
}
