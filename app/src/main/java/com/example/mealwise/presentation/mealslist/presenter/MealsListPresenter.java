package com.example.mealwise.presentation.mealslist.presenter;

public interface MealsListPresenter {
    public void getMeals(int type, String name);
    public void filterMeals(String query);

    }
