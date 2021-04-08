package com.example.keep_exploring.model;

import android.net.Uri;

public class ImageDisplay {
    private Uri imageUri;
    private String imageString;

    @Override
    public String toString() {
        return "ImageDisplay{" +
                "imageUri=" + imageUri +
                ", imageString='" + imageString + '\'' +
                '}';
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
