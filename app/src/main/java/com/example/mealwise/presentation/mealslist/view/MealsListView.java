package com.example.mealwise.presentation.mealslist.view;

import com.example.mealwise.data.meals.models.Meal;
import java.util.List;

public interface MealsListView {
    void showLoading();
    void hideLoading();
    void showMeals(List<Meal> meals);
    void showError(String message);
}