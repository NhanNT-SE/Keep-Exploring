package com.example.keep_exploring.model;

import android.net.Uri;

public class Blog_Details {
    String id, content, img;
    Uri uriImage;

    @Override
    public String toString() {
        return "Blog_Details{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", uriImage=" + uriImage +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }
}
