package com.example.mealwise.presentation.search.view;

import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

public interface SearchView {
    void showLoading();
    void hideLoading();
    void showMeals(List<Meal> meals);
    void showError(String error);

    void showEmptyState();
    void clearResults();
}