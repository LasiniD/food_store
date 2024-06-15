package com.example.foodstore.models;

public class NavCategoryModel {
    String name;
    String description;
    String discount;
    String image_url;
    String type;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String name, String description, String discount, String image_url, String type) {
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.image_url = image_url;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

