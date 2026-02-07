package com.example.mealwise.presentation.details.presenter;

import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.details.view.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{

    private MealDetailsView view;
    private MealsRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailsPresenterImpl(MealDetailsView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }
@Override
    public void getMealDetails(String mealId) {
        view.showLoading();

        disposables.add(
                repository.getMealDetails(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                        view.showMealDetails(response.getMeals().get(0));
                                    } else {
                                        view.showError("Meal details not found");
                                    }
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }
                        )
        );
    }

    public void onDestroy() {
        disposables.clear();
    }
}