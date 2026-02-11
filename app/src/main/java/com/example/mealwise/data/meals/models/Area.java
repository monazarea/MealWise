package com.example.mealwise.data.meals.models;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    private String name;

    public String getName() { return name; }
}