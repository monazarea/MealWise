package com.example.mealwise.presentation.fav.view;

import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

public interface FavoritesView {
    void showFavorites(List<Meal> meals);
    void showEmptyState();
    void showError(String errorMsg);
    void showRemoveSuccess(Meal meal);
    void showGuestWarning();
}
