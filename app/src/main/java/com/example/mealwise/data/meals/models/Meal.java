package com.example.mealwise.data.meals.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {

    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String thumbUrl;

    @SerializedName("strYoutube")
    private String youtubeUrl;

    @SerializedName("strSource")
    private String sourceUrl;


    @SerializedName("strIngredient1") private String ingredient1;
    @SerializedName("strIngredient2") private String ingredient2;
    @SerializedName("strIngredient3") private String ingredient3;
    @SerializedName("strIngredient4") private String ingredient4;
    @SerializedName("strIngredient5") private String ingredient5;
    @SerializedName("strIngredient6") private String ingredient6;
    @SerializedName("strIngredient7") private String ingredient7;
    @SerializedName("strIngredient8") private String ingredient8;
    @SerializedName("strIngredient9") private String ingredient9;
    @SerializedName("strIngredient10") private String ingredient10;
    @SerializedName("strIngredient11") private String ingredient11;
    @SerializedName("strIngredient12") private String ingredient12;
    @SerializedName("strIngredient13") private String ingredient13;
    @SerializedName("strIngredient14") private String ingredient14;
    @SerializedName("strIngredient15") private String ingredient15;
    @SerializedName("strIngredient16") private String ingredient16;
    @SerializedName("strIngredient17") private String ingredient17;
    @SerializedName("strIngredient18") private String ingredient18;
    @SerializedName("strIngredient19") private String ingredient19;
    @SerializedName("strIngredient20") private String ingredient20;


    @SerializedName("strMeasure1") private String measure1;
    @SerializedName("strMeasure2") private String measure2;
    @SerializedName("strMeasure3") private String measure3;
    @SerializedName("strMeasure4") private String measure4;
    @SerializedName("strMeasure5") private String measure5;
    @SerializedName("strMeasure6") private String measure6;
    @SerializedName("strMeasure7") private String measure7;
    @SerializedName("strMeasure8") private String measure8;
    @SerializedName("strMeasure9") private String measure9;
    @SerializedName("strMeasure10") private String measure10;
    @SerializedName("strMeasure11") private String measure11;
    @SerializedName("strMeasure12") private String measure12;
    @SerializedName("strMeasure13") private String measure13;
    @SerializedName("strMeasure14") private String measure14;
    @SerializedName("strMeasure15") private String measure15;
    @SerializedName("strMeasure16") private String measure16;
    @SerializedName("strMeasure17") private String measure17;
    @SerializedName("strMeasure18") private String measure18;
    @SerializedName("strMeasure19") private String measure19;
    @SerializedName("strMeasure20") private String measure20;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getArea() { return area; }
    public String getInstructions() { return instructions; }
    public String getThumbUrl() { return thumbUrl; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public String getSourceUrl() { return sourceUrl; }

    public List<Ingredient> getIngredients() {
        List<Ingredient> list = new ArrayList<>();

        addIngredient(list, ingredient1, measure1);
        addIngredient(list, ingredient2, measure2);
        addIngredient(list, ingredient3, measure3);
        addIngredient(list, ingredient4, measure4);
        addIngredient(list, ingredient5, measure5);
        addIngredient(list, ingredient6, measure6);
        addIngredient(list, ingredient7, measure7);
        addIngredient(list, ingredient8, measure8);
        addIngredient(list, ingredient9, measure9);
        addIngredient(list, ingredient10, measure10);
        addIngredient(list, ingredient11, measure11);
        addIngredient(list, ingredient12, measure12);
        addIngredient(list, ingredient13, measure13);
        addIngredient(list, ingredient14, measure14);
        addIngredient(list, ingredient15, measure15);
        addIngredient(list, ingredient16, measure16);
        addIngredient(list, ingredient17, measure17);
        addIngredient(list, ingredient18, measure18);
        addIngredient(list, ingredient19, measure19);
        addIngredient(list, ingredient20, measure20);

        return list;
    }

    private void addIngredient(List<Ingredient> list, String name, String measure) {
        if (name != null && !name.trim().isEmpty()) {
            list.add(new Ingredient(name, measure));
        }
    }
}
