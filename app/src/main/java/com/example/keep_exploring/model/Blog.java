package com.example.keep_exploring.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Blog {
    private String _id, title;
    @SerializedName("img")
    private String image;
    private String status, created_on;
    @SerializedName("like_list")
    private List<String> likes;
    @SerializedName("comment")
    private List<String> comments;
    private List<Blog_Details> blogDetails;
    private User owner;

    @Override
    public String toString() {
        return "Blog{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", created_on='" + created_on + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", blogDetails=" + blogDetails +
                ", owner=" + owner +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }


    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<Blog_Details> getBlogDetails() {
        return blogDetails;
    }

    public void setBlogDetails(List<Blog_Details> blogDetails) {
        this.blogDetails = blogDetails;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
