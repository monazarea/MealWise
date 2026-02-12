package com.example.mealwise.data.meals.datasource.local;

import android.content.Context;

import com.example.mealwise.data.dp.AppDatabase;
import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSource {

    private final MealsDao dao;
    private static MealsLocalDataSource instance;

    private MealsLocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.dao = db.mealsDao();
    }

    public static MealsLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealsLocalDataSource(context);
        }
        return instance;
    }


    public Completable insertMeal(Meal meal) {
        return dao.insertMeal(meal);
    }
    public Completable insertMeals(List<Meal> meals) {
        return dao.insertAllMeals(meals);
    }

    public Completable deleteMeal(Meal meal) {
        return dao.deleteMeal(meal);
    }

    public Completable deleteFavoriteById(String id, String userId) {
        return dao.deleteFavoriteById(id, userId);
    }
    public Completable deleteAllFavorites(String userId){
        return  dao.deleteAllFavorites(userId);
    }


    public Flowable<List<Meal>> getFavoriteMeals(String userId) {
        return dao.getFavoriteMeals(userId);
    }

    public Flowable<List<Meal>> getMealsByDay(String day, String userId) {
        return dao.getMealsByDay(day, userId);
    }

    public Single<Boolean> isMealFavorite(String apiId, String userId) {
        return dao.isMealFavorite(apiId, userId)
                .map(count -> count > 0);
    }
}