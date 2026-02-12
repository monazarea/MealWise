package com.example.mealwise.presentation.mealslist.presenter;

import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.models.MealResponse;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.mealslist.view.MealsListView;
import com.example.mealwise.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsListPresenterImpl implements MealsListPresenter{

    private MealsListView view;
    private MealsRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private List<Meal> allMeals = new ArrayList<>();

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
                                        this.allMeals = response.getMeals();
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

    @Override
    public void filterMeals(String query) {
        if (allMeals == null || allMeals.isEmpty()) return;

        if (query.isEmpty()) {
            view.showMeals(allMeals);
            return;
        }

        disposables.add(
                Observable.fromIterable(allMeals)
                        .filter(meal -> meal.getName().toLowerCase().contains(query.toLowerCase()))
                        .toList()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                filteredList -> view.showMeals(filteredList),
                                throwable -> view.showError(throwable.getMessage())
                        )
        );
    }

    public void onDestroy() {
        disposables.clear();
    }
}