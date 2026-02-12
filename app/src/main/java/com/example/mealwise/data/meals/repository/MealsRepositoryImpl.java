package com.example.mealwise.data.meals.repository;

import com.example.mealwise.utils.helpers.FirestoreHelper;
import com.example.mealwise.data.meals.datasource.local.MealsLocalDataSource;
import com.example.mealwise.data.meals.datasource.remote.MealsRemoteDataSource;
import com.example.mealwise.data.meals.models.AreasResponse;
import com.example.mealwise.data.meals.models.CategoriesResponse;
import com.example.mealwise.data.meals.models.IngredientsResponse;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.models.MealResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements MealsRepository {

    private static MealsRepositoryImpl instance;
    private final MealsRemoteDataSource remoteDataSource;
    private final MealsLocalDataSource localDataSource;
    private final FirestoreHelper firestoreHelper;

    private MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource, MealsLocalDataSource localDataSource, FirestoreHelper firestoreHelper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.firestoreHelper = firestoreHelper;
    }

    public static MealsRepositoryImpl getInstance(MealsRemoteDataSource remoteDataSource,MealsLocalDataSource localDataSource,FirestoreHelper firestoreHelper) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(remoteDataSource,localDataSource,firestoreHelper);
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

    @Override
    public Single<MealResponse> getMealDetails(String id) {
        return remoteDataSource.getMealDetails(id);
    }

    @Override
    public Single<MealResponse> searchMealsByName(String mealName) {
        return remoteDataSource.searchMealsByName(mealName);
    }

    @Override
    public Completable addToFavorites(Meal meal) {
        String userId = getCurrentUserId();
        if (userId == null) return Completable.error(new Throwable("User not logged in"));

        meal.setType("FAVORITE");
        meal.setUserId(userId);

        return localDataSource.insertMeal(meal)
                .andThen(firestoreHelper.insertMeal(meal));
    }

    @Override
    public Completable removeFromFavorites(Meal meal) {
        String userId = getCurrentUserId();
        if (userId == null) return Completable.error(new Throwable("User not logged in"));
        return localDataSource.deleteFavoriteById(meal.getId(), userId)
                .andThen(firestoreHelper.deleteMeal(meal));
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        String userId = getCurrentUserId();
        if (userId == null) return Single.just(false);

        return localDataSource.isMealFavorite(mealId, userId);
    }



    @Override
    public Flowable<List<Meal>> getFavoriteMeals() {
        String userId = getCurrentUserId();
        if (userId == null) return Flowable.empty();

        Flowable<List<Meal>> localData = localDataSource.getFavoriteMeals(userId);

        return localData.doOnSubscribe(subscription -> syncEverything(userId));
    }

    @Override
    public Flowable<List<Meal>> getPlanByDay(String day) {
        String userId = getCurrentUserId();
        if (userId == null) return Flowable.empty();

        Flowable<List<Meal>> localData = localDataSource.getMealsByDay(day, userId);

        return localData.doOnSubscribe(subscription -> syncEverything(userId));
    }
    private void syncEverything(String userId) {
        firestoreHelper.getMeals(userId)
                .flatMapCompletable(meals -> {
                    for (Meal meal : meals) {
                        meal.setUserId(userId);
                        if (meal.getDayOfWeek() != null && !meal.getDayOfWeek().isEmpty()) {
                            meal.setType("PLAN");
                        } else {
                            meal.setType("FAVORITE");
                        }
                    }
                    return localDataSource.deleteAll(userId)
                            .andThen(localDataSource.insertMeals(meals));
                })
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();
    }


    @Override
    public Completable addToPlan(Meal meal, String dayOfWeek) {
        String userId = getCurrentUserId();
        if (userId == null) return Completable.error(new Throwable("User not logged in"));

        meal.setUserId(userId);
        meal.setType("PLAN");
        meal.setDayOfWeek(dayOfWeek);

        return localDataSource.insertMeal(meal)
                .andThen(firestoreHelper.insertMeal(meal));
    }

    @Override
    public Completable removeFromPlan(Meal meal) {
        return localDataSource.deleteMeal(meal)
                .andThen(firestoreHelper.deleteMeal(meal));
    }



    private String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }


    @Override
    public Single<AreasResponse> getAreasList() {
        return remoteDataSource.getAreasList();
    }

    @Override
    public Single<IngredientsResponse> getIngredientsList() {
        return remoteDataSource.getIngredientsList();
    }


}