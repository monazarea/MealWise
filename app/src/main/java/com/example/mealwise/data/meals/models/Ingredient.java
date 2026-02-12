package com.example.mealwise.data.meals.models;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("strIngredient")
    private String name;

    public String getName() { return name; }
}
