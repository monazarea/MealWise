package com.example.mealwise.data.meals.repository;

import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.MealResponse;
import io.reactivex.rxjava3.core.Single;

public interface MealsRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoriesResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String categoryName);

    Single<MealResponse> getMealsByArea(String areaName);

    Single<MealResponse> getMealsByIngredient(String ingredientName);



}