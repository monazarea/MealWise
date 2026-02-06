package com.example.mealwise.data.meals.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("idCategory")
    private String id;

    @SerializedName("strCategory")
    private String name;

    @SerializedName("strCategoryThumb")
    private String thumbUrl;

    @SerializedName("strCategoryDescription")
    private String description;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getThumbUrl() { return thumbUrl; }
    public String getDescription() { return description; }
}