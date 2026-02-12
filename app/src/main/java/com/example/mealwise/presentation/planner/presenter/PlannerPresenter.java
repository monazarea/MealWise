package com.example.mealwise.presentation.planner.presenter;

import com.example.mealwise.data.meals.models.Meal;

public interface PlannerPresenter {
    void getPlannedByDay(String day);
    void removeFromPlanned(Meal meal);
    Boolean isGuestMode();
    void onDestroy();
}
