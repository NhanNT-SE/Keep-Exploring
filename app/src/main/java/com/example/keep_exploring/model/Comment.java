package com.example.keep_exploring.model;

public class Comment {
    private String _id, idPost, idBlog;
    private String date, content, img;
    private User idUser;

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", idPost='" + idPost + '\'' +
                ", idBlog='" + idBlog + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", idUser=" + idUser +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }
}
