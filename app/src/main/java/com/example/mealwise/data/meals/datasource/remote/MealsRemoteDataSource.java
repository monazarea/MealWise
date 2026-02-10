package com.example.mealwise.data.meals.datasource.remote;

import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.MealResponse;
import com.example.mealwise.data.network.APIClient;

import io.reactivex.rxjava3.core.Single;

public class MealsRemoteDataSource {

    private static MealsRemoteDataSource instance;
    private final MealsService mealsService;

    private MealsRemoteDataSource(){
        this.mealsService = APIClient.getInstance().create(MealsService.class);
    }

    public static MealsRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new MealsRemoteDataSource();
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal(){
        return mealsService.getRandomMeal();
    }

    public Single<CategoriesResponse> getCategories() {
        return mealsService.getCategories();
    }

    public Single<MealResponse> getMealsByCategory(String categoryName){
        return mealsService.getMealsByCategory(categoryName);
    }

    public Single<MealResponse> getMealsByIngredient(String categoryName){
        return mealsService.getMealsByIngredient(categoryName);
    }

    public Single<MealResponse> getMealsByArea(String categoryName){
        return mealsService.getMealsByArea(categoryName);
    }
    public Single<MealResponse>getMealDetails(String id){
        return mealsService.getMealDetails(id);
    }

    public Single<MealResponse> searchMealsByName(String mealName) {
        return mealsService.searchMealsByName(mealName);
    }
}
