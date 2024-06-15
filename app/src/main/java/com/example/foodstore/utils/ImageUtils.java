package com.example.foodstore.utils;

import com.example.foodstore.models.ImageModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ImageUtils {
    public static List<ImageModel> getImagesFromJson(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ImageModel>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}
