package com.example.mealwise.presentation.planner.view;

import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

public interface PlannerView {
    void showPlanMeals(List<Meal> meals);
    void showEmptyState();
    void showError(String message);
    void showRemoveSuccess(Meal meal);
    void showGuestWarning();
}
