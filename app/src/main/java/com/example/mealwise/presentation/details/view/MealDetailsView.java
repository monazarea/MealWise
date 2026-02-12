package com.example.mealwise.presentation.details.view;

import com.example.mealwise.data.meals.models.Meal;

public interface MealDetailsView {
    void showLoading();
    void hideLoading();
    void showMealDetails(Meal meal);
    void showError(String message);

    void updateFavoriteState(boolean isFavorite);

    void showSuccessMessage(String message);
}