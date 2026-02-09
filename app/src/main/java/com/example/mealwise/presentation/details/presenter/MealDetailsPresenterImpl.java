package com.example.mealwise.presentation.details.presenter;

import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.details.view.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{

    private MealDetailsView view;
    private MealsRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    private boolean isFavorite = false;

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

    @Override
    public void checkFavoriteStatus(String mealId) {
        disposables.add(
                repository.isFavorite(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isFav -> {
                                    this.isFavorite = isFav;
                                    view.updateFavoriteState(isFav);
                                },
                                error -> {}
                        )
        );
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (isFavorite) {
            removeFromFavorites(meal);
        } else {
            addToFavorites(meal);
        }
    }

    private void addToFavorites(Meal meal) {
        disposables.add(
                repository.addToFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    isFavorite = true;
                                    view.updateFavoriteState(true);
                                    view.showSuccessMessage("Added to Favorites");
                                },
                                error -> view.showError("Failed to add: " + error.getMessage())
                        )
        );
    }

    private void removeFromFavorites(Meal meal) {
        disposables.add(
                repository.removeFromFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    isFavorite = false;
                                    view.updateFavoriteState(false);
                                    view.showSuccessMessage("Removed from Favorites");
                                },
                                error -> view.showError("Failed to remove: " + error.getMessage())
                        )
        );
    }

    @Override
    public void addToPlan(Meal meal, String dayOfWeek) {
        disposables.add(
                repository.addToPlan(meal, dayOfWeek)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> view.showSuccessMessage("Added to Plan (" + dayOfWeek + ")"),
                                error -> view.showError("Failed to plan: " + error.getMessage())
                        )
        );
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}