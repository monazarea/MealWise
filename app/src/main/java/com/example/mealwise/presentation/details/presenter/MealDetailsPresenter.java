package com.example.mealwise.presentation.details.presenter;

import com.example.mealwise.data.meals.models.Meal;

public interface MealDetailsPresenter {
    public void getMealDetails(String mealId);

    void checkFavoriteStatus(String mealId);

    void toggleFavorite(Meal meal);

    void addToPlan(Meal meal, String dayOfWeek);

    void onDestroy();
}
