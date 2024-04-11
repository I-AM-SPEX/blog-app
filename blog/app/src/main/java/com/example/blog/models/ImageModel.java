package com.example.blog.models;

public class ImageModel {
    String imageUri;

    public ImageModel() {
    }

    public ImageModel(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
