package com.example.mealwise.data.meals.repository;

import com.example.mealwise.data.meals.models.AreasResponse;
import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.IngredientsResponse;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.models.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealsRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoriesResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String categoryName);

    Single<MealResponse> getMealsByArea(String areaName);

    Single<MealResponse> getMealsByIngredient(String ingredientName);

    Single<MealResponse> getMealDetails(String id);

    Completable addToFavorites(Meal meal);

    Completable removeFromFavorites(Meal meal);

    Single<Boolean> isFavorite(String mealId);

    Flowable<List<Meal>> getFavoriteMeals();

    Completable addToPlan(Meal meal, String dayOfWeek);

    Completable removeFromPlan(Meal meal);

    Flowable<List<Meal>> getPlanByDay(String day);

    Single<MealResponse> searchMealsByName(String mealName);

    Single<AreasResponse> getAreasList();

    Single<IngredientsResponse> getIngredientsList();

}