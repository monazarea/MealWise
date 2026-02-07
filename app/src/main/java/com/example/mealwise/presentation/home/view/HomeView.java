package com.example.mealwise.presentation.home.view;

import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void showRandomMeal(Meal meal);
    void showCategories(List<Category> categories);
    void showMealsByCategory(List<Meal> meals,String categoryName);
    void showError(String message);
    void navigateToDetails(Meal meal);
    void navigateToSeeAll(String categoryName);
}