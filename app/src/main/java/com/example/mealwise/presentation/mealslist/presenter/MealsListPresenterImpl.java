package com.example.mealwise.presentation.mealslist.presenter;

import com.example.mealwise.data.meals.models.MealResponse;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.mealslist.view.MealsListView;
import com.example.mealwise.utils.Constants;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsListPresenterImpl {

    private MealsListView view;
    private MealsRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public MealsListPresenterImpl(MealsListView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getMeals(int type, String name) {
        view.showLoading();

        Single<MealResponse> apiCall = null;

        switch (type) {
            case Constants.TYPE_CATEGORY:
                apiCall = repository.getMealsByCategory(name);
                break;
            case Constants.TYPE_AREA:
                apiCall = repository.getMealsByArea(name);
                break;
            case Constants.TYPE_INGREDIENT:
                apiCall = repository.getMealsByIngredient(name);
                break;
        }

        if (apiCall != null) {
            disposables.add(
                    apiCall
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    response -> {
                                        view.hideLoading();
                                        view.showMeals(response.getMeals());
                                    },
                                    throwable -> {
                                        view.hideLoading();
                                        view.showError(throwable.getMessage());
                                    }
                            )
            );
        }
    }

    public void onDestroy() {
        disposables.clear();
    }
}