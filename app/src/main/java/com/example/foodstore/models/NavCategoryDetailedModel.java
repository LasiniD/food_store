package com.example.foodstore.models;

public class NavCategoryDetailedModel {
    String name;
    String type;
    String image_url;
    int price;

    public NavCategoryDetailedModel() {
    }

    public NavCategoryDetailedModel(String name, String type, String image_url, int price) {
        this.name = name;
        this.type = type;
        this.image_url = image_url;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
