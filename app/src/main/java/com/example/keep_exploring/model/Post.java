package com.example.keep_exploring.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post  {
    private String _id, category, title;
    private String desc, address, status, created_on;
    private List<String> imgs;
    private int rating;
    @SerializedName("like_list")
    private List<String> likes ;
    @SerializedName("comment")
    private List<String> comments ;
    private User owner;

    @Override
    public String toString() {
        return "Post{" +
                "_id='" + _id + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", created_on='" + created_on + '\'' +
                ", imgs=" + imgs +
                ", rating=" + rating +
                ", likes=" + likes +
                ", comments=" + comments +
                ", owner=" + owner +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
