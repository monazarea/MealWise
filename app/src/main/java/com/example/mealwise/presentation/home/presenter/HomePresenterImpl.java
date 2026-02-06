package com.example.mealwise.presentation.home.presenter;

import android.util.Pair;

import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.home.view.HomeView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public HomePresenterImpl(HomeView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void getHomeData() {
        view.showLoading();
        Single<Pair<Meal, List<Category>>> combinedSingle = Single.zip(
                repository.getRandomMeal().map(response -> response.getMeals().get(0)),
                repository.getCategories().map(response -> response.getCategories()),
                (meal, categories) -> new Pair<>(meal, categories)
        );
        disposables.add(
                combinedSingle
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pair -> {
                                    view.hideLoading();
                                    if (pair.first != null) {
                                        view.showRandomMeal(pair.first);
                                    }
                                    if (pair.second != null && !pair.second.isEmpty()) {
                                        view.showCategories(pair.second);
                                        getMealsByCategory(pair.second.get(0).getName());
                                    }
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }
                        )
        );

    }
    @Override

    public void getMealsByCategory(String categoryName) {
        disposables.add(
                repository.getMealsByCategory(categoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> {
                            List<Meal> allMeals = response.getMeals();
                            if (allMeals != null && allMeals.size() > 10) {
                                return allMeals.subList(0, 10);
                            }
                            return allMeals;
                        })
                        .subscribe(
                                meals -> {
                                    view.showMealsByCategory(meals,categoryName);
                                },
                                throwable -> {
                                    view.showError(throwable.getMessage());
                                }
                        )
        );
    }
    @Override
    public void onDestroy() {
        disposables.clear();
    }
}