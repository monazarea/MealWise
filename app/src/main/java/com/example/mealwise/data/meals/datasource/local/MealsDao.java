package com.example.mealwise.data.meals.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealwise.data.meals.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
@Dao
public interface MealsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(Meal meal);

    @Delete
    Completable deleteMeal(Meal meal);

    @Query("SELECT * FROM meals_table WHERE type = 'FAVORITE' AND userId = :userId")
    Flowable<List<Meal>> getFavoriteMeals(String userId);

    @Query("SELECT * FROM meals_table WHERE type = 'PLAN' AND dayOfWeek = :day AND userId = :userId")
    Flowable<List<Meal>> getMealsByDay(String day, String userId);

    @Query("SELECT COUNT(*) FROM meals_table WHERE id = :apiId AND type = 'FAVORITE' AND userId = :userId")
    Single<Integer> isMealFavorite(String apiId, String userId);

    @Query("DELETE FROM meals_table WHERE id = :apiId AND type = 'FAVORITE' AND userId = :userId")
    Completable deleteFavoriteById(String apiId, String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllMeals(List<Meal> meals);

    @Query("DELETE FROM meals_table WHERE type = 'FAVORITE' AND userId = :userId")
    Completable deleteAllFavorites(String userId);

}
