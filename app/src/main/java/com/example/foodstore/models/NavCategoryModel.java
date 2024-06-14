package com.example.foodstore.models;

public class NavCategoryModel {
    String name;
    String description;
    String discount;
    String image_url;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String name, String description, String discount, String image_url) {
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

