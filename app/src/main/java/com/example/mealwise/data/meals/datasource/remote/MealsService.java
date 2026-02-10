package com.example.mealwise.data.meals.datasource.remote;

import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsService {

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoriesResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String categoryName);

    @GET("filter.php")
    Single<MealResponse> getMealsByArea(@Query("a") String area);

    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Single<MealResponse> getMealDetails(@Query("i") String id);

    @GET("search.php")
    Single<MealResponse> searchMealsByName(@Query("s") String mealName);
}
