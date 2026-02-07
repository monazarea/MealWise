package com.example.mealwise.data.meals.repository;

import com.example.mealwise.data.meals.datasource.remote.MealsRemoteDataSource;
import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.MealResponse;
import io.reactivex.rxjava3.core.Single;

public class MealsRepositoryImpl implements MealsRepository {

    private static MealsRepositoryImpl instance;
    private final MealsRemoteDataSource remoteDataSource;

    private MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static MealsRepositoryImpl getInstance(MealsRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(remoteDataSource);
        }
        return instance;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoriesResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<MealResponse> getMealsByCategory(String categoryName) {
        return remoteDataSource.getMealsByCategory(categoryName);
    }

    @Override
    public Single<MealResponse> getMealsByArea(String areaName) {
        return remoteDataSource.getMealsByArea(areaName);
    }

    @Override
    public Single<MealResponse> getMealsByIngredient(String ingredientName) {
        return remoteDataSource.getMealsByIngredient(ingredientName);
    }
}