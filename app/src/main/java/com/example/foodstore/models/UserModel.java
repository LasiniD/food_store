package com.example.foodstore.models;

public class UserModel {
    String name;
    String email;
    String password;
    String profileImg;

    public UserModel(){
    }

    public UserModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName(){
        return name;
    }

    public void setName(){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(){
        this.password = password;
    }
}
