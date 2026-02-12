package com.example.mealwise.data.meals.models;

import java.io.Serializable;

public class IngredientAndMeasure implements Serializable {
    private String name;
    private String measure;

    public IngredientAndMeasure(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }
}

