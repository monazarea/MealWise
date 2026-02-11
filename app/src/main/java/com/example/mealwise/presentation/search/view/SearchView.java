package com.example.mealwise.presentation.search.view;

import com.example.mealwise.data.meals.models.Area;
import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Ingredient;
import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

public interface SearchView {
    void showLoading();
    void hideLoading();
    void showMeals(List<Meal> meals);
    void showError(String error);
    void showNetworkError();
    void showContent();
    void showEmptyState();
    void clearResults();

    void showCategoriesList(List<Category> categories);
    void showAreasList(List<Area> areas);
    void showIngredientsList(List<Ingredient> ingredients);

}